package com.service.service.filter;

import com.service.service.Constants;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;
import com.service.service.managers.IAuthenticationManager;
import com.service.service.managers.IRepositoryManager;
import com.service.service.managers.IRuntimeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.annotation.WebFilter;

import static com.service.service.Constants.ZIP_PATH;

/**
 * @auther: dk
 * @date: 2018/7/6
 * @des: DownloadZipFilter 是一个 AccessRestrictionFilter,
 *       它确保查看受限存储库的 zip 请求具有适当的身份验证凭据并得到授权。
 */
//@WebFilter(ZIP_PATH)
public class GDownloadZipFilter extends AccessRestrictionFilter {

    @Autowired
    protected GDownloadZipFilter(IRuntimeManager runtimeManager,
                                IAuthenticationManager authenticationManager,
                                IRepositoryManager repositoryManager) {
        super(runtimeManager, authenticationManager, repositoryManager);
    }

    /**
     * 从 url 中提取存储库名称。
     * @param url
     * @return
     */
    @Override
    protected String extractRepositoryName(String url) {
        int a = url.indexOf("r=");
        if (a > -1) {
            String repository = url.substring(a + 2);
            if (repository.indexOf('&') > -1) {
                repository = repository.substring(0, repository.indexOf('&'));
            }
            return repository;
        }
        return null;
    }

    /**
     *分析 url 并返回请求的操作。
     * @param url
     * @return
     */
    @Override
    protected String getUrlRequestAction(String url) {
        return "DOWNLOAD";
    }

    /**
     * 确定是否可以使用此筛选器创建非现有存储库。
     * @param action
     * @return
     */
    @Override
    protected boolean isCreationAllowed(String action) {
        return false;
    }

    /**
     * 确定是否可以在存储库上执行该操作。
     * @param repository
     * @param action
     * @param method
     * @return
     */
    @Override
    protected boolean isActionAllowed(TaskEntity repository, String action, String method) {
        return true;
    }

    /**
     * 确定存储库是否需要身份验证。
     * @param repository
     * @param action
     * @param method
     * @return
     */
    @Override
    protected boolean requiresAuthentication(TaskEntity repository, String action, String method) {
        return repository.getAccessRestriction().atLeast(Constants.AccessRestrictionType.VIEW);
    }

    /**
     * 确定用户是否可以访问存储库并执行指定的操作。
     * @param repository
     * @param user
     * @param action
     * @return
     */
    @Override
    protected boolean canAccess(TaskEntity repository, UserModel user, String action) {
        return user.canView(repository);
    }
}
