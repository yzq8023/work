/*
 * Copyright 2011 gitblit.com.
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
package com.service.service;

import com.github.wxiaoqi.security.api.vo.user.UserInfo;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.context.BaseContextHandler;
import com.service.service.Constants.AccessPermission;
import com.service.service.Constants.AccountType;
import com.service.service.Constants.Role;
import com.service.service.Constants.Transport;
import com.service.service.biz.MapUserTaskBiz;
import com.service.service.biz.TaskBiz;
import com.service.service.biz.TeamBiz;
import com.service.service.entity.*;
import com.service.service.feign.IUserFeignClient;
import com.service.service.managers.IRuntimeManager;
import com.service.service.mapper.MapUserTaskMapper;
import com.service.service.utils.ArrayUtils;
import com.service.service.utils.DeepCopier;
import com.service.service.utils.StringUtils;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.service.service.Constants.AccountType.LOCAL;

/**
 *
 *
 * 用户和他们的存储库成员被存储在一个配置文件中，
 * 该文件在修改时被缓存并动态重新加载。
 * 这个文件是纯文本的，是可读的，
 * 并且可以用文本编辑器编辑。
 *
 * 此外，这种格式允许在不引入数据库复杂性的情况下扩展用户模型。
 *
 * @author James Moger
 *
 */
@Service
public class ConfigUserService extends BaseBiz<MapUserTaskMapper, MapUserTask> implements IUserService {

	private static final String TEAM = "team";

	private static final String USER = "user";

	private static final String PASSWORD = "password";

	private static final String DISPLAYNAME = "displayName";

	private static final String EMAILADDRESS = "emailAddress";

	private static final String ORGANIZATIONALUNIT = "organizationalUnit";

	private static final String ORGANIZATION = "organization";

	private static final String LOCALITY = "locality";

	private static final String STATEPROVINCE = "stateProvince";

	private static final String COUNTRYCODE = "countryCode";

	private static final String COOKIE = "cookie";

	private static final String REPOSITORY = "repository";

	private static final String ROLE = "role";

	private static final String MAILINGLIST = "mailingList";

	private static final String PRERECEIVE = "preReceiveScript";

	private static final String POSTRECEIVE = "postReceiveScript";

	private static final String STARRED = "starred";

	private static final String LOCALE = "locale";

	private static final String EMAILONMYTICKETCHANGES = "emailMeOnMyTicketChanges";

	private static final String TRANSPORT = "transport";

	private static final String ACCOUNTTYPE = "accountType";

	private static final String DISABLED = "disabled";



	private final Logger logger = LoggerFactory.getLogger(ConfigUserService.class);

	private final Map<String, UserModel> users = new ConcurrentHashMap<String, UserModel>();

	private final Map<String, UserModel> cookies = new ConcurrentHashMap<String, UserModel>();

	private Map<String, TeamModel> teams = new ConcurrentHashMap<String, TeamModel>();

	private volatile long lastModified;

	private volatile boolean forceReload;

	private IUserFeignClient feignClient;

	private TeamBiz teamBiz;

	private TaskBiz taskBiz;

	private MapUserTaskBiz mapUserTaskBiz;
	@Autowired
	public ConfigUserService(IUserFeignClient feignClient,
							 TeamBiz teamBiz,
							 TaskBiz taskBiz,
							 MapUserTaskBiz mapUserTaskBiz) {
		this.feignClient = feignClient;
		this.teamBiz = teamBiz;
		this.taskBiz = taskBiz;
		this.mapUserTaskBiz = mapUserTaskBiz;
	}

	public ConfigUserService() {
	}
	/**
	 * 设置用户服务。
	 *
	 * @param runtimeManager
	 * @since 1.4.0
	 */
	@Override
	public void setup(IRuntimeManager runtimeManager) {
	}

	/**
	 * 返回指定用户的cookie值。
	 *
	 * @param model
	 * @return cookie value
	 */
	@Override
	public synchronized String getCookie(UserModel model) {
		if (!StringUtils.isEmpty(model.getCookie())) {
			return model.getCookie();
		}
		UserModel storedModel = getUserModel(model.getUserId());
		if (storedModel == null) {
			return null;
		}
		return storedModel.getCookie();
	}

	/**
	 * 获取指定cookie的用户对象。
	 *
	 * @param cookie
	 * @return a user object or null
	 */
	@Override
	public synchronized UserModel getUserModel(char[] cookie) {
		String hash = new String(cookie);
		if (StringUtils.isEmpty(hash)) {
			return null;
		}
		read();
		UserModel model = null;
		if (cookies.containsKey(hash)) {
			model = cookies.get(hash);
		}

		if (model != null) {
			// clone the model, otherwise all changes to this object are
			// live and unpersisted
			model = DeepCopier.copy(model);
		}
		return model;
	}

	/**
	 * 检索指定用户名的用户对象。
	 *
	 * @param username
	 * @return a user object or null
	 */
	@Override
	public synchronized UserModel getUserModel(String username) {
		read();
		UserModel model = users.get(username.toLowerCase());
		if (model != null) {
			// clone the model, otherwise all changes to this object are
			// live and unpersisted
			model = DeepCopier.copy(model);
		}
		return model;
	}

	/**
	 * 为指定的userId检索user对象。
	 *
	 * @param userId
	 * @return a user object or null
	 */
	@Override
	public synchronized UserModel getUserModel(Integer userId) {
		read();
		UserModel model = users.get(userId);
		if (model != null) {
			// clone the model, otherwise all changes to this object are
			// live and unpersisted
			model = DeepCopier.copy(model);
		}
		return model;
	}

	/**
	 * 更新/写一个完整的用户对象。
	 *
	 * @param model
	 * @return true if update is successful
	 */
	@Override
	public synchronized boolean updateUserModel(UserModel model) {
		return updateUserModel(model.getUserId(), model);
	}

	/**
	 * 更新/写入所有指定的用户对象。
	 *
	 * @param models a list of user models
	 * @return true if update is successful
	 * @since 1.2.0
	 */
	@Override
	public synchronized boolean updateUserModels(Collection<UserModel> models) {
		try {
			read();
			for (UserModel model : models) {
				UserModel originalUser = users.remove(model.getUserId().toLowerCase());
				users.put(model.getUserId().toLowerCase(), model);
				// null check on "final" teams because JSON-sourced UserModel
				// can have a null teams object
				if (model.getTeams() != null) {
					Set<TeamModel> userTeams = new HashSet<TeamModel>();
					for (TeamModel team : model.getTeams()) {
						TeamModel t = teams.get(team.getId());
						if (t == null) {
							// new team
							t = team;
							teams.put(String.valueOf(team.getId()), t);
						}
						// do not clobber existing team definition
						// maybe because this is a federated user
						t.addUser(model.getUserId());
						userTeams.add(t);
					}
					// replace Team-Models in users by new ones.
					model.getTeams().clear();
					model.getTeams().addAll(userTeams);

					// check for implicit team removal
					if (originalUser != null) {
						for (TeamModel team : originalUser.getTeams()) {
							if (!model.isTeamMember(String.valueOf(team.getId()))) {
								team.removeUser(model.getUserId());
							}
						}
					}
				}
			}
			return true;
		} catch (Throwable t) {
			logger.error(MessageFormat.format("Failed to update user {0} models!", models.size()),
					t);
		}
		return false;
	}

	/**
	 * 更新/写入并替换由用户名键入的完整用户对象。这种方法允许重命名用户。
	 *
	 * @param userId
	 *            the old username
	 * @param model
	 *            the user object to use for username
	 * @return true if update is successful
	 */
	@Override
	public synchronized boolean updateUserModel(String userId, UserModel model) {
		UserModel originalUser = null;
		try {
			if (!model.isLocalAccount()) {
				// 不保存密码
				model.setPassword(Constants.EXTERNAL_ACCOUNT);
			}
			read();
			originalUser = users.remove(userId);

			if (originalUser != null) {
				//在缓存中删除相应用户
				cookies.remove(originalUser.getCookie());
			}
			users.put(model.getUserId(), model);
			// null check on "final" teams because JSON-sourced UserModel
			// can have a null teams object
			if (model.getTeams() != null) {
				for (TeamModel team : model.getTeams()) {
					TeamModel t = teams.get(team.getId());
					if (t == null) {
						// new team
						team.addUser(userId);
						teams.put(String.valueOf(team.getId()), team);
					} else {
						// do not clobber existing team definition
						// maybe because this is a federated user
						t.removeUser(userId);
						t.addUser(model.getUserId());
					}
				}

				// check for implicit team removal
				if (originalUser != null) {
					for (TeamModel team : originalUser.getTeams()) {
						if (!model.isTeamMember(String.valueOf(team.getId()))) {
							team.removeUser(userId);
						}
					}
				}
			}
			return true;
		} catch (Throwable t) {
			if (originalUser != null) {
				// restore original user
				users.put(originalUser.getUserId().toLowerCase(), originalUser);
			} else {
				// drop attempted add
				users.remove(model.getUserId().toLowerCase());
			}
			logger.error(MessageFormat.format("Failed to update user model {0}!", model.getUserId()),
					t);
		}
		return false;
	}

	/**
	 * 从用户服务中删除用户对象。
	 *
	 * @param model
	 * @return true if successful
	 */
	@Override
	public synchronized boolean deleteUserModel(UserModel model) {
		return deleteUser(model.getUserId());
	}

	/**
	 * 使用指定的用户名删除用户对象
	 *
	 * @param username
	 * @return true if successful
	 */
	@Override
	public synchronized boolean deleteUser(String username) {
		try {
			// Read realm file
			read();
			UserModel model = users.remove(username.toLowerCase());
			if (model == null) {
				// user does not exist
				return false;
			}
			// remove user from team
			for (TeamModel team : model.getTeams()) {
				TeamModel t = teams.get(team.getId());
				if (t == null) {
					// new team
					team.removeUser(username);
					teams.put(String.valueOf(team.getId()), team);
				} else {
					// existing team
					t.removeUser(username);
				}
			}
			return true;
		} catch (Throwable t) {
			logger.error(MessageFormat.format("Failed to delete user {0}!", username), t);
		}
		return false;
	}

	/**
	 * 返回登录服务可用的所有团队的列表。
	 *
	 * @return list of all teams
	 * @since 0.8.0
	 */
	@Override
	public synchronized List<String> getAllTeamNames() {
		read();
		List<String> list = new ArrayList<String>(teams.keySet());
		Collections.sort(list);
		return list;
	}

	/**
	 * 返回登录服务可用的所有团队的列表。
	 *
	 * @return list of all teams
	 * @since 0.8.0
	 */
	@Override
	public synchronized List<TeamModel> getAllTeams() {
		read();
		List<TeamModel> list = new ArrayList<TeamModel>(teams.values());
		list = DeepCopier.copy(list);
		Collections.sort(list);
		return list;
	}

	/**
	 * 返回所有允许绕过指定存储库上的访问限制的用户的列表。
	 *
	 * @param role
	 *            the repository name
	 * @return list of all usernames that can bypass the access restriction
	 */
	@Override
	public synchronized List<String> getTeamNamesForRepositoryRole(String role) {
		List<String> list = new ArrayList<String>();
		try {
			read();
			for (Map.Entry<String, TeamModel> entry : teams.entrySet()) {
				TeamModel model = entry.getValue();
				if (model.hasRepositoryPermission(role)) {
					list.add(String.valueOf(model.getId()));
				}
			}
		} catch (Throwable t) {
			logger.error(MessageFormat.format("Failed to get teamnames for role {0}!", role), t);
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * 检索团队对象以获得指定的团队名称。
	 *
	 * @param teamname
	 * @return a team object or null
	 * @since 0.8.0
	 */
	@Override
	public synchronized TeamModel getTeamModel(String teamname) {
		read();
		TeamModel model = teams.get(teamname.toLowerCase());
		if (model != null) {
			// clone the model, otherwise all changes to this object are
			// live and unpersisted
			model = DeepCopier.copy(model);
		}
		return model;
	}

	/**
	 * 更新/写一个完整的团队对象。
	 *
	 * @param model
	 * @return true if update is successful
	 * @since 0.8.0
	 */
	@Override
	public synchronized boolean updateTeamModel(TeamModel model) {
		return updateTeamModel(String.valueOf(model.getId()), model);
	}

	/**
	 * Updates/writes all specified team objects.
	 *
	 * @param models a list of team models
	 * @return true if update is successful
	 * @since 1.2.0
	 */
	@Override
	public synchronized boolean updateTeamModels(Collection<TeamModel> models) {
		try {
			read();
			for (TeamModel team : models) {
				teams.put(String.valueOf(team.getId()), team);
			}
			return true;
		} catch (Throwable t) {
			logger.error(MessageFormat.format("Failed to update team {0} models!", models.size()), t);
		}
		return false;
	}

	/**
	 * Updates/writes and replaces a complete team object keyed by teamname.
	 * This method allows for renaming a team.
	 *
	 * @param teamname
	 *            the old teamname
	 * @param model
	 *            the team object to use for teamname
	 * @return true if update is successful
	 * @since 0.8.0
	 */
	@Override
	public synchronized boolean updateTeamModel(String teamname, TeamModel model) {
		TeamModel original = null;
		try {
			read();
			original = teams.remove(teamname.toLowerCase());
			teams.put(String.valueOf(model.getId()), model);
			return true;
		} catch (Throwable t) {
			if (original != null) {
				// restore original team
				teams.put(String.valueOf(original.getId()), original);
			} else {
				// drop attempted add
				teams.remove(model.getId());
			}
			logger.error(MessageFormat.format("Failed to update team model {0}!", model.getId()), t);
		}
		return false;
	}

	/**
	 * Deletes the team object from the user service.
	 *
	 * @param model
	 * @return true if successful
	 * @since 0.8.0
	 */
	@Override
	public synchronized boolean deleteTeamModel(TeamModel model) {
		return deleteTeam(String.valueOf(model.getId()));
	}

	/**
	 * Delete the team object with the specified teamname
	 *
	 * @param teamname
	 * @return true if successful
	 * @since 0.8.0
	 */
	@Override
	public synchronized boolean deleteTeam(String teamname) {
		try {
			// Read realm file
			read();
			teams.remove(teamname.toLowerCase());
			return true;
		} catch (Throwable t) {
			logger.error(MessageFormat.format("Failed to delete team {0}!", teamname), t);
		}
		return false;
	}

	/**
	 * Returns the list of all users available to the login service.
	 *
	 * @return list of all usernames
	 */
	@Override
	public synchronized List<String> getAllUsernames() {
		read();
		List<String> list = new ArrayList<String>(users.keySet());
		Collections.sort(list);
		return list;
	}

	/**
	 * 返回登录服务可用的所有用户列表。
	 *
	 * @return list of all usernames
	 */
	@Override
	public synchronized List<UserModel> getAllUsers() {
		read();
		List<UserModel> list = new ArrayList<UserModel>(users.values());
		list = DeepCopier.copy(list);
		Collections.sort(list);
		return list;
	}

	/**
	 * 返回所有允许绕过指定存储库上的访问限制的用户的列表。
	 *
	 * @param role
	 *            the repository name
	 * @return list of all usernames that can bypass the access restriction
	 */
	@Override
	public synchronized List<String> getUsernamesForRepositoryRole(String role) {
		List<String> list = new ArrayList<String>();
		try {
			read();
			for (Map.Entry<String, UserModel> entry : users.entrySet()) {
				UserModel model = entry.getValue();
				if (model.hasRepositoryPermission(role)) {
					list.add(model.getUserId());
				}
			}
		} catch (Throwable t) {
			logger.error(MessageFormat.format("Failed to get usernames for role {0}!", role), t);
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * 重命名一个存储库的作用。
	 * @param oldRole
	 * @param newRole
	 * @return true if successful
	 */
	@Override
	public synchronized boolean renameRepositoryRole(String oldRole, String newRole) {
		try {
			read();
			// identify users which require role rename
			for (UserModel model : users.values()) {
				if (model.hasRepositoryPermission(oldRole)) {
					AccessPermission permission = model.removeRepositoryPermission(oldRole);
					model.setRepositoryPermission(newRole, permission);
				}
			}

			// identify teams which require role rename
			for (TeamModel model : teams.values()) {
				if (model.hasRepositoryPermission(oldRole)) {
					AccessPermission permission = model.removeRepositoryPermission(oldRole);
					model.setRepositoryPermission(newRole, permission);
				}
			}
			return true;
		} catch (Throwable t) {
			logger.error(
					MessageFormat.format("Failed to rename role {0} to {1}!", oldRole, newRole), t);
		}
		return false;
	}

	/**
	 * 从所有用户中删除存储库角色。
	 *
	 * @param role
	 * @return true if successful
	 */
	@Override
	public synchronized boolean deleteRepositoryRole(String role) {
		try {
			read();

			// identify users which require role rename
			for (UserModel user : users.values()) {
				user.removeRepositoryPermission(role);
			}

			// identify teams which require role rename
			for (TeamModel team : teams.values()) {
				team.removeRepositoryPermission(role);
			}
			return true;
		} catch (Throwable t) {
			logger.error(MessageFormat.format("Failed to delete role {0}!", role), t);
		}
		return false;
	}

	/**
	 * 读取ace-admin服务中的用户并重新构建内存中的查找表。
	 */
	protected synchronized void read() {
		if (forceReload) {
			forceReload = false;
			users.clear();
			cookies.clear();
			teams.clear();

			try {
				//必须保证Admin服务优先启动
				List<UserInfo> userInfos = feignClient.all();
				for (UserInfo userInfo : userInfos) {
					Example example1 = new Example(TaskEntity.class);
					example1.createCriteria().andEqualTo("crt_user", userInfo.getId());
					List<TaskEntity> taskEntities = taskBiz.selectByExample(example1);

					Example example2 = new Example(TaskEntity.class);
					example2.createCriteria().andEqualTo("user_id", userInfo.getId());
					List<MapUserTask> mapUserTasks = mapUserTaskBiz.selectByExample(example2);

					UserModel user = new UserModel(userInfo.getId());
					user.setPassword(userInfo.getPassword());
					user.setName(userInfo.getName());
					user.setEmailAddress("hollykunge@163.com");
					user.setAccountType(LOCAL);
					user.setDisabled(false);
					user.setOrganizationalUnit(null);
					user.setOrganization(null);
					user.setLocality(null);
					user.setStateProvince(null);
					user.setCountryCode(null);
					user.setCookie(null);
					if (StringUtils.isEmpty(user.getCookie()) && !StringUtils.isEmpty(user.getPassword())) {
						user.setCookie(user.createCookie());
					}

					// preferences
					user.getPreferences().setLocale(userInfo.getId());
					user.getPreferences().setTransport(Transport.fromString(userInfo.getId()));

					user.setCanAdmin(userInfo.isCanAdmin());
					user.setCanFork(userInfo.isCanFork());
					user.setCanCreate(userInfo.isCanCreate());

					user.setJoinedReps(mapUserTasks);
					user.setCreateReps(taskEntities);

					// repository memberships
					if (!user.isCanAdmin()) {
						// 非admin, 直接读取数据库获取用户仓库
						Set<String> repositories = new HashSet<String>(mapper.getUserIds());
						for (String repository : repositories) {
							user.addRepositoryPermission(repository);
						}
					}

					// update cache
					users.put(user.getUserId(), user);
					if (!StringUtils.isEmpty(user.getCookie())) {
						cookies.put(user.getCookie(), user);
					}
				}

				this.teams = teamBiz.loadTeams();
			} catch (Exception e) {
				logger.error(MessageFormat.format("Failed to load {0}", users), e);
			}
		}
	}

	protected long lastModified() {
		return lastModified;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "()";
	}

	@Override
	protected String getPageName() {
		return null;
	}
}
