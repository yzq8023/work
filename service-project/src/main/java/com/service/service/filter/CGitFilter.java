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
package com.service.service.filter;

import com.service.service.Constants.AccessRestrictionType;
import com.service.service.Constants.AuthorizationControl;
import com.service.service.IStoredSettings;
import com.service.service.Keys;
import com.service.service.constant.FileStore;
import com.service.service.entity.TaskEntity;
import com.service.service.feign.IUserFeignClient;
import com.service.service.managers.IAuthenticationManager;
import com.service.service.managers.IRepositoryManager;
import com.service.service.managers.IRuntimeManager;
import com.service.service.entity.UserModel;
import com.service.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.service.service.exception.GitBlitException;
import org.springframework.core.annotation.Order;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

import static com.service.service.Constants.GIT_PATH;
import static com.service.service.Constants.R_PATH;

/**
 * @auther: dk
 * @date: 2018/7/6
 * @des: GitFilter 是一个 AccessRestrictionFilter,
 * 它确保了 Git 客户端对推送、克隆或查看受限存储库的请求得到了验证和授权。
 */
@WebFilter(urlPatterns = {R_PATH, GIT_PATH})
public class CGitFilter extends AccessRestrictionFilter {

    protected static final String gitReceivePack = "/git-receive-pack";

    protected static final String gitUploadPack = "/git-upload-pack";

    protected static final String gitLfs = "/info/lfs";

    protected static final String[] suffixes = {gitReceivePack, gitUploadPack, "/info/refs", "/HEAD",
            "/objects", gitLfs};

    private IStoredSettings settings;

    private IUserFeignClient userFeignClient;

    @Autowired
    public CGitFilter(
            IRuntimeManager runtimeManager,
            IAuthenticationManager authenticationManager,
            IRepositoryManager repositoryManager) {

        super(runtimeManager, authenticationManager, repositoryManager);

        this.settings = runtimeManager.getSettings();
    }

    /**
     * 从 url 中提取存储库名称。
     *
     * @param value
     * @return repository name
     */
    public static String getRepositoryName(String value) {
        String repository = value;
        // get the repository name from the url by finding a known url suffix
        for (String urlSuffix : suffixes) {
            if (repository.indexOf(urlSuffix) > -1) {
                repository = repository.substring(0, repository.indexOf(urlSuffix));
            }
        }
        return repository;
    }

    /**
     * 从 url 中提取存储库名称。
     *
     * @param url
     * @return repository name
     */
    @Override
    protected String extractRepositoryName(String url) {
        return CGitFilter.getRepositoryName(url);
    }

    /**
     * 分析 url 并返回请求的操作。
     * 返回值: "/git-receive-pack", "/git-upload-pack" or "/info/lfs".
     *
     * @param suffix
     * @return action of the request
     */
    @Override
    protected String getUrlRequestAction(String suffix) {
        if (!StringUtils.isEmpty(suffix)) {
            if (suffix.startsWith(gitReceivePack)) {
                return gitReceivePack;
            } else if (suffix.startsWith(gitUploadPack)) {
                return gitUploadPack;
            } else if (suffix.contains("?service=git-receive-pack")) {
                return gitReceivePack;
            } else if (suffix.contains("?service=git-upload-pack")) {
                return gitUploadPack;
            } else if (suffix.startsWith(gitLfs)) {
                return gitLfs;
            } else {
                return gitUploadPack;
            }
        }
        return null;
    }

    /**
     * 如果用户已进行身份验证, 则返回发出请求的用户。
     *
     * @param httpRequest
     * @return user
     */
    @Override
    protected UserModel getUser(HttpServletRequest httpRequest) {
        UserModel user = authenticationManager.authenticate(httpRequest, requiresClientCertificate());
        return user;
    }

    /**
     * 确定是否可以使用此筛选器创建非现有存储库。
     *
     * @return true if the server allows repository creation on-push
     */
    @Override
    protected boolean isCreationAllowed(String action) {

        //Repository must already exist before large files can be deposited
        if (action.equals(gitLfs)) {
            return false;
        }

        return settings.getBoolean(Keys.git.allowCreateOnPush, true);
    }

    /**
     * 确定存储库是否可以接收推送。
     *
     * @param repository
     * @param action
     * @return true if the action may be performed
     */
    @Override
    protected boolean isActionAllowed(TaskEntity repository, String action, String method) {
        // the log here has been moved into ReceiveHook to provide clients with
        // error messages
        if (gitLfs.equals(action)) {
            if (!method.matches("GET|POST|PUT|HEAD")) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected boolean requiresClientCertificate() {
        return settings.getBoolean(Keys.git.requiresClientCertificate, false);
    }

    /**
     * 确定存储库是否需要身份验证。
     *
     * @param repository
     * @param action
     * @param method
     * @return true if authentication required
     */
    @Override
    protected boolean requiresAuthentication(TaskEntity repository, String action, String method) {
        if (gitUploadPack.equals(action)) {
            // send to client
            return repository.getAccessRestriction().atLeast(AccessRestrictionType.CLONE);
        } else if (gitReceivePack.equals(action)) {
            // receive from client
            return repository.getAccessRestriction().atLeast(AccessRestrictionType.PUSH);
        } else if (gitLfs.equals(action)) {

            if (method.matches("GET|HEAD")) {
                return repository.getAccessRestriction().atLeast(AccessRestrictionType.CLONE);
            } else {
                //NOTE: Treat POST as PUT as as without reading message type cannot determine
                return repository.getAccessRestriction().atLeast(AccessRestrictionType.PUSH);
            }
        }
        return false;
    }

    /**
     * 确定用户是否可以访问存储库并执行指定的 action
     *
     * @param repository
     * @param user
     * @param action
     * @return true if user may execute the action on the repository
     */
    @Override
    protected boolean canAccess(TaskEntity repository, UserModel user, String action) {
        if (!settings.getBoolean(Keys.git.enableGitServlet, true)) {
            // Git Servlet disabled
            return false;
        }
        if (action.equals(gitReceivePack)) {
            // push permissions are enforced in the receive pack
            return true;
        } else if (action.equals(gitUploadPack)) {
            // Clone request
            if (user.canClone(repository)) {
                return true;
            } else {
                // user is unauthorized to clone this repository
                logger.warn(MessageFormat.format("user {0} is not authorized to clone {1}",
                        user.getUserId(), repository));
                return false;
            }
        }
        return true;
    }

    /**
     * 具有创建角色的经过身份验证的用户可以在 push
     * 上创建存储库。
     *
     * @param user
     * @param repository
     * @param action
     * @return the repository model, if it is created, null otherwise
     */
    @Override
    protected TaskEntity createRepository(UserModel user, String repository, String action) {
        boolean isPush = !StringUtils.isEmpty(action) && gitReceivePack.equals(action);

        if (action.equals(gitLfs)) {
            //Repository must already exist for any filestore actions
            return null;
        }

        if (isPush) {
            if (user.canCreate(repository)) {
                // user is pushing to a new repository
                // validate name
                if (repository.startsWith("../")) {
                    logger.error(MessageFormat.format("Illegal relative path in repository name! {0}", repository));
                    return null;
                }
                if (repository.contains("/../")) {
                    logger.error(MessageFormat.format("Illegal relative path in repository name! {0}", repository));
                    return null;
                }

                // confirm valid characters in repository name
                Character c = StringUtils.findInvalidCharacter(repository);
                if (c != null) {
                    logger.error(MessageFormat.format("Invalid character '{0}' in repository name {1}!", c, repository));
                    return null;
                }

                // create repository
                TaskEntity taskEntity = new TaskEntity();
                taskEntity.setTaskName(repository);
                taskEntity.setCrtUser(user.getUserId());
                taskEntity.setProjectPath(StringUtils.getFirstPathElement(repository));
                if (taskEntity.isUsersPersonalRepository(user.getUserId())) {
                    // 个人资料库, 默认为用户专用
                    taskEntity.setAuthorizationControl(AuthorizationControl.NAMED);
                    taskEntity.setAccessRestriction(AccessRestrictionType.VIEW);
                } else {
                    // 公用存储库, 用户默认服务器设置
                    taskEntity.setAuthorizationControl(AuthorizationControl.fromName(settings.getString(Keys.git.defaultAuthorizationControl, "")));
                    taskEntity.setAccessRestriction(AccessRestrictionType.fromName(settings.getString(Keys.git.defaultAccessRestriction, "PUSH")));
                }

                // create the repository
                try {
                    repositoryManager.updateRepositoryModel(taskEntity.getTaskName(), taskEntity, true);
                    logger.info(MessageFormat.format("{0} created {1} ON-PUSH", user.getUserId(), taskEntity.getTaskName()));
                    return repositoryManager.getRepositoryModel(taskEntity.getTaskName());
                } catch (GitBlitException e) {
                    e.printStackTrace();
                }
            } else {
                logger.warn(MessageFormat.format("{0} is not permitted to create repository {1} ON-PUSH!", user.getUserId(), repository));
            }
        }

        // 无法创建存储库或操作不是推送
        return null;
    }

    /**
     * Git lfs 操作使用另一种身份验证头,
     * 依赖于查看方法。
     *
     * @param httpRequest
     * @param action
     * @return
     */
    @Override
    protected String getAuthenticationHeader(HttpServletRequest httpRequest, String action) {

        if (action.equals(gitLfs)) {
            if (hasContentInRequestHeader(httpRequest, "Accept", FileStore.GIT_LFS_META_MIME)) {
                return "LFS-Authenticate";
            }
        }

        return super.getAuthenticationHeader(httpRequest, action);
    }

    /**
     * 根据操作查询请求标头
     *
     * @param action
     * @param request
     * @return
     */
    @Override
    protected boolean hasValidRequestHeader(String action,
                                            HttpServletRequest request) {

        if (action.equals(gitLfs) && request.getMethod().equals("POST")) {
            if (!hasContentInRequestHeader(request, "Accept", FileStore.GIT_LFS_META_MIME)
                    || !hasContentInRequestHeader(request, "Content-Type", FileStore.GIT_LFS_META_MIME)) {
                return false;
            }
        }

        return super.hasValidRequestHeader(action, request);
    }
}
