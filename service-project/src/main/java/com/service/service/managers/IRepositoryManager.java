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

import com.service.service.entity.*;
import com.service.service.exception.GitBlitException;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author workhub
 */

public interface IRepositoryManager extends IManager {

	/**
	 * 返回所有仓库所在文件夹路径. This method checks to see if
	 * Gitblit is running on a cloud service and may return an adjusted path.
	 *
	 * @return 所有仓库的跟路径
 	 * @since 1.4.0
	 */
	File getRepositoriesFolder();

	/**
	 * 返回所有钩子所在文件夹路径. This method checks to see if
	 * Gitblit is running on a cloud service and may return an adjusted path.
	 *
	 * @return Groovy钩子的脚本文件夹路径。
 	 * @since 1.4.0
	 */
	File getHooksFolder();

	/**
	 * 返回grapes所在文件夹路径. This method checks to see if
	 * Gitblit is running on a cloud service and may return an adjusted path.
	 *
	 * @return the Groovy grapes folder path
 	 * @since 1.4.0
	 */
	File getGrapesFolder();

	/**
	 * 返回仓库最后的活动日期.
	 *
	 * @return a date
 	 * @since 1.4.0
	 */
	Date getLastActivityDate();

	/**
	 * 根据团队成员的角色和权限，返回当前用户的权限列表
	 *
	 * @param user a
	 * @return 返回当前用户有效的权限列表
 	 * @since 1.4.0
	 */
	List<RegistrantAccessPermission> getUserAccessPermissions(UserModel user);

	/**
	 * 返回指定的用户及其访问权限列表
	 *
	 * @param repository a
	 * @return a list of RegistrantAccessPermissions
 	 * @since 1.4.0
	 */
	List<RegistrantAccessPermission> getUserAccessPermissions(TaskEntity repository);

	/**
	 * 为指定的用户设置指定仓库的访问权限
	 *
	 * @param repository a
	 * @param permissions a
	 * @return true if the user models have been updated
 	 * @since 1.4.0
	 */
	boolean setUserAccessPermissions(TaskEntity repository, Collection<RegistrantAccessPermission> permissions);

	/**
	 * 根据指定仓库返回具有显式访问权限的所有用户的列表.
	 *
	 * @param repository a
	 * @return list of all usernames that have an access permission for the repository
 	 * @since 1.4.0
	 */
	List<String> getRepositoryUsers(TaskEntity repository);

	/**
	 * 返回指定的团队及其访问权限列表
	 * 包含权限源的存储库，如管理标志
	 * 或正则表达式。
	 *
	 * @param repository a
	 * @return a list of RegistrantAccessPermissions
 	 * @since 1.4.0
	 */
	List<RegistrantAccessPermission> getTeamAccessPermissions(TaskEntity repository);

	/**
	 * 为指定的团队设置指定存储库的访问权限。
	 *
	 * @param repository a
	 * @param permissions a
	 * @return true if the team models have been updated
 	 * @since 1.4.0
	 */
	boolean setTeamAccessPermissions(TaskEntity repository, Collection<RegistrantAccessPermission> permissions);

	/**
	 * 返回有明确访问权限的所有团队的列表
	 * 指定的存储库。
	 *
	 * @param repository a
	 * @return list of all teamnames with explicit access permissions to the repository
 	 * @since 1.4.0
	 */
	List<String> getRepositoryTeams(TaskEntity repository);

	/**
	 * 如果Gitblit是配置为缓存存储库列表，则将存储库添加到缓存存储库列表。
	 *
	 * @param model a
 	 * @since 1.4.0
	 */
	void addToCachedRepositoryList(TaskEntity model);

	/**
	 * 重置仓库缓存列表
	 *
 	 * @since 1.4.0
	 *
	 */
	void resetRepositoryListCache();

	/**
	 * 重置当前仓库的所有缓存.
	 *
	 * @param repositoryName a
	 * @since 1.5.1
	 */
	void resetRepositoryCache(String repositoryName);

	/**
	 * 返回所有可用仓库的列表.
	 *
	 *
	 * @return list of all repositories
 	 * @since 1.4.0
	 */
	List<String> getRepositoryList();

	/**
	 * 根据名称返回指定的仓库
	 *
	 * @param repositoryName a
	 * @return repository or null
 	 * @since 1.4.0
	 */
	Repository getRepository(String repositoryName);

	/**
	 * 根据名称返回指定的仓库
	 *
	 * @param repositoryName a
	 * @param logError a
	 * @return repository or null
 	 * @since 1.4.0
	 */
	Repository getRepository(String repositoryName, boolean logError);

	/**
	 * Returns the list of all repository models.
	 *
	 * @return list of all repository models
 	 * @since 1.6.1
	 */
	List<TaskEntity> getRepositoryModels();

	/**
	 * Returns the list of repository models that are accessible to the user.
	 *
	 * @param user a
	 * @return list of repository models accessible to user
 	 * @since 1.4.0
	 */
	List<TaskEntity> getRepositoryModels(UserModel user);

	/**
	 * Returns a repository model if the repository exists and the user may
	 * access the repository.
	 *
	 * @param user a
	 * @param repositoryName a
	 * @return repository model or null
 	 * @since 1.4.0
	 */
	TaskEntity getRepositoryModel(UserModel user, String repositoryName);

	/**
	 * Returns the repository model for the specified repository. This method
	 * does not consider user access permissions.
	 *
	 * @param repositoryName a
	 * @return repository model or null
 	 * @since 1.4.0
	 */
	TaskEntity getRepositoryModel(String repositoryName);

	/**
	 * 返回标星仓库
	 *
	 * @param repository a
	 * @return the star count
 	 * @since 1.4.0
	 */
//	long getStarCount(TaskEntity repository);

	/**
	 * 确定此服务器是否具有所请求的存储库。
	 *
	 * @param repositoryName a
	 * @return true if the repository exists
 	 * @since 1.4.0
	 */
	boolean hasRepository(String repositoryName);

	/**
	 * 确定此服务器是否具有所请求的存储库。
	 *
	 * @param repositoryName a
	 * @param caseSensitiveCheck a
	 * @return true if the repository exists
 	 * @since 1.4.0
	 */
	boolean hasRepository(String repositoryName, boolean caseSensitiveCheck);

	/**
	 * 确定用户是否fork了该仓库
	 *
	 * @param username a
	 * @param origin a
	 * @return true the if the user has a fork
 	 * @since 1.4.0
	 */
	boolean hasFork(String username, String origin);

	/**
	 * 获取指定原点的用户分支的名称
	 * 库。
	 *
	 * @param username a
	 * @param origin a
	 * @return the name of the user's fork, null otherwise
 	 * @since 1.4.0
	 */
	String getFork(String username, String origin);

	/**
	 * 通过遍历叉图返回存储库的分支网络
	 * 通过根节点的所有子节点发现根然后向下。
	 *
	 * @param repository a
	 * @return a ForkModel
 	 * @since 1.4.0
	 */
	ForkModel getForkNetwork(String repository);

	/**
	 * 更新仓库计算的大小
	 * Gitblit caches the repository sizes to reduce the performance
	 * penalty of recursive calculation. The cache is updated if the repository
	 * has been changed since the last calculation.
	 *
	 * @param model a
	 * @return size in bytes of the repository
 	 * @since 1.4.0
	 */
	long updateLastChangeFields(Repository r, TaskEntity model);

	/**
	 * 返回指定存储库的默认分支的度量。
	 * This method builds a metrics cache. The cache is updated if the
	 * repository is updated. A new copy of the metrics list is returned on each
	 * call so that modifications to the list are non-destructive.
	 *
	 * @param model a
	 * @param repository a
	 * @return a new array list of metrics
 	 * @since 1.4.0
	 */
	List<Metric> getRepositoryDefaultMetrics(TaskEntity model, Repository repository);

	/**
	 * 创建/更新由仓库名称键入的存储库模型。 Saves all
	 * repository settings in .git/config. This method allows for renaming
	 * repositories and will update user access permissions accordingly.
	 *
	 * All repositories created by this method are bare and automatically have
	 * .git appended to their names, which is the standard convention for bare
	 * repositories.
	 *
	 * @param repositoryName a
	 * @param repository a
	 * @param isCreate a
	 * @throws GitBlitException e
 	 * @since 1.4.0
	 */
	void updateRepositoryModel(String repositoryName, TaskEntity repository, boolean isCreate)
			throws GitBlitException;

	/**
	 * 更新指定存储库的GITBLIT配置。
	 *
	 * @param r
	 *            the Git repository
	 * @param repository
	 *            the Gitblit repository model
 	 * @since 1.4.0
	 */
	void updateConfiguration(Repository r, TaskEntity repository);

	/**
	 * 如果可以删除存储库，则返回true。
	 *
	 * @param model a
	 * @return true if the repository can be deleted
	 * @since 1.6.0
	 */
	boolean canDelete(TaskEntity model);

	/**
	 * Deletes the repository from the file system and removes the repository
	 * permission from all repository users.
	 *
	 * @param model a
	 * @return true if successful
 	 * @since 1.4.0
	 */
	boolean deleteRepositoryModel(TaskEntity model);

	/**
	 * Deletes the repository from the file system and removes the repository
	 * permission from all repository users.
	 *
	 * @param repositoryName a
	 * @return true if successful
 	 * @since 1.4.0
	 */
	boolean deleteRepository(String repositoryName);

	/**
	 * Returns the list of all Groovy push hook scripts. Script files must have
	 * .groovy extension
	 *
	 * @return list of available hook scripts
 	 * @since 1.4.0
	 */
	List<String> getAllScripts();

	/**
	 * Returns the list of pre-receive scripts the repository inherited from the
	 * global settings and team affiliations.
	 *
	 * @param repository
	 *            if null only the globally specified scripts are returned
	 * @return a list of scripts
 	 * @since 1.4.0
	 */
	List<String> getPreReceiveScriptsInherited(TaskEntity repository);

	/**
	 * Returns the list of all available Groovy pre-receive push hook scripts
	 * that are not already inherited by the repository. Script files must have
	 * .groovy extension
	 *
	 * @param repository
	 *            optional parameter
	 * @return list of available hook scripts
 	 * @since 1.4.0
	 */
	List<String> getPreReceiveScriptsUnused(TaskEntity repository);

	/**
	 * Returns the list of post-receive scripts the repository inherited from
	 * the global settings and team affiliations.
	 *
	 * @param repository
	 *            if null only the globally specified scripts are returned
	 * @return a list of scripts
 	 * @since 1.4.0
	 */
	List<String> getPostReceiveScriptsInherited(TaskEntity repository);

	/**
	 * Returns the list of unused Groovy post-receive push hook scripts that are
	 * not already inherited by the repository. Script files must have .groovy
	 * extension
	 *
	 * @param repository
	 *            optional parameter
	 * @return list of available hook scripts
 	 * @since 1.4.0
	 */
	List<String> getPostReceiveScriptsUnused(TaskEntity repository);

	/**
	 * Search the specified repositories using the Lucene query.
	 *
	 * @param query a
	 * @param page a
	 * @param pageSize a
	 * @param repositories a
	 * @return a list of search results
 	 * @since 1.4.0
	 */
	List<SearchResult> search(String query, int page, int pageSize, List<String> repositories);

	/**
	 *
	 * @return true if we are running the gc executor
 	 * @since 1.4.0
	 */
	boolean isCollectingGarbage();

	/**
	 * Returns true if Gitblit is actively collecting garbage in this repository.
	 *
	 * @param repositoryName a
	 * @return true if actively collecting garbage
 	 * @since 1.4.0
	 */
	boolean isCollectingGarbage(String repositoryName);

	/**
	 * Ensures that all cached repositories are completely closed and their resources
	 * are properly released.
 	 * @since 1.4.0
	 */
	void closeAll();

	/**
	 * Ensures that a cached repository is completely closed and it's resources
	 * are properly released.
 	 * @since 1.4.0
	 */
	void close(String repository);

	/**
	 * Returns true if the repository is idle (not being accessed).
	 *
	 * @param repository a
	 * @return true if the repository is idle
 	 * @since 1.4.0
	 */
	boolean isIdle(Repository repository);


}