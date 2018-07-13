/*
 * Copyright 2013 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.service.service.managers;

import com.service.service.Constants;
import com.service.service.Constants.*;
import com.service.service.IStoredSettings;
import com.service.service.Keys;
import com.service.service.entity.*;
import com.service.service.exception.GitBlitException;
import com.service.service.extensions.RepositoryLifeCycleListener;
import com.service.service.service.GarbageCollectorService;
import com.service.service.service.LuceneService;
import com.service.service.service.MirrorService;
import com.service.service.utils.*;
import com.service.service.utils.JGitUtils.LastChange;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.eclipse.jgit.storage.file.WindowCacheConfig;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.jgit.util.RawParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 任务库管理器，创建、更新、删除和缓存 git 存储库。
 * 它还启动用于镜像、索引和清理存储库的服务。
 *
 * @author James Moger
 * @update dk
 */
@Component
public class RepositoryManager implements IRepositoryManager {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(5);

	private final ObjectCache<Long> repositorySizeCache = new ObjectCache<Long>();

	private final ObjectCache<List<Metric>> repositoryMetricsCache = new ObjectCache<List<Metric>>();

	private final Map<String, TaskEntity> repositoryListCache = new ConcurrentHashMap<String, TaskEntity>();

	private final AtomicReference<String> repositoryListSettingsChecksum = new AtomicReference<String>("");

	private final IStoredSettings settings;

	private final IRuntimeManager runtimeManager;

	private final IPluginManager pluginManager;

	private final IUserManager userManager;

	private File repositoriesFolder;

	private LuceneService luceneExecutor;

	private GarbageCollectorService gcExecutor;

	private MirrorService mirrorExecutor;

	public RepositoryManager(
			IRuntimeManager runtimeManager,
			IPluginManager pluginManager,
			IUserManager userManager) {

		this.settings = runtimeManager.getSettings();
		this.runtimeManager = runtimeManager;
		this.pluginManager = pluginManager;
		this.userManager = userManager;
	}

	@Override
	public RepositoryManager start() {
		repositoriesFolder = runtimeManager.getFileOrFolder(Keys.git.repositoriesFolder, "${baseFolder}/git");
		logger.info("Repositories folder : {}", repositoriesFolder.getAbsolutePath());

		// initialize utilities
		String prefix = settings.getString(Keys.git.userRepositoryPrefix, "~");
		ModelUtils.setUserRepoPrefix(prefix);

		// calculate repository list settings checksum for future config changes
		repositoryListSettingsChecksum.set(getRepositoryListSettingsChecksum());

		// build initial repository list
		if (settings.getBoolean(Keys.git.cacheRepositoryList,  true)) {
			logger.info("Identifying repositories...");
			getRepositoryList();
		}

		configureLuceneIndexing();
		configureGarbageCollector();
		configureMirrorExecutor();
		configureJGit();
		configureCommitCache();

		confirmWriteAccess();

		return this;
	}

	@Override
	public RepositoryManager stop() {
		scheduledExecutor.shutdownNow();
		luceneExecutor.close();
		gcExecutor.close();
		mirrorExecutor.close();

		closeAll();
		return this;
	}

	/**
	 * 返回由 WorkHub 服务的任何任务库的最新更改日期。
	 *
	 * @return a date
	 */
	@Override
	public Date getLastActivityDate() {
		Date date = null;
		for (String name : getRepositoryList()) {
			Repository r = getRepository(name);
			Date lastChange = JGitUtils.getLastChange(r).when;
			r.close();
			if (lastChange != null && (date == null || lastChange.after(date))) {
				date = lastChange;
			}
		}
		return date;
	}

	/**
	 * 返回任务库文件夹的路径。此方法检查 WorkHub 是否在云服务上运行, 并可能返回经过调整的路径。
	 *
	 * @return the repositories folder path
	 */
	@Override
	public File getRepositoriesFolder() {
		return repositoriesFolder;
	}

	/**
	 * 返回 Groovy 文件夹的路径。此方法检查 WorkHub 是否在云服务上运行, 并可能返回经过调整的路径。
	 *
	 * @return the Groovy scripts folder path
	 */
	@Override
	public File getHooksFolder() {
		return runtimeManager.getFileOrFolder(Keys.groovy.scriptsFolder, "${baseFolder}/groovy");
	}

	/**
	 * 返回 Groovy文件夹的路径。此方法检查 WorkHub 是否在云服务上运行, 并可能返回经过调整的路径。
	 *
	 * @return the Groovy Grape folder path
	 */
	@Override
	public File getGrapesFolder() {
		return runtimeManager.getFileOrFolder(Keys.groovy.grapeFolder, "${baseFolder}/groovy/grape");
	}

	/**
	 * 垃圾回收
	 * @return true if we are running the gc executor
	 */
	@Override
	public boolean isCollectingGarbage() {
		return gcExecutor != null && gcExecutor.isRunning();
	}

	/**
	 * 如果 WorkHub 正收集此存储库中的垃圾, 则返回 true。
	 *
	 * @param repositoryName a
	 * @return true if actively collecting garbage
	 */
	@Override
	public boolean isCollectingGarbage(String repositoryName) {
		return gcExecutor != null && gcExecutor.isCollectingGarbage(repositoryName);
	}

	/**
	 * 返回此用户的有效权限列表, 其中考虑到团队成员身份、所有权。
	 *
	 * @param user a
	 * @return the effective list of permissions for the user
	 */
	@Override
	public List<RegistrantAccessPermission> getUserAccessPermissions(UserModel user) {
		if (StringUtils.isEmpty(user.username)) {
			// new user
			return new ArrayList<RegistrantAccessPermission>();
		}
		Set<RegistrantAccessPermission> set = new LinkedHashSet<RegistrantAccessPermission>();
		set.addAll(user.getRepositoryPermissions());
		// 标记丢失的存储库
		for (RegistrantAccessPermission permission : set) {
			if (permission.mutable && PermissionType.EXPLICIT.equals(permission.permissionType)) {
				TaskEntity rm = getRepositoryModel(permission.registrant);
				if (rm == null) {
					permission.permissionType = PermissionType.MISSING;
					permission.mutable = false;
					continue;
				}
			}
		}

		// TODO: 重新考虑所有权为用户属性
		// 手动指定个人存储库所有权
		for (TaskEntity rm : repositoryListCache.values()) {
			if (rm.isUsersPersonalRepository(user.username) || rm.isOwner(user.username)) {
				RegistrantAccessPermission rp = new RegistrantAccessPermission(rm.getTaskName(), AccessPermission.REWIND,
						PermissionType.OWNER, RegistrantType.REPOSITORY, null, false);
				// 用户可能是其继承的存储库的所有者
				// 团队权限, 替换任何已有权限和拥有者权限
				set.remove(rp);
				set.add(rp);
			}
		}

		List<RegistrantAccessPermission> list = new ArrayList<RegistrantAccessPermission>(set);
		Collections.sort(list);
		return list;
	}

	/**
	 * 返回指定存储库的用户及其访问权限列表, 包括权限源信息, 如设置该权限的团队或正则表达式。
	 *
	 * @param repository a
	 * @return a list of RegistrantAccessPermissions
	 */
	@Override
	public List<RegistrantAccessPermission> getUserAccessPermissions(TaskEntity repository) {
		List<RegistrantAccessPermission> list = new ArrayList<RegistrantAccessPermission>();
		if (AccessRestrictionType.NONE.equals(repository.getAccessRestriction())) {
			// no permissions needed, REWIND for everyone!
			return list;
		}
		if (AuthorizationControl.AUTHENTICATED.equals(repository.getAuthorizationControl())) {
			// no permissions needed, REWIND for authenticated!
			return list;
		}
		// NAMED users and teams
		for (UserModel user : userManager.getAllUsers()) {
			RegistrantAccessPermission ap = user.getRepositoryPermission(repository);
			if (ap.permission.exceeds(AccessPermission.NONE)) {
				list.add(ap);
			}
		}
		return list;
	}

	/**
	 * 将访问权限设置为指定用户的指定存储库。
	 *
	 * @param repository a
	 * @param permissions a
	 * @return true if the user models have been updated
	 */
	@Override
	public boolean setUserAccessPermissions(TaskEntity repository, Collection<RegistrantAccessPermission> permissions) {
		List<UserModel> users = new ArrayList<UserModel>();
		for (RegistrantAccessPermission up : permissions) {
			if (up.mutable) {
				// only set editable defined permissions
				UserModel user = userManager.getUserModel(up.registrant);
				user.setRepositoryPermission(repository.getTaskName(), up.permission);
				users.add(user);
			}
		}
		return userManager.updateUserModels(users);
	}

	/**
	 * 返回具有指定存储库的显式访问权限的所有用户的列表。
	 *
	 * @param repository a
	 * @return list of all usernames that have an access permission for the repository
	 */
	@Override
	public List<String> getRepositoryUsers(TaskEntity repository) {
		return userManager.getUsernamesForRepositoryRole(repository.getTaskName());
	}

	/**
	 * 返回指定存储库的团队及其访问权限的列表, 包括权限的来源, 如管理标志或正则表达式。
	 *
	 * @param repository a
	 * @return a list of RegistrantAccessPermissions
	 */
	@Override
	public List<RegistrantAccessPermission> getTeamAccessPermissions(TaskEntity repository) {
		List<RegistrantAccessPermission> list = new ArrayList<RegistrantAccessPermission>();
		for (TeamModel team : userManager.getAllTeams()) {
			RegistrantAccessPermission ap = team.getRepositoryPermission(repository);
			if (ap.permission.exceeds(AccessPermission.NONE)) {
				list.add(ap);
			}
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * 为指定的团队设置对指定存储库的访问权限。
	 *
	 * @param repository a
	 * @param permissions a
	 * @return true if the team models have been updated
	 */
	@Override
	public boolean setTeamAccessPermissions(TaskEntity repository, Collection<RegistrantAccessPermission> permissions) {
		List<TeamModel> teams = new ArrayList<TeamModel>();
		for (RegistrantAccessPermission tp : permissions) {
			if (tp.mutable) {
				// only set explicitly defined access permissions
				TeamModel team = userManager.getTeamModel(tp.registrant);
				team.setRepositoryPermission(repository.getTaskName(), tp.permission);
				teams.add(team);
			}
		}
		return userManager.updateTeamModels(teams);
	}

	/**
	 * 返回具有指定存储库的显式访问权限的所有团队的列表。
	 *
	 * @param repository a
	 * @return list of all teamnames with explicit access permissions to the repository
	 */
	@Override
	public List<String> getRepositoryTeams(TaskEntity repository) {
		return userManager.getTeamNamesForRepositoryRole(repository.getTaskName());
	}

	/**
	 * 如果 WorkHub 配置为缓存存储库列表, 则将存储库添加到缓存的存储库列表中。
	 *
	 * @param model a
	 */
	@Override
	public void addToCachedRepositoryList(TaskEntity model) {
		if (settings.getBoolean(Keys.git.cacheRepositoryList, true)) {
			String key = getRepositoryKey(model.getTaskName());
			repositoryListCache.put(key, model);

			// update the fork origin repository with this repository clone
			if (!StringUtils.isEmpty(model.getOriginRepository())) {
				String originKey = getRepositoryKey(model.getOriginRepository());
				if (repositoryListCache.containsKey(originKey)) {
					TaskEntity origin = repositoryListCache.get(originKey);
					origin.addFork(model.getTaskName());
				}
			}
		}
	}

	/**
	 * 从缓存的存储库列表中删除存储库。
	 *
	 * @param name
	 * @return the model being removed
	 */
	private TaskEntity removeFromCachedRepositoryList(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		String key = getRepositoryKey(name);
		return repositoryListCache.remove(key);
	}

	/**
	 * 清除指定存储库的所有缓存元数据。
	 *
	 * @param repositoryName a
	 */
	private void clearRepositoryMetadataCache(String repositoryName) {
		repositorySizeCache.remove(repositoryName);
		repositoryMetricsCache.remove(repositoryName);
		CommitCache.instance().clear(repositoryName);
	}

	/**
	 * 重置此存储库的所有缓存。
	 *
	 * @param repositoryName a
	 * @since 1.5.1
	 */
	@Override
	public void resetRepositoryCache(String repositoryName) {
		removeFromCachedRepositoryList(repositoryName);
		clearRepositoryMetadataCache(repositoryName);
		// force a reload of the repository data (ticket-82, issue-433)
		getRepositoryModel(repositoryName);
	}

	/**
	 * 重置存储库列表缓存。
	 *
	 */
	@Override
	public void resetRepositoryListCache() {
		logger.info("Repository cache manually reset");
		repositoryListCache.clear();
		repositorySizeCache.clear();
		repositoryMetricsCache.clear();
		CommitCache.instance().clear();
	}

	/**
	 * 计算影响存储库列表缓存的设置的校验和。
	 * @return a checksum
	 */
	private String getRepositoryListSettingsChecksum() {
		StringBuilder ns = new StringBuilder();
		ns.append(settings.getString(Keys.git.cacheRepositoryList, "")).append('\n');
		ns.append(settings.getString(Keys.git.onlyAccessBareRepositories, "")).append('\n');
		ns.append(settings.getString(Keys.git.searchRepositoriesSubfolders, "")).append('\n');
		ns.append(settings.getString(Keys.git.searchRecursionDepth, "")).append('\n');
		ns.append(settings.getString(Keys.git.searchExclusions, "")).append('\n');
		String checksum = StringUtils.getSHA1(ns.toString());
		return checksum;
	}

	/**
	 * 将上次存储库列表设置校验和与当前校验和进行比较。如果不同, 则清除缓存, 以便可以重新生成它。
	 *
	 * @return true if the cached repository list is valid since the last check
	 */
	private boolean isValidRepositoryList() {
		String newChecksum = getRepositoryListSettingsChecksum();
		boolean valid = newChecksum.equals(repositoryListSettingsChecksum.get());
		repositoryListSettingsChecksum.set(newChecksum);
		if (!valid && settings.getBoolean(Keys.git.cacheRepositoryList,  true)) {
			logger.info("Repository list settings have changed. Clearing repository list cache.");
			repositoryListCache.clear();
		}
		return valid;
	}

	/**
	 * 返回 WorkHub 可用的所有资料库的列表。此方法不考虑用户访问权限。
	 *
	 * @return list of all repositories
	 */
	@Override
	public List<String> getRepositoryList() {
		if (repositoryListCache.size() == 0 || !isValidRepositoryList()) {
			// 非缓存中、没有缓存或者缓存无效
			long startTime = System.currentTimeMillis();
			List<String> repositories = JGitUtils.getRepositoryList(repositoriesFolder,
					settings.getBoolean(Keys.git.onlyAccessBareRepositories, false),
					settings.getBoolean(Keys.git.searchRepositoriesSubfolders, true),
					settings.getInteger(Keys.git.searchRecursionDepth, -1),
					settings.getStrings(Keys.git.searchExclusions));

			if (!settings.getBoolean(Keys.git.cacheRepositoryList,  true)) {
				// we are not caching
				StringUtils.sortRepositorynames(repositories);
				return repositories;
			} else {
				// we are caching this list
				String msg = "{0} repositories identified in {1} msecs";
				if (settings.getBoolean(Keys.web.showRepositorySizes, true)) {
					// optionally (re)calculate repository sizes
					msg = "{0} repositories identified with calculated folder sizes in {1} msecs";
				}

				for (String repository : repositories) {
					getRepositoryModel(repository);
				}

				// rebuild fork networks
				for (TaskEntity model : repositoryListCache.values()) {
					if (!StringUtils.isEmpty(model.getOriginRepository())) {
						String originKey = getRepositoryKey(model.getOriginRepository());
						if (repositoryListCache.containsKey(originKey)) {
							TaskEntity origin = repositoryListCache.get(originKey);
							origin.addFork(model.getTaskName());
						}
					}
				}

				long duration = System.currentTimeMillis() - startTime;
				logger.info(MessageFormat.format(msg, repositoryListCache.size(), duration));
			}
		}

		// return sorted copy of cached list
		List<String> list = new ArrayList<String>();
		for (TaskEntity model : repositoryListCache.values()) {
			list.add(model.getTaskName());
		}
		StringUtils.sortRepositorynames(list);
		return list;
	}

	/**
	 * 返回指定名称的 JGit 存储库。
	 *
	 * @param repositoryName a
	 * @return repository or null
	 */
	@Override
	public Repository getRepository(String repositoryName) {
		return getRepository(repositoryName, true);
	}

	/**
	 * 返回指定名称的 JGit 存储库。
	 *
	 * @param name a
	 * @param logError a
	 * @return repository or null
	 */
	@Override
	public Repository getRepository(String name, boolean logError) {
		String repositoryName = fixRepositoryName(name);

		if (isCollectingGarbage(repositoryName)) {
			logger.warn(MessageFormat.format("Rejecting request for {0}, busy collecting garbage!", repositoryName));
			return null;
		}

		File dir = FileKey.resolve(new File(repositoriesFolder, repositoryName), FS.DETECTED);
		if (dir == null)
		{return null;}

		Repository r = null;
		try {
			FileKey key = FileKey.exact(dir, FS.DETECTED);
			r = RepositoryCache.open(key, true);
		} catch (IOException e) {
			if (logError) {
				logger.error("GitBlit.getRepository(String) failed to find "
						+ new File(repositoriesFolder, repositoryName).getAbsolutePath());
			}
		}
		return r;
	}

	/**
	 * 返回所有repository models的列表
	 *
	 * @return list of all repository models
	 */
	@Override
	public List<TaskEntity> getRepositoryModels() {
		long methodStart = System.currentTimeMillis();
		List<String> list = getRepositoryList();
		List<TaskEntity> repositories = new ArrayList<TaskEntity>();
		for (String repo : list) {
			TaskEntity model = getRepositoryModel(repo);
			if (model != null) {
				repositories.add(model);
			}
		}
		long duration = System.currentTimeMillis() - methodStart;
		logger.info(MessageFormat.format("{0} repository models loaded in {1} msecs", duration));
		return repositories;
	}

	/**
	 * 返回用户可访问的repository models的列表。
	 *
	 * @param user
	 * @return list of repository models accessible to user
	 */
	@Override
	public List<TaskEntity> getRepositoryModels(UserModel user) {
		long methodStart = System.currentTimeMillis();
		List<String> list = getRepositoryList();
		List<TaskEntity> repositories = new ArrayList<TaskEntity>();
		for (String repo : list) {
			TaskEntity model = getRepositoryModel(user, repo);
			if (model != null) {
				if (!model.isHasCommits()) {
					// only add empty repositories that user can push to
					if (UserModel.ANONYMOUS.canPush(model)
							|| user != null && user.canPush(model)) {
						repositories.add(model);
					}
				} else {
					repositories.add(model);
				}
			}
		}
		long duration = System.currentTimeMillis() - methodStart;
		logger.info(MessageFormat.format("{0} repository models loaded for {1} in {2} msecs",
				repositories.size(), user == null ? "anonymous" : user.username, duration));
		return repositories;
	}

	/**
	 * 如果存储库存在并且用户可以访问存储库, 则返回存储库模型。
	 *
	 * @param user a
	 * @param repositoryName a
	 * @return repository model or null
	 */
	@Override
	public TaskEntity getRepositoryModel(UserModel user, String repositoryName) {
		TaskEntity model = getRepositoryModel(repositoryName);
		if (model == null) {
			return null;
		}
		if (user == null) {
			user = UserModel.ANONYMOUS;
		}
		if (user.canView(model)) {
			return model;
		}
		return null;
	}

	/**
	 * 返回指定存储库的存储库模型。此方法不考虑用户访问权限。
	 *
	 * @param name a
	 * @return repository model or null
	 */
	@Override
	public TaskEntity getRepositoryModel(String name) {
		String repositoryName = fixRepositoryName(name);

		String repositoryKey = getRepositoryKey(repositoryName);
		if (!repositoryListCache.containsKey(repositoryKey)) {
			TaskEntity model = loadRepositoryModel(repositoryName);
			if (model == null) {
				return null;
			}
			addToCachedRepositoryList(model);
			return DeepCopier.copy(model);
		}

		// cached model
		TaskEntity model = repositoryListCache.get(repositoryKey);

		if (isCollectingGarbage(model.getTaskName())) {
			// Gitblit is busy collecting garbage, use our cached model
			TaskEntity rm = DeepCopier.copy(model);
//			rm.isCollectingGarbage = true;
			return rm;
		}

		// check for updates
		Repository r = getRepository(model.getTaskName());
		if (r == null) {
			// repository is missing
			removeFromCachedRepositoryList(repositoryName);
			logger.error(MessageFormat.format("Repository \"{0}\" is missing! Removing from cache.", repositoryName));
			return null;
		}

		FileBasedConfig config = (FileBasedConfig) getRepositoryConfig(r);
		if (config.isOutdated()) {
			// reload model
			logger.debug(MessageFormat.format("Config for \"{0}\" has changed. Reloading model and updating cache.", repositoryName));
			model = loadRepositoryModel(model.getTaskName());
			removeFromCachedRepositoryList(model.getTaskName());
			addToCachedRepositoryList(model);
		} else {
			// update a few repository parameters
			if (!model.isHasCommits()) {
				// update hasCommits, assume a repository only gains commits :)
				model.setHasCommits(JGitUtils.hasCommits(r));
			}

			updateLastChangeFields(r, model);
		}
		r.close();

		// return a copy of the cached model
		return DeepCopier.copy(model);
	}

	/**
	 * 替换存储库名称中的非法字符模式。
	 *
	 * @param repositoryName a
	 * @return a corrected name
	 */
	private String fixRepositoryName(String repositoryName) {
		if (StringUtils.isEmpty(repositoryName)) {
			return repositoryName;
		}

		// Decode url-encoded repository name (issue-278)
		// http://stackoverflow.com/questions/17183110
		String name  = repositoryName.replace("%7E", "~").replace("%7e", "~");
		name = name.replace("%2F", "/").replace("%2f", "/");

		if (name.charAt(name.length() - 1) == '/') {
			name = name.substring(0, name.length() - 1);
		}

		// strip duplicate-slashes from requests for repositoryName (ticket-117, issue-454)
		// specify first char as slash so we strip leading slashes
		char lastChar = '/';
		StringBuilder sb = new StringBuilder();
		for (char c : name.toCharArray()) {
			if (c == '/' && lastChar == c) {
				continue;
			}
			sb.append(c);
			lastChar = c;
		}

		return sb.toString();
	}

	/**
	 * 返回存储库名称的缓存键。
	 *
	 * @param repositoryName a
	 * @return the cache key for the repository
	 */
	private String getRepositoryKey(String repositoryName) {
		String name = fixRepositoryName(repositoryName);
		return StringUtils.stripDotGit(name).toLowerCase();
	}

	/**
	 * 变通办法 JGit。
	 * 我需要直接访问原始配置对象, 以查看配置是否是脏的, 这样我就可以重新加载存储库模型。
	 * 如果我使用股票 JGit 方法获取配置, 它已经重新加载配置。
	 * 如果配置更改是在 WorkHub 内进行的, 则此情况很好, 因为返回的配置仍将标记为脏。
	 * 但是... 如果配置是在 WorkHub 之外操作的, 那么它就无法识别这是脏的。
	 *
	 * @param r
	 * @return a config
	 */
	private StoredConfig getRepositoryConfig(Repository r) {
		try {
			Field f = r.getClass().getDeclaredField("repoConfig");
			f.setAccessible(true);
			StoredConfig config = (StoredConfig) f.get(r);
			return config;
		} catch (Exception e) {
			logger.error("Failed to retrieve \"repoConfig\" via reflection", e);
		}
		return r.getConfig();
	}

	/**
	 * 从配置和存储库数据创建任务库模型。
	 *
	 * @param repositoryName a
	 * @return a repositoryModel or null if the repository does not exist
	 */
	private TaskEntity loadRepositoryModel(String repositoryName) {
		Repository r = getRepository(repositoryName);
		if (r == null) {
			return null;
		}
		TaskEntity model = new TaskEntity();
		model.setBare(r.isBare());
		File basePath = getRepositoriesFolder();
		if (model.isBare()) {
			model.setTaskName(com.service.service.utils.FileUtils.getRelativePath(basePath, r.getDirectory()));
		} else {
			model.setTaskName(com.service.service.utils.FileUtils.getRelativePath(basePath, r.getDirectory().getParentFile()));
		}
		if (StringUtils.isEmpty(model.getTaskName())) {
			// Repository is NOT located relative to the base folder because it
			// is symlinked.  Use the provided repository name.
			model.setTaskName(repositoryName);
		}
		model.setProjectPath(StringUtils.getFirstPathElement(repositoryName));

		StoredConfig config = r.getConfig();
		boolean hasOrigin = false;

		if (config != null) {
			// Initialize description from description file
			hasOrigin = !StringUtils.isEmpty(config.getString("remote", "origin", "url"));
			if (getConfig(config,"description", null) == null) {
				File descFile = new File(r.getDirectory(), "description");
				if (descFile.exists()) {
					String desc = com.service.service.utils.FileUtils.readContent(descFile, System.getProperty("line.separator"));
					if (!desc.toLowerCase().startsWith("unnamed repository")) {
						config.setString(Constants.CONFIG_GITBLIT, null, "description", desc);
					}
				}
			}
			model.setTaskDes(getConfig(config, "description", ""));
			model.setOriginRepository(getConfig(config, "originRepository", null));
			model.addOwners(ArrayUtils.fromString(getConfig(config, "owner", "")));
			model.setAcceptNewPatchsets(getConfig(config, "acceptNewPatchsets", true));
			model.setAcceptNewTickets(getConfig(config, "acceptNewTickets", true));
			model.setRequireApproval(getConfig(config, "requireApproval", settings.getBoolean(Keys.tickets.requireApproval, false)));
			model.setMergeTo(getConfig(config, "mergeTo", null));
			model.setMergeType(MergeType.fromName(getConfig(config, "mergeType", settings.getString(Keys.tickets.mergeType, null))));
			model.setUseIncrementalPushTags(getConfig(config, "useIncrementalPushTags", false));
			model.setIncrementalPushTagPrefix(getConfig(config, "incrementalPushTagPrefix", null));
			model.setAllowForks(getConfig(config, "allowForks", true));
			model.setAccessRestriction(AccessRestrictionType.fromName(getConfig(config,
					"accessRestriction", settings.getString(Keys.git.defaultAccessRestriction, "PUSH"))));
			model.setAuthorizationControl(AuthorizationControl.fromName(getConfig(config,
					"authorizationControl", settings.getString(Keys.git.defaultAuthorizationControl, null))));
			model.setVerifyCommitter(getConfig(config, "verifyCommitter", false));
//			model.showRemoteBranches = getConfig(config, "showRemoteBranches", hasOrigin);
			model.setFrozen(getConfig(config, "isFrozen", false));
//			model.skipSizeCalculation = getConfig(config, "skipSizeCalculation", false);
//			model.skipSummaryMetrics = getConfig(config, "skipSummaryMetrics", false);
//			model.commitMessageRenderer = CommitMessageRenderer.fromName(getConfig(config, "commitMessageRenderer",
//					settings.getString(Keys.web.commitMessageRenderer, null)));
//			model.federationStrategy = FederationStrategy.fromName(getConfig(config,
//					"federationStrategy", null));
//			model.federationSets = new ArrayList<String>(Arrays.asList(config.getStringList(
//					Constants.CONFIG_GITBLIT, null, "federationSets")));
//			model.isFederated = getConfig(config, "isFederated", false);
//			model.gcThreshold = getConfig(config, "gcThreshold", settings.getString(Keys.git.defaultGarbageCollectionThreshold, "500KB"));
//			model.gcPeriod = getConfig(config, "gcPeriod", settings.getInteger(Keys.git.defaultGarbageCollectionPeriod, 7));
//			try {
//				model.lastGC = new SimpleDateFormat(Constants.ISO8601).parse(getConfig(config, "lastGC", "1970-01-01'T'00:00:00Z"));
//			} catch (Exception e) {
//				model.lastGC = new Date(0);
//			}
			model.setMaxActivityCommits(getConfig(config, "maxActivityCommits", settings.getInteger(Keys.web.maxActivityCommits, 0)));
			model.setOriginRepository(config.getString("remote", "origin", "url"));
			if (model.getOrigin() != null) {
				model.setOrigin(model.getOrigin().replace('\\', '/'));
				model.setMirror(config.getBoolean("remote", "origin", "mirror", false));
			}
//			model.preReceiveScripts = new ArrayList<String>(Arrays.asList(config.getStringList(
//					Constants.CONFIG_GITBLIT, null, "preReceiveScript")));
//			model.postReceiveScripts = new ArrayList<String>(Arrays.asList(config.getStringList(
//					Constants.CONFIG_GITBLIT, null, "postReceiveScript")));
//			model.mailingLists = new ArrayList<String>(Arrays.asList(config.getStringList(
//					Constants.CONFIG_GITBLIT, null, "mailingList")));
//			model.indexedBranches = new ArrayList<String>(Arrays.asList(config.getStringList(
//					Constants.CONFIG_GITBLIT, null, "indexBranch")));
//			model.metricAuthorExclusions = new ArrayList<String>(Arrays.asList(config.getStringList(
//					Constants.CONFIG_GITBLIT, null, "metricAuthorExclusions")));

			// Custom defined properties
//			model.customFields = new LinkedHashMap<String, String>();
//			for (String aProperty : config.getNames(Constants.CONFIG_GITBLIT, Constants.CONFIG_CUSTOM_FIELDS)) {
//				model.customFields.put(aProperty, config.getString(Constants.CONFIG_GITBLIT, Constants.CONFIG_CUSTOM_FIELDS, aProperty));
//			}
		}
		model.setHead(JGitUtils.getHEADRef(r));
		if (StringUtils.isEmpty(model.getMergeTo())) {
			model.setMergeTo(model.getHead());
		}
		model.setAvailableRefs(JGitUtils.getAvailableHeadTargets(r));
//		model.sparkleshareId = JGitUtils.getSparkleshareId(r);
		model.setHasCommits(JGitUtils.hasCommits(r));
		updateLastChangeFields(r, model);
		r.close();

		if (StringUtils.isEmpty(model.getOriginRepository()) && model.getOrigin() != null && model.getOrigin().startsWith("file://")) {
			// repository was cloned locally... perhaps as a fork
			try {
				File folder = new File(new URI(model.getOrigin()));
				String originRepo = com.service.service.utils.FileUtils.getRelativePath(getRepositoriesFolder(), folder);
				if (!StringUtils.isEmpty(originRepo)) {
					// ensure origin still exists
					File repoFolder = new File(getRepositoriesFolder(), originRepo);
					if (repoFolder.exists()) {
						model.setOriginRepository(originRepo.toLowerCase());

						// persist the fork origin
						updateConfiguration(r, model);
					}
				}
			} catch (URISyntaxException e) {
				logger.error("Failed to determine fork for " + model, e);
			}
		}
		return model;
	}

	/**
	 * 确定此服务器是否具有所请求的存储库。
	 *
	 * @param repositoryName
	 * @return true if the repository exists
	 */
	@Override
	public boolean hasRepository(String repositoryName) {
		return hasRepository(repositoryName, false);
	}

	/**
	 * 确定此服务器是否具有所请求的存储库。
	 *
	 * @param repositoryName
	 * @param caseSensitiveCheck
	 * @return true if the repository exists
	 */
	@Override
	public boolean hasRepository(String repositoryName, boolean caseSensitiveCheck) {
		if (!caseSensitiveCheck && settings.getBoolean(Keys.git.cacheRepositoryList, true)) {
			// if we are caching use the cache to determine availability
			// otherwise we end up adding a phantom repository to the cache
			String key = getRepositoryKey(repositoryName);
			return repositoryListCache.containsKey(key);
		}
		Repository r = getRepository(repositoryName, false);
		if (r == null) {
			return false;
		}
		r.close();
		return true;
	}

	/**
	 * 确定指定的用户是否具有指定源的分支库
	 *
	 * @param username
	 * @param origin
	 * @return true the if the user has a fork
	 */
	@Override
	public boolean hasFork(String username, String origin) {
		return getFork(username, origin) != null;
	}

	/**
	 * 获取指定源存储库的用户分叉的名称。
	 *
	 * @param username
	 * @param origin
	 * @return the name of the user's fork, null otherwise
	 */
	@Override
	public String getFork(String username, String origin) {
		if (StringUtils.isEmpty(origin)) {
			return null;
		}
		String userProject = ModelUtils.getPersonalPath(username);
		if (settings.getBoolean(Keys.git.cacheRepositoryList, true)) {
			String originKey = getRepositoryKey(origin);
			String userPath = userProject + "/";

			// collect all origin nodes in fork network
			Set<String> roots = new HashSet<String>();
			roots.add(originKey);
			TaskEntity originModel = repositoryListCache.get(originKey);
			while (originModel != null) {
				if (!ArrayUtils.isEmpty(originModel.getForks())) {
					for (String fork : originModel.getForks()) {
						if (!fork.startsWith(userPath)) {
							roots.add(fork.toLowerCase());
						}
					}
				}

				if (originModel.getOriginRepository() != null) {
					String ooKey = getRepositoryKey(originModel.getOriginRepository());
					roots.add(ooKey);
					originModel = repositoryListCache.get(ooKey);
				} else {
					// break
					originModel = null;
				}
			}

			for (String repository : repositoryListCache.keySet()) {
				if (repository.startsWith(userPath)) {
					TaskEntity model = repositoryListCache.get(repository);
					if (!StringUtils.isEmpty(model.getOriginRepository())) {
						String ooKey = getRepositoryKey(model.getOriginRepository());
						if (roots.contains(ooKey)) {
							// user has a fork in this graph
							return model.getTaskName();
						}
					}
				}
			}
		} else {
			// not caching
			File subfolder = new File(getRepositoriesFolder(), userProject);
			List<String> repositories = JGitUtils.getRepositoryList(subfolder,
					settings.getBoolean(Keys.git.onlyAccessBareRepositories, false),
					settings.getBoolean(Keys.git.searchRepositoriesSubfolders, true),
					settings.getInteger(Keys.git.searchRecursionDepth, -1),
					settings.getStrings(Keys.git.searchExclusions));
			for (String repository : repositories) {
				TaskEntity model = getRepositoryModel(userProject + "/" + repository);
				if (model.getOriginRepository() != null && model.getOriginRepository().equalsIgnoreCase(origin)) {
					// user has a fork
					return model.getTaskName();
				}
			}
		}
		// user does not have a fork
		return null;
	}

	/**
	 * 通过遍历分叉图以发现根节点的所有子级, 然后向下, 返回存储库的叉网络。
	 *
	 * @param repository
	 * @return a ForkModel
	 */
	@Override
	public ForkModel getForkNetwork(String repository) {
		if (settings.getBoolean(Keys.git.cacheRepositoryList, true)) {
			// find the root, cached
			String key = getRepositoryKey(repository);
			TaskEntity model = repositoryListCache.get(key);
			if (model == null) {
				return null;
			}

			while (model.getOriginRepository() != null) {
				String originKey = getRepositoryKey(model.getOriginRepository());
				model = repositoryListCache.get(originKey);
				if (model == null) {
					return null;
				}
			}
			ForkModel root = getForkModelFromCache(model.getTaskName());
			return root;
		} else {
			// find the root, non-cached
			TaskEntity model = getRepositoryModel(repository.toLowerCase());
			while (model.getOriginRepository() != null) {
				model = getRepositoryModel(model.getOriginRepository());
			}
			ForkModel root = getForkModel(model.getTaskName());
			return root;
		}
	}

	private ForkModel getForkModelFromCache(String repository) {
		String key = getRepositoryKey(repository);
		TaskEntity model = repositoryListCache.get(key);
		if (model == null) {
			return null;
		}
		ForkModel fork = new ForkModel(model);
		if (!ArrayUtils.isEmpty(model.getForks())) {
			for (String aFork : model.getForks()) {
				ForkModel fm = getForkModelFromCache(aFork);
				if (fm != null) {
					fork.forks.add(fm);
				}
			}
		}
		return fork;
	}

	private ForkModel getForkModel(String repository) {
		TaskEntity model = getRepositoryModel(repository.toLowerCase());
		if (model == null) {
			return null;
		}
		ForkModel fork = new ForkModel(model);
		if (!ArrayUtils.isEmpty(model.getForks())) {
			for (String aFork : model.getForks()) {
				ForkModel fm = getForkModel(aFork);
				if (fm != null) {
					fork.forks.add(fm);
				}
			}
		}
		return fork;
	}

	/**
	 * 更新上次更改的字段, 并可选择计算存储库的大小。
	 * WorkHub 缓存存储库大小以降低递归计算的性能损失。
	 * 如果存储库自上次计算以来发生更改, 则更新缓存。
	 *
	 * @param model
	 * @return size in bytes of the repository
	 */
	@Override
	public long updateLastChangeFields(Repository r, TaskEntity model) {
		LastChange lc = JGitUtils.getLastChange(r);
		model.setUpdTime(lc.when);
		model.setUpdUser(lc.who);

		if (!settings.getBoolean(Keys.web.showRepositorySizes, true) || model.isSkipSizeCalculation()) {
			model.setSize(null);
			return 0L;
		}
		if (!repositorySizeCache.hasCurrent(model.getTaskName(), model.getUpdTime())) {
			File gitDir = r.getDirectory();
			long sz = com.service.service.utils.FileUtils.folderSize(gitDir);
			repositorySizeCache.updateObject(model.getTaskName(), model.getUpdTime(), sz);
		}
		long size = repositorySizeCache.getObject(model.getTaskName());
		ByteFormat byteFormat = new ByteFormat();
		model.setSize(byteFormat.format(size));
		return size;
	}

	/**
	 * 如果存储库处于空闲状态 (无法访问), 则返回 true。
	 *
	 * @param repository
	 * @return true if the repository is idle
	 */
	@Override
	public boolean isIdle(Repository repository) {
		try {
			// Read the use count.
			// An idle use count is 2:
			// +1 for being in the cache
			// +1 for the repository parameter in this method
			Field useCnt = Repository.class.getDeclaredField("useCnt");
			useCnt.setAccessible(true);
			int useCount = ((AtomicInteger) useCnt.get(repository)).get();
			return useCount == 2;
		} catch (Exception e) {
			logger.warn(MessageFormat
					.format("Failed to reflectively determine use count for repository {0}",
							repository.getDirectory().getPath()), e);
		}
		return false;
	}

	/**
	 * 确保所有缓存的存储库完全关闭, 并正确释放它们的资源。
	 */
	@Override
	public void closeAll() {
		for (String repository : getRepositoryList()) {
			close(repository);
		}
	}

	/**
	 * 确保已缓存的存储库完全关闭, 并且其资源已正确释放。
	 *
	 * @param repositoryName
	 */
	@Override
	public void close(String repositoryName) {
		Repository repository = getRepository(repositoryName);
		if (repository == null) {
			return;
		}
		RepositoryCache.close(repository);

		// assume 2 uses in case reflection fails
		int uses = 2;
		try {
			// The FileResolver caches repositories which is very useful
			// for performance until you want to delete a repository.
			// I have to use reflection to call close() the correct
			// number of times to ensure that the object and ref databases
			// are properly closed before I can delete the repository from
			// the filesystem.
			Field useCnt = Repository.class.getDeclaredField("useCnt");
			useCnt.setAccessible(true);
			uses = ((AtomicInteger) useCnt.get(repository)).get();
		} catch (Exception e) {
			logger.warn(MessageFormat
					.format("Failed to reflectively determine use count for repository {0}",
							repositoryName), e);
		}
		if (uses > 0) {
			logger.debug(MessageFormat
					.format("{0}.useCnt={1}, calling close() {2} time(s) to close object and ref databases",
							repositoryName, uses, uses));
			for (int i = 0; i < uses; i++) {
				repository.close();
			}
		}

		// close any open index writer/searcher in the Lucene executor
		luceneExecutor.close(repositoryName);
	}

	/**
	 * 返回指定存储库的默认分支的度量值。
	 * 此方法生成度量值缓存。
	 * 如果更新了存储库, 则会更新缓存。
	 * 每个调用都返回度量值列表的新副本, 以便对列表的修改是非破坏性的。
	 *
	 * @param model
	 * @param repository
	 * @return a new array list of metrics
	 */
	@Override
	public List<Metric> getRepositoryDefaultMetrics(TaskEntity model, Repository repository) {
		if (repositoryMetricsCache.hasCurrent(model.getTaskName(), model.getUpdTime())) {
			return new ArrayList<Metric>(repositoryMetricsCache.getObject(model.getTaskName()));
		}
		List<Metric> metrics = MetricUtils.getDateMetrics(repository, null, true, null, runtimeManager.getTimezone());
		repositoryMetricsCache.updateObject(model.getTaskName(), model.getUpdTime(), metrics);
		return new ArrayList<Metric>(metrics);
	}

	/**
	 * 返回指定键的 WorkHub 字符串值。如果未设置键, 则返回默认。
	 *
	 * @param config
	 * @param field
	 * @param defaultValue
	 * @return field value or defaultValue
	 */
	private String getConfig(StoredConfig config, String field, String defaultValue) {
		String value = config.getString(Constants.CONFIG_GITBLIT, null, field);
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * 返回指定键的 WorkHub 布尔值。如果未设置键, 则返回默认。
	 *
	 * @param config
	 * @param field
	 * @param defaultValue
	 * @return field value or defaultValue
	 */
	private boolean getConfig(StoredConfig config, String field, boolean defaultValue) {
		return config.getBoolean(Constants.CONFIG_GITBLIT, field, defaultValue);
	}

	/**
	 * 返回指定键的 gitblit 字符串值。如果未设置键, 则返回默认。
	 *
	 * @param config
	 * @param field
	 * @param defaultValue
	 * @return field value or defaultValue
	 */
	private int getConfig(StoredConfig config, String field, int defaultValue) {
		String value = config.getString(Constants.CONFIG_GITBLIT, null, field);
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * 创建/更新由 repositoryName 键控的存储库模型。
	 * 保存. git/config 中的所有存储库设置。
	 * 此方法允许重命名存储库, 并将相应地更新用户访问权限。
	 *
	 * 此方法创建的所有存储库都是裸的,并且自动具有.git附加到它们的名称, 这是裸存储库的标准约定。
     * 创建新库已经完成改造
	 *
	 * @param repositoryName 项目名/任务名格式
	 * @param repository 库模型
	 * @param isCreate 是否创建
	 * @throws GitBlitException e
	 */
	@Override
	public void updateRepositoryModel(String repositoryName, TaskEntity repository,
			boolean isCreate) throws GitBlitException {

		if (isCollectingGarbage(repositoryName)) {
			throw new GitBlitException(MessageFormat.format("sorry, Gitblit is busy collecting garbage in {0}",
					repositoryName));
		}
		Repository r = null;
//		String projectPath = StringUtils.getFirstPathElement(repository.getTaskName());
		String projectPath = repository.getTaskProjectName();
		if (!StringUtils.isEmpty(projectPath)) {
			if (projectPath.equalsIgnoreCase(settings.getString(Keys.web.repositoryRootGroupName, "main"))) {
				// 带项目名称的仓库全称
				repository.setTaskName(repository.getTaskName().substring(projectPath.length() + 1));
			}
		}
		boolean isRename = false;
		if (isCreate) {
			// 确保创建的存储库名称以.git结尾
			if (!repository.getTaskName().toLowerCase().endsWith(org.eclipse.jgit.lib.Constants.DOT_GIT_EXT)) {
				repository.setTaskName(repository.getTaskName() + org.eclipse.jgit.lib.Constants.DOT_GIT_EXT);
			}
			if (hasRepository(repository.getTaskName())) {
				throw new GitBlitException(MessageFormat.format(
						"Can not create repository ''{0}'' because it already exists.",
						repository.getTaskName()));
			}
			// create repository
			logger.info("create repository " + repository.getTaskName());
			String shared = settings.getString(Keys.git.createRepositoriesShared, "FALSE");
			r = JGitUtils.createRepository(repositoriesFolder, repository.getTaskName(), shared);
		} else {
			// rename repository
			isRename = !repositoryName.equalsIgnoreCase(repository.getTaskName());
			if (isRename) {
				if (!repository.getTaskName().toLowerCase().endsWith(
						org.eclipse.jgit.lib.Constants.DOT_GIT_EXT)) {
					repository.setTaskName(repository.getTaskName() + org.eclipse.jgit.lib.Constants.DOT_GIT_EXT);
				}
				if (new File(repositoriesFolder, repository.getTaskName()).exists()) {
					throw new GitBlitException(MessageFormat.format(
							"Failed to rename ''{0}'' because ''{1}'' already exists.",
							repositoryName, repository.getTaskName()));
				}
				close(repositoryName);
				File folder = new File(repositoriesFolder, repositoryName);
				File destFolder = new File(repositoriesFolder, repository.getTaskName());
				if (destFolder.exists()) {
					throw new GitBlitException(
							MessageFormat
									.format("Can not rename repository ''{0}'' to ''{1}'' because ''{1}'' already exists.",
											repositoryName, repository.getTaskName()));
				}
				File parentFile = destFolder.getParentFile();
				if (!parentFile.exists() && !parentFile.mkdirs()) {
					throw new GitBlitException(MessageFormat.format(
							"Failed to create folder ''{0}''", parentFile.getAbsolutePath()));
				}
				if (!folder.renameTo(destFolder)) {
					throw new GitBlitException(MessageFormat.format(
							"Failed to rename repository ''{0}'' to ''{1}''.", repositoryName,
							repository.getTaskName()));
				}
				// rename the roles
				if (!userManager.renameRepositoryRole(repositoryName, repository.getTaskName())) {
					throw new GitBlitException(MessageFormat.format(
							"Failed to rename repository permissions ''{0}'' to ''{1}''.",
							repositoryName, repository.getTaskName()));
				}

				// rename fork origins in their configs
				if (!ArrayUtils.isEmpty(repository.getForks())) {
					for (String fork : repository.getForks()) {
						Repository rf = getRepository(fork);
						try {
							StoredConfig config = rf.getConfig();
							String origin = config.getString("remote", "origin", "url");
							origin = origin.replace(repositoryName, repository.getTaskName());
							config.setString("remote", "origin", "url", origin);
							config.setString(Constants.CONFIG_GITBLIT, null, "originRepository", repository.getTaskName());
							config.save();
						} catch (Exception e) {
							logger.error("Failed to update repository fork config for " + fork, e);
						}
						rf.close();
					}
				}

				// update this repository's origin's fork list
				if (!StringUtils.isEmpty(repository.getOriginRepository())) {
					String originKey = getRepositoryKey(repository.getOriginRepository());
					TaskEntity origin = repositoryListCache.get(originKey);
					if (origin != null && !ArrayUtils.isEmpty(origin.getForks())) {
						origin.getForks().remove(repositoryName);
						origin.getForks().add(repository.getTaskName());
					}
				}

				// clear the cache
				clearRepositoryMetadataCache(repositoryName);
//				repository.resetDisplayName();
			}

			// load repository
			logger.info("edit repository " + repository.getTaskName());
			r = getRepository(repository.getTaskName());
		}

		// update settings
		if (r != null) {
			updateConfiguration(r, repository);
			// Update the description file
			File descFile = new File(r.getDirectory(), "description");
			if (repository.getTaskDes() != null)
			{
				com.service.service.utils.FileUtils.writeContent(descFile, repository.getTaskDes());
			}
			else if (descFile.exists() && !descFile.isDirectory()) {
				descFile.delete();
			}
			// only update symbolic head if it changes
			String currentRef = JGitUtils.getHEADRef(r);
			if (!StringUtils.isEmpty(repository.getHead()) && !repository.getHead().equals(currentRef)) {
				logger.info(MessageFormat.format("Relinking {0} HEAD from {1} to {2}",
						repository.getTaskName(), currentRef, repository.getHead()));
				if (JGitUtils.setHEADtoRef(r, repository.getHead())) {
					// clear the cache
					clearRepositoryMetadataCache(repository.getTaskName());
				}
			}

			// Adjust permissions in case we updated the config files
			JGitUtils.adjustSharedPerm(new File(r.getDirectory().getAbsolutePath(), "config"),
					settings.getString(Keys.git.createRepositoriesShared, "FALSE"));
			JGitUtils.adjustSharedPerm(new File(r.getDirectory().getAbsolutePath(), "HEAD"),
					settings.getString(Keys.git.createRepositoriesShared, "FALSE"));

			// close the repository object
			r.close();
		}

		// update repository cache
		removeFromCachedRepositoryList(repositoryName);
		// model will actually be replaced on next load because config is stale
		addToCachedRepositoryList(repository);

		if (isCreate && pluginManager != null) {
			for (RepositoryLifeCycleListener listener : pluginManager.getExtensions(RepositoryLifeCycleListener.class)) {
				try {
					listener.onCreation(repository);
				} catch (Throwable t) {
					logger.error(String.format("failed to call plugin onCreation %s", repositoryName), t);
				}
			}
		} else if (isRename && pluginManager != null) {
			for (RepositoryLifeCycleListener listener : pluginManager.getExtensions(RepositoryLifeCycleListener.class)) {
				try {
					listener.onRename(repositoryName, repository);
				} catch (Throwable t) {
					logger.error(String.format("failed to call plugin onRename %s", repositoryName), t);
				}
			}
		}
	}

	/**
	 * 更新指定存储库的 Gitblit 配置。
	 *
	 * @param r
	 *            the Git repository
	 * @param repository
	 *            the Gitblit repository model
	 */
	@Override
	public void updateConfiguration(Repository r, TaskEntity repository) {
		StoredConfig config = r.getConfig();
		config.setString(Constants.CONFIG_GITBLIT, null, "description", repository.getTaskDes());
		config.setString(Constants.CONFIG_GITBLIT, null, "originRepository", repository.getOriginRepository());
		config.setString(Constants.CONFIG_GITBLIT, null, "owner", ArrayUtils.toString(repository.getOwners()));
//		config.setBoolean(Constants.CONFIG_GITBLIT, null, "acceptNewPatchsets", repository.acceptNewPatchsets);
//		config.setBoolean(Constants.CONFIG_GITBLIT, null, "acceptNewTickets", repository.acceptNewTickets);
//		if (settings.getBoolean(Keys.tickets.requireApproval, false) == repository.requireApproval) {
//			// use default
//			config.unset(Constants.CONFIG_GITBLIT, null, "requireApproval");
//		} else {
//			// override default
//			config.setBoolean(Constants.CONFIG_GITBLIT, null, "requireApproval", repository.requireApproval);
//		}
		if (!StringUtils.isEmpty(repository.getMergeTo())) {
			config.setString(Constants.CONFIG_GITBLIT, null, "mergeTo", repository.getMergeTo());
		}
		if (repository.getMergeType() == null || repository.getMergeType() == MergeType.fromName(settings.getString(Keys.tickets.mergeType, null))) {
			// use default
			config.unset(Constants.CONFIG_GITBLIT, null, "mergeType");
		} else {
			// override default
			config.setString(Constants.CONFIG_GITBLIT, null, "mergeType", repository.getMergeType().name());
		}
//		config.setBoolean(Constants.CONFIG_GITBLIT, null, "useIncrementalPushTags", repository.useIncrementalPushTags);
//		if (StringUtils.isEmpty(repository.incrementalPushTagPrefix) ||
//				repository.incrementalPushTagPrefix.equals(settings.getString(Keys.git.defaultIncrementalPushTagPrefix, "r"))) {
//			config.unset(Constants.CONFIG_GITBLIT, null, "incrementalPushTagPrefix");
//		} else {
//			config.setString(Constants.CONFIG_GITBLIT, null, "incrementalPushTagPrefix", repository.incrementalPushTagPrefix);
//		}
//		config.setBoolean(Constants.CONFIG_GITBLIT, null, "allowForks", repository.allowForks);
		config.setString(Constants.CONFIG_GITBLIT, null, "accessRestriction", repository.getAccessRestriction().name());
		config.setString(Constants.CONFIG_GITBLIT, null, "authorizationControl", repository.getAuthorizationControl().name());
//		config.setBoolean(Constants.CONFIG_GITBLIT, null, "verifyCommitter", repository.verifyCommitter);
//		config.setBoolean(Constants.CONFIG_GITBLIT, null, "showRemoteBranches", repository.showRemoteBranches);
//		config.setBoolean(Constants.CONFIG_GITBLIT, null, "isFrozen", repository.isFrozen);
//		config.setBoolean(Constants.CONFIG_GITBLIT, null, "skipSizeCalculation", repository.skipSizeCalculation);
//		config.setBoolean(Constants.CONFIG_GITBLIT, null, "skipSummaryMetrics", repository.skipSummaryMetrics);
//		config.setString(Constants.CONFIG_GITBLIT, null, "federationStrategy",
//				repository.federationStrategy.name());
//		config.setBoolean(Constants.CONFIG_GITBLIT, null, "isFederated", repository.isFederated);
//		config.setString(Constants.CONFIG_GITBLIT, null, "gcThreshold", repository.gcThreshold);
//		if (repository.gcPeriod == settings.getInteger(Keys.git.defaultGarbageCollectionPeriod, 7)) {
//			// use default from config
//			config.unset(Constants.CONFIG_GITBLIT, null, "gcPeriod");
//		} else {
//			config.setInt(Constants.CONFIG_GITBLIT, null, "gcPeriod", repository.gcPeriod);
//		}
//		if (repository.lastGC != null) {
//			config.setString(Constants.CONFIG_GITBLIT, null, "lastGC", new SimpleDateFormat(Constants.ISO8601).format(repository.lastGC));
//		}
		if (repository.getMaxActivityCommits() == settings.getInteger(Keys.web.maxActivityCommits, 0)) {
			// use default from config
			config.unset(Constants.CONFIG_GITBLIT, null, "maxActivityCommits");
		} else {
			config.setInt(Constants.CONFIG_GITBLIT, null, "maxActivityCommits", repository.getMaxActivityCommits());
		}

		CommitMessageRenderer defaultRenderer = CommitMessageRenderer.fromName(settings.getString(Keys.web.commitMessageRenderer, null));
//		if (repository.commitMessageRenderer == null || repository.commitMessageRenderer == defaultRenderer) {
//			// use default from config
//			config.unset(Constants.CONFIG_GITBLIT, null, "commitMessageRenderer");
//		} else {
//			// repository overrides default
//			config.setString(Constants.CONFIG_GITBLIT, null, "commitMessageRenderer",
//					repository.commitMessageRenderer.name());
//		}

//		updateList(config, "federationSets", repository.federationSets);
//		updateList(config, "preReceiveScript", repository.preReceiveScripts);
//		updateList(config, "postReceiveScript", repository.postReceiveScripts);
//		updateList(config, "mailingList", repository.mailingLists);
//		updateList(config, "indexBranch", repository.indexedBranches);
//		updateList(config, "metricAuthorExclusions", repository.metricAuthorExclusions);

		// User Defined Properties
//		if (repository.customFields != null) {
//			if (repository.customFields.size() == 0) {
//				// clear section
//				config.unsetSection(Constants.CONFIG_GITBLIT, Constants.CONFIG_CUSTOM_FIELDS);
//			} else {
//				for (Entry<String, String> property : repository.customFields.entrySet()) {
//					// set field
//					String key = property.getKey();
//					String value = property.getValue();
//					config.setString(Constants.CONFIG_GITBLIT, Constants.CONFIG_CUSTOM_FIELDS, key, value);
//				}
//			}
//		}

		try {
			config.save();
		} catch (IOException e) {
			logger.error("Failed to save repository config!", e);
		}
	}

	private void updateList(StoredConfig config, String field, List<String> list) {
		// a null list is skipped, not cleared
		// this is for RPC administration where an older manager might be used
		if (list == null) {
			return;
		}
		if (ArrayUtils.isEmpty(list)) {
			config.unset(Constants.CONFIG_GITBLIT, null, field);
		} else {
			config.setStringList(Constants.CONFIG_GITBLIT, null, field, list);
		}
	}

	/**
	 * 如果可以删除存储库, 则返回 true。
	 *
	 * @return true if the repository can be deleted
	 */
	@Override
	public boolean canDelete(TaskEntity repository) {
		return settings.getBoolean(Keys.web.allowDeletingNonEmptyRepositories, true)
					|| !repository.isHasCommits();
	}

	/**
	 * 从文件系统中删除存储库, 并从所有存储库用户中删除存储库权限。
	 *
	 * @param model
	 * @return true if successful
	 */
	@Override
	public boolean deleteRepositoryModel(TaskEntity model) {
		return deleteRepository(model.getTaskName());
	}

	/**
	 * 从文件系统中删除存储库, 并从所有存储库用户中删除存储库权限。
	 *
	 * @param repositoryName
	 * @return true if successful
	 */
	@Override
	public boolean deleteRepository(String repositoryName) {
		TaskEntity repository = getRepositoryModel(repositoryName);
		if (!canDelete(repository)) {
			logger.warn("Attempt to delete {} rejected!", repositoryName);
			return false;
		}

		try {
			close(repositoryName);
			// clear the repository cache
			clearRepositoryMetadataCache(repositoryName);

			TaskEntity model = removeFromCachedRepositoryList(repositoryName);
			if (model != null && !ArrayUtils.isEmpty(model.getForks())) {
				resetRepositoryListCache();
			}

			File folder = new File(repositoriesFolder, repositoryName);
			if (folder.exists() && folder.isDirectory()) {
				FileUtils.delete(folder, FileUtils.RECURSIVE | FileUtils.RETRY);
				if (userManager.deleteRepositoryRole(repositoryName)) {
					logger.info(MessageFormat.format("Repository \"{0}\" deleted", repositoryName));

					if (pluginManager != null) {
						for (RepositoryLifeCycleListener listener : pluginManager.getExtensions(RepositoryLifeCycleListener.class)) {
							try {
								listener.onDeletion(repository);
							} catch (Throwable t) {
								logger.error(String.format("failed to call plugin onDeletion %s", repositoryName), t);
							}
						}
					}
					return true;
				}
			}
		} catch (Throwable t) {
			logger.error(MessageFormat.format("Failed to delete repository {0}", repositoryName), t);
		}
		return false;
	}

	/**
	 * 返回所有 Groovy 推送挂钩脚本的列表。脚本文件必须具有. groovy 扩展名
	 *
	 * @return list of available hook scripts
	 */
	@Override
	public List<String> getAllScripts() {
		File groovyFolder = getHooksFolder();
		File[] files = groovyFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile() && pathname.getName().endsWith(".groovy");
			}
		});
		List<String> scripts = new ArrayList<String>();
		if (files != null) {
			for (File file : files) {
				String script = file.getName().substring(0, file.getName().lastIndexOf('.'));
				scripts.add(script);
			}
		}
		return scripts;
	}

	/**
	 * 返回存储库从全局设置和团队从属关系继承的预接收脚本列表。
	 *
	 * @param repository
	 *            if null only the globally specified scripts are returned
	 * @return a list of scripts
	 */
	@Override
	public List<String> getPreReceiveScriptsInherited(TaskEntity repository) {
		Set<String> scripts = new LinkedHashSet<String>();
		// Globals
		for (String script : settings.getStrings(Keys.groovy.preReceiveScripts)) {
			if (script.endsWith(".groovy")) {
				scripts.add(script.substring(0, script.lastIndexOf('.')));
			} else {
				scripts.add(script);
			}
		}

		// Team Scripts
		if (repository != null) {
			for (String teamname : userManager.getTeamNamesForRepositoryRole(repository.getTaskName())) {
				TeamModel team = userManager.getTeamModel(teamname);
				if (!ArrayUtils.isEmpty(team.preReceiveScripts)) {
					scripts.addAll(team.preReceiveScripts);
				}
			}
		}
		return new ArrayList<String>(scripts);
	}

	/**
	 * 返回存储库尚未继承的所有可用的 Groovy 预接收推挂钩脚本的列表。
	 * 脚本文件必须具有. groovy 扩展名
	 *
	 * @param repository
	 *            optional parameter
	 * @return list of available hook scripts
	 */
	@Override
	public List<String> getPreReceiveScriptsUnused(TaskEntity repository) {
		Set<String> inherited = new TreeSet<String>(getPreReceiveScriptsInherited(repository));

		// create list of available scripts by excluding inherited scripts
		List<String> scripts = new ArrayList<String>();
		for (String script : getAllScripts()) {
			if (!inherited.contains(script)) {
				scripts.add(script);
			}
		}
		return scripts;
	}

	/**
	 * 返回存储库从全局设置和团队从属关系继承的接收后脚本列表。
	 *
	 * @param repository
	 *            if null only the globally specified scripts are returned
	 * @return a list of scripts
	 */
	@Override
	public List<String> getPostReceiveScriptsInherited(TaskEntity repository) {
		Set<String> scripts = new LinkedHashSet<String>();
		// Global Scripts
		for (String script : settings.getStrings(Keys.groovy.postReceiveScripts)) {
			if (script.endsWith(".groovy")) {
				scripts.add(script.substring(0, script.lastIndexOf('.')));
			} else {
				scripts.add(script);
			}
		}
		// Team Scripts
		if (repository != null) {
			for (String teamname : userManager.getTeamNamesForRepositoryRole(repository.getTaskName())) {
				TeamModel team = userManager.getTeamModel(teamname);
				if (!ArrayUtils.isEmpty(team.postReceiveScripts)) {
					scripts.addAll(team.postReceiveScripts);
				}
			}
		}
		return new ArrayList<String>(scripts);
	}

	/**
	 * 返回尚未由存储库继承的未使用的常规后接收推挂钩脚本的列表。
	 * 脚本文件必须具有. groovy 扩展名
	 *
	 * @param repository
	 *            optional parameter
	 * @return list of available hook scripts
	 */
	@Override
	public List<String> getPostReceiveScriptsUnused(TaskEntity repository) {
		Set<String> inherited = new TreeSet<String>(getPostReceiveScriptsInherited(repository));

		// create list of available scripts by excluding inherited scripts
		List<String> scripts = new ArrayList<String>();
		for (String script : getAllScripts()) {
			if (!inherited.contains(script)) {
				scripts.add(script);
			}
		}
		return scripts;
	}

	/**
	 * 使用 Lucene 查询搜索指定的资料库。
	 *
	 * @param query
	 * @param page
	 * @param pageSize
	 * @param repositories
	 * @return
	 */
	@Override
	public List<SearchResult> search(String query, int page, int pageSize, List<String> repositories) {
		List<SearchResult> srs = luceneExecutor.search(query, page, pageSize, repositories);
		return srs;
	}

	protected void configureLuceneIndexing() {
		luceneExecutor = new LuceneService(settings, this);
		String frequency = settings.getString(Keys.web.luceneFrequency, "2 mins");
		int mins = TimeUtils.convertFrequencyToMinutes(frequency, 2);
		scheduledExecutor.scheduleAtFixedRate(luceneExecutor, 1, mins,  TimeUnit.MINUTES);
		logger.info("Lucene will process indexed branches every {} minutes.", mins);
	}

	protected void configureGarbageCollector() {
		// schedule gc engine
		gcExecutor = new GarbageCollectorService(settings, this);
		if (gcExecutor.isReady()) {
			logger.info("Garbage Collector (GC) will scan repositories every 24 hours.");
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, settings.getInteger(Keys.git.garbageCollectionHour, 0));
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			Date cd = c.getTime();
			Date now = new Date();
			int delay = 0;
			if (cd.before(now)) {
				c.add(Calendar.DATE, 1);
				cd = c.getTime();
			}
			delay = (int) ((cd.getTime() - now.getTime())/TimeUtils.MIN);
			String when = delay + " mins";
			if (delay > 60) {
				when = MessageFormat.format("{0,number,0.0} hours", delay / 60f);
			}
			logger.info(MessageFormat.format("Next scheculed GC scan is in {0}", when));
			scheduledExecutor.scheduleAtFixedRate(gcExecutor, delay, 60 * 24, TimeUnit.MINUTES);
		} else {
			logger.info("Garbage Collector (GC) is disabled.");
		}
	}

	protected void configureMirrorExecutor() {
		mirrorExecutor = new MirrorService(settings, this);
		if (mirrorExecutor.isReady()) {
			int mins = TimeUtils.convertFrequencyToMinutes(settings.getString(Keys.git.mirrorPeriod, "30 mins"), 5);
			int delay = 1;
			scheduledExecutor.scheduleAtFixedRate(mirrorExecutor, delay, mins,  TimeUnit.MINUTES);
			logger.info("Mirror service will fetch updates every {} minutes.", mins);
			logger.info("Next scheduled mirror fetch is in {} minutes", delay);
		} else {
			logger.info("Mirror service is disabled.");
		}
	}

	protected void configureJGit() {
		// Configure JGit
		WindowCacheConfig cfg = new WindowCacheConfig();

		cfg.setPackedGitWindowSize(settings.getFilesize(Keys.git.packedGitWindowSize, cfg.getPackedGitWindowSize()));
		cfg.setPackedGitLimit(settings.getFilesize(Keys.git.packedGitLimit, cfg.getPackedGitLimit()));
		cfg.setDeltaBaseCacheLimit(settings.getFilesize(Keys.git.deltaBaseCacheLimit, cfg.getDeltaBaseCacheLimit()));
		cfg.setPackedGitOpenFiles(settings.getInteger(Keys.git.packedGitOpenFiles, cfg.getPackedGitOpenFiles()));
		cfg.setPackedGitMMAP(settings.getBoolean(Keys.git.packedGitMmap, cfg.isPackedGitMMAP()));

		try {
			cfg.install();
			logger.debug(MessageFormat.format("{0} = {1,number,0}", Keys.git.packedGitWindowSize, cfg.getPackedGitWindowSize()));
			logger.debug(MessageFormat.format("{0} = {1,number,0}", Keys.git.packedGitLimit, cfg.getPackedGitLimit()));
			logger.debug(MessageFormat.format("{0} = {1,number,0}", Keys.git.deltaBaseCacheLimit, cfg.getDeltaBaseCacheLimit()));
			logger.debug(MessageFormat.format("{0} = {1,number,0}", Keys.git.packedGitOpenFiles, cfg.getPackedGitOpenFiles()));
			logger.debug(MessageFormat.format("{0} = {1}", Keys.git.packedGitMmap, cfg.isPackedGitMMAP()));
		} catch (IllegalArgumentException e) {
			logger.error("Failed to configure JGit parameters!", e);
		}

		try {
			// issue-486/ticket-151: UTF-9 & UTF-18
			// issue-560/ticket-237: 'UTF8'
			Field field = RawParseUtils.class.getDeclaredField("encodingAliases");
			field.setAccessible(true);
			Map<String, Charset> encodingAliases = (Map<String, Charset>) field.get(null);
			encodingAliases.put("'utf8'", RawParseUtils.UTF8_CHARSET);
			encodingAliases.put("utf-9", RawParseUtils.UTF8_CHARSET);
			encodingAliases.put("utf-18", RawParseUtils.UTF8_CHARSET);
			logger.info("Alias 'UTF8', UTF-9 & UTF-18 encodings as UTF-8 in JGit");
		} catch (Throwable t) {
			logger.error("Failed to inject UTF-9 & UTF-18 encoding aliases into JGit", t);
		}
	}

	protected void configureCommitCache() {
		final int daysToCache = settings.getInteger(Keys.web.activityCacheDays, 14);
		if (daysToCache <= 0) {
			logger.info("Commit cache is disabled");
			return;
		}
		logger.info(MessageFormat.format("Preparing {0} day commit cache...", daysToCache));
		CommitCache.instance().setCacheDays(daysToCache);
		Thread loader = new Thread() {
			@Override
			public void run() {
				long start = System.nanoTime();
				long repoCount = 0;
				long commitCount = 0;
				Date cutoff = CommitCache.instance().getCutoffDate();
				for (String repositoryName : getRepositoryList()) {
					TaskEntity model = getRepositoryModel(repositoryName);
					if (model != null && model.isHasCommits() && model.getUpdTime().after(cutoff)) {
						repoCount++;
						Repository repository = getRepository(repositoryName);
						for (RefModel ref : JGitUtils.getLocalBranches(repository, true, -1)) {
							if (!ref.getDate().after(cutoff)) {
								// branch not recently updated
								continue;
							}
							List<?> commits = CommitCache.instance().getCommits(repositoryName, repository, ref.getName());
							if (commits.size() > 0) {
								logger.info(MessageFormat.format("  cached {0} commits for {1}:{2}",
										commits.size(), repositoryName, ref.getName()));
								commitCount += commits.size();
							}
						}
						repository.close();
					}
				}
				logger.info(MessageFormat.format("built {0} day commit cache of {1} commits across {2} repositories in {3} msecs",
						daysToCache, commitCount, repoCount, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start)));
			}
		};
		loader.setName("CommitCacheLoader");
		loader.setDaemon(true);
		loader.start();
	}

	protected void confirmWriteAccess() {
		try {
			if (!getRepositoriesFolder().exists()) {
				getRepositoriesFolder().mkdirs();
			}
			File file = File.createTempFile(".test-", ".txt", getRepositoriesFolder());
			file.delete();
		} catch (Exception e) {
			logger.error("");
			logger.error(Constants.BORDER2);
			logger.error("Please check filesystem permissions!");
			logger.error("FAILED TO WRITE TO REPOSITORIES FOLDER!!", e);
			logger.error(Constants.BORDER2);
			logger.error("");
		}
	}
}
