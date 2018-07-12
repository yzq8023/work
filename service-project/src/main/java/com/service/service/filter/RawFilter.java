package com.service.service.filter;

import com.service.service.Constants;
import com.service.service.entity.RepositoryModel;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;
import com.service.service.managers.IAuthenticationManager;
import com.service.service.managers.IRepositoryManager;
import com.service.service.managers.IRuntimeManager;
import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.annotation.WebFilter;

import static com.service.service.Constants.RAW_PATH;

/**
 * @auther: dk
 * @date: 2018/7/6
 * @des: RawFilter 是一个 AccessRestrictionFilter, 它确保对受视图限制的存储库的 http 分支请求进行身份验证和授权。
 */
@WebFilter(RAW_PATH)
@Order(4)
public class RawFilter extends AccessRestrictionFilter {

    @Autowired
    public RawFilter(
            IRuntimeManager runtimeManager,
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
        // 通过查找已知的 url 后缀从 url 获取存储库名称
        String repository = "";
        Repository r = null;
        int offset = 0;
        while (r == null) {
            int slash = url.indexOf('/', offset);
            if (slash == -1) {
                repository = url;
            } else {
                repository = url.substring(0, slash);
            }
            r = repositoryManager.getRepository(repository, false);
            if (r == null) {
                // try again
                offset = slash + 1;
            } else {
                // close the repo
                r.close();
            }
            if (repository.equals(url)) {
                // either only repository in url or no repository found
                break;
            }
        }
        return repository;
    }

    /**
     * 分析 url 并返回请求的操作。
     * @param url
     * @return
     */
    @Override
    protected String getUrlRequestAction(String url) {
        return "VIEW";
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
     * 确定用户是否可以访问存储库并执行指定的 actions
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
