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

import com.github.wxiaoqi.security.api.vo.user.UserInfo;
import com.service.service.*;
import com.service.service.entity.TeamModel;
import com.service.service.entity.UserModel;
import com.service.service.extensions.UserTeamLifeCycleListener;
import com.service.service.feign.IUserFeignClient;
import com.service.service.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;

/**
 * 用户管理器管理用户和团队的持久性和检索。
 *
 * @author James Moger
 */
@Component
public class UserManager implements IUserManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IStoredSettings settings;

    private final IRuntimeManager runtimeManager;

    private final IPluginManager pluginManager;

    private IUserService userService;

    private IUserFeignClient userFeignClient;

    @Autowired
    public UserManager(IRuntimeManager runtimeManager, IPluginManager pluginManager, IUserFeignClient userFeignClient) {
        this.settings = runtimeManager.getSettings();
        this.runtimeManager = runtimeManager;
        this.pluginManager = pluginManager;
        this.userFeignClient = userFeignClient;
        this.start();
    }

    /**
     * 设置用户服务。用户服务对 * 本地 * 用户进行身份验证, 并负责持续和检索所有用户和所有团队。
     *
     * @param userService
     */
    public void setUserService(IUserService userService) {
        this.userService = userService;
        logger.info(userService.toString());
    }

    @Override
    public void setup(IRuntimeManager runtimeManager) {
        // NOOP
    }

    @Override
    public UserManager start() {
        if (this.userService == null) {
//			String realm = settings.getString(Keys.realm.userService, "${baseFolder}/users.conf");
            IUserService service = null;
            // 典型文件路径配置
            File realmFile = runtimeManager.getFileOrFolder(Keys.realm.userService, "${baseFolder}/users.conf");
            service = createUserService(realmFile);
            setUserService(service);
        }
        return this;
    }

    /**
     * 创建 {@link IUserService} 通过 {@link #runtimeManager} 作为构造器成员
     *
     * @param realm the class name of the {@link IUserService} to be instantiated
     * @return the {@link IUserService} or {@code null} if instantiation fails
     */
    private IUserService createIRuntimeManagerAwareUserService(String realm) {
        try {
            Constructor<?> constructor = Class.forName(realm).getConstructor(IRuntimeManager.class);
            return (IUserService) constructor.newInstance(runtimeManager);
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.error("failed to instantiate user service {}: {}", realm, e.getMessage());
            return null;
        }
    }

    protected IUserService createUserService(File realmFile) {
        IUserService service = null;
        service = new ConfigUserService();
        return service;
    }

    @Override
    public UserManager stop() {
        return this;
    }

    /**
     * 如果用户名代表一个内部帐户，返回true
     *
     * @param username
     * @return true if the specified username represents an internal account
     */
    @Override
    public boolean isInternalAccount(String username) {
        return !StringUtils.isEmpty(username)
                && (username.equalsIgnoreCase(Constants.FEDERATION_USER)
                || username.equalsIgnoreCase(UserModel.ANONYMOUS.getUsername()));
    }

    /**
     * 返回指定用户的cookie值。
     *
     * @param model
     * @return cookie value
     */
    @Override
    public String getCookie(UserModel model) {
        return userService.getCookie(model);
    }

    /**
     * 检索指定cookie的用户对象。
     *
     * @param cookie
     * @return a user object or null
     */
    @Override
    public UserModel getUserModel(char[] cookie) {
        UserModel user = userService.getUserModel(cookie);
        return user;
    }

    /**
     * 检索指定用户名的用户对象。
     *
     * @param username
     * @return a user object or null
     */
    @Override
    public UserModel getUserModel(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        String usernameDecoded = StringUtils.decodeUsername(username);
        UserModel user = userService.getUserModel(usernameDecoded);
        return user;
    }

    /**
     * 检索指定用户名的用户对象。
     *
     * @param userId
     * @return a user object or null
     */
    @Override
    public UserModel getUserModel(Integer userId) {
        if (StringUtils.isEmpty(String.valueOf(userId))) {
            return null;
        }
        UserInfo userInfo = userFeignClient.info(userId);
        UserModel user = new UserModel(userInfo.getId());
        BeanUtils.copyProperties(userInfo, user);
        return user;
    }

    /**
     * 更新/写一个完整的用户对象。
     *
     * @param model
     * @return true if update is successful
     */
    @Override
    public boolean updateUserModel(UserModel model) {
        final boolean isCreate = null == userService.getUserModel(model.getUsername());
        if (userService.updateUserModel(model)) {
            if (isCreate) {
                callCreateUserListeners(model);
            }
            return true;
        }
        return false;
    }

    /**
     * 更新/写入所有指定的用户对象。
     *
     * @param models a list of user models
     * @return true if update is successful
     * @since 1.2.0
     */
    @Override
    public boolean updateUserModels(Collection<UserModel> models) {
        return userService.updateUserModels(models);
    }

    /**
     * 添加/更新由用户名键入的用户对象。这种方法允许重命名用户。
     *
     * @param username the old username
     * @param model    the user object to use for username
     * @return true if update is successful
     */
    @Override
    public boolean updateUserModel(String username, UserModel model) {
        final boolean isCreate = null == userService.getUserModel(username);
        if (userService.updateUserModel(username, model)) {
            if (isCreate) {
                callCreateUserListeners(model);
            }
            return true;
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
    public boolean deleteUserModel(UserModel model) {
        if (userService.deleteUserModel(model)) {
            callDeleteUserListeners(model);
            return true;
        }
        return false;
    }

    /**
     * 使用指定的用户名删除用户对象
     *
     * @param username
     * @return true if successful
     */
    @Override
    public boolean deleteUser(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        String usernameDecoded = StringUtils.decodeUsername(username);
        UserModel user = getUserModel(usernameDecoded);
        if (userService.deleteUser(usernameDecoded)) {
            callDeleteUserListeners(user);
            return true;
        }
        return false;
    }

    /**
     * 返回登录服务可用的所有用户列表。
     *
     * @return list of all usernames
     */
    @Override
    public List<String> getAllUsernames() {
        List<String> names = new ArrayList<String>(userService.getAllUsernames());
        return names;
    }

    /**
     * 返回登录服务可用的所有用户列表。
     *
     * @return list of all users
     * @since 0.8.0
     */
    @Override
    public List<UserModel> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        return users;
    }

    /**
     * 返回登录服务可用的所有团队的列表。
     *
     * @return list of all teams
     * @since 0.8.0
     */
    @Override
    public List<String> getAllTeamNames() {
        List<String> teams = userService.getAllTeamNames();
        return teams;
    }

    /**
     * 返回登录服务可用的所有团队的列表。
     *
     * @return list of all teams
     * @since 0.8.0
     */
    @Override
    public List<TeamModel> getAllTeams() {
        List<TeamModel> teams = userService.getAllTeams();
        return teams;
    }

    /**
     * 返回所有被允许绕过指定存储库上的访问限制的团队的列表。
     *
     * @param role the repository name
     * @return list of all teams that can bypass the access restriction
     * @since 0.8.0
     */
    @Override
    public List<String> getTeamNamesForRepositoryRole(String role) {
        List<String> teams = userService.getTeamNamesForRepositoryRole(role);
        return teams;
    }

    /**
     * 检索团队对象以获得指定的团队名称。
     *
     * @param teamname
     * @return a team object or null
     * @since 0.8.0
     */
    @Override
    public TeamModel getTeamModel(String teamname) {
        TeamModel team = userService.getTeamModel(teamname);
        return team;
    }

    /**
     * 更新/写一个完整的团队对象。
     *
     * @param model
     * @return true if update is successful
     * @since 0.8.0
     */
    @Override
    public boolean updateTeamModel(TeamModel model) {
        final boolean isCreate = null == userService.getTeamModel(String.valueOf(model.getId()));
        if (userService.updateTeamModel(model)) {
            if (isCreate) {
                callCreateTeamListeners(model);
            }
            return true;
        }
        return false;
    }

    /**
     * 更新/写入所有指定的团队对象。
     *
     * @param models a list of team models
     * @return true if update is successful
     * @since 1.2.0
     */
    @Override
    public boolean updateTeamModels(Collection<TeamModel> models) {
        return userService.updateTeamModels(models);
    }

    /**
     * 更新/写入和替换一个由teamname键入的完整的团队对象。这种方法允许重命名一个团队。
     *
     * @param teamname the old teamname
     * @param model    the team object to use for teamname
     * @return true if update is successful
     * @since 0.8.0
     */
    @Override
    public boolean updateTeamModel(String teamname, TeamModel model) {
        final boolean isCreate = null == userService.getTeamModel(teamname);
        if (userService.updateTeamModel(teamname, model)) {
            if (isCreate) {
                callCreateTeamListeners(model);
            }
            return true;
        }
        return false;
    }

    /**
     * 从用户服务中删除团队对象。
     *
     * @param model
     * @return true if successful
     * @since 0.8.0
     */
    @Override
    public boolean deleteTeamModel(TeamModel model) {
        if (userService.deleteTeamModel(model)) {
            callDeleteTeamListeners(model);
            return true;
        }
        return false;
    }

    /**
     * 用指定的teamname删除team object
     *
     * @param teamname
     * @return true if successful
     * @since 0.8.0
     */
    @Override
    public boolean deleteTeam(String teamname) {
        TeamModel team = userService.getTeamModel(teamname);
        if (userService.deleteTeam(teamname)) {
            callDeleteTeamListeners(team);
            return true;
        }
        return false;
    }

    /**
     * 返回所有允许绕过指定存储库上的访问限制的用户的列表。
     *
     * @param role the repository name
     * @return list of all usernames that can bypass the access restriction
     * @since 0.8.0
     */
    @Override
    public List<String> getUsernamesForRepositoryRole(String role) {
        return userService.getUsernamesForRepositoryRole(role);
    }

    /**
     * 重命名一个存储库的作用。
     *
     * @param oldRole
     * @param newRole
     * @return true if successful
     */
    @Override
    public boolean renameRepositoryRole(String oldRole, String newRole) {
        return userService.renameRepositoryRole(oldRole, newRole);
    }

    /**
     * 从所有用户中删除存储库角色。
     *
     * @param role
     * @return true if successful
     */
    @Override
    public boolean deleteRepositoryRole(String role) {
        return userService.deleteRepositoryRole(role);
    }

    protected void callCreateUserListeners(UserModel user) {
        if (pluginManager == null || user == null) {
            return;
        }

        for (UserTeamLifeCycleListener listener : pluginManager.getExtensions(UserTeamLifeCycleListener.class)) {
            try {
                listener.onCreation(user);
            } catch (Throwable t) {
                logger.error(String.format("failed to call plugin.onCreation%s", user.getUsername()), t);
            }
        }
    }

    protected void callCreateTeamListeners(TeamModel team) {
        if (pluginManager == null || team == null) {
            return;
        }

        for (UserTeamLifeCycleListener listener : pluginManager.getExtensions(UserTeamLifeCycleListener.class)) {
            try {
                listener.onCreation(team);
            } catch (Throwable t) {
                logger.error(String.format("failed to call plugin.onCreation %s", team.getId()), t);
            }
        }
    }

    protected void callDeleteUserListeners(UserModel user) {
        if (pluginManager == null || user == null) {
            return;
        }

        for (UserTeamLifeCycleListener listener : pluginManager.getExtensions(UserTeamLifeCycleListener.class)) {
            try {
                listener.onDeletion(user);
            } catch (Throwable t) {
                logger.error(String.format("failed to call plugin.onDeletion %s", user.getUsername()), t);
            }
        }
    }

    protected void callDeleteTeamListeners(TeamModel team) {
        if (pluginManager == null || team == null) {
            return;
        }

        for (UserTeamLifeCycleListener listener : pluginManager.getExtensions(UserTeamLifeCycleListener.class)) {
            try {
                listener.onDeletion(team);
            } catch (Throwable t) {
                logger.error(String.format("failed to call plugin.onDeletion %s", team.getId()), t);
            }
        }
    }

}
