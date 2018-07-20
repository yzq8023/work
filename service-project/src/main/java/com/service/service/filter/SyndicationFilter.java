package com.service.service.filter;

import com.service.service.Constants;
import com.service.service.entity.ProjectModel;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;
import com.service.service.managers.IAuthenticationManager;
import com.service.service.managers.IProjectManager;
import com.service.service.managers.IRepositoryManager;
import com.service.service.managers.IRuntimeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

import static com.service.service.Constants.SYNDICATION_PATH;

/**
 * @auther: dk
 * @date: 2018/7/6
 * @des: SyndicationFilter 是一个 AuthenticationFilter,
 *       它确保对项目或受视图限制的存储库的馈送请求具有正确的身份验证凭据,
 *       并被请求的提要授权。
 */
@WebFilter(SYNDICATION_PATH)
@Order(8)
public class SyndicationFilter extends AuthenticationFilter {

    private IRuntimeManager runtimeManager;
    private IRepositoryManager repositoryManager;
    private IProjectManager projectManager;

    @Autowired
    public SyndicationFilter(
            IRuntimeManager runtimeManager,
            IAuthenticationManager authenticationManager,
            IRepositoryManager repositoryManager,
            IProjectManager projectManager) {
        super(authenticationManager);

        this.runtimeManager = runtimeManager;
        this.repositoryManager = repositoryManager;
        this.projectManager = projectManager;
    }

    /**
     * 从 url 中提取存储库名称。
     * @param url
     * @return
     */
    protected String extractRequestedName(String url) {
        if (url.indexOf('?') > -1) {
            return url.substring(0, url.indexOf('?'));
        }
        return url;
    }

    /**
     * doFilter 执行对请求进行预处理的实际工作, 以确保用户可以继续。
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String fullUrl = getFullUrl(httpRequest);
        String name = extractRequestedName(fullUrl);

        ProjectModel project = projectManager.getProjectModel(name);
        TaskEntity model = null;

        if (project == null) {
            // try loading a repository model
            model = repositoryManager.getRepositoryModel(name);
            if (model == null) {
                // repository not found. send 404.
                logger.info(MessageFormat.format("ARF: {0} ({1})", fullUrl,
                        HttpServletResponse.SC_NOT_FOUND));
                httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        // Wrap the HttpServletRequest with the AccessRestrictionRequest which
        // overrides the servlet container user principal methods.
        // JGit requires either:
        //
        // 1. servlet container authenticated user
        // 2. http.receivepack = true in each repository's config
        //
        // Gitblit must conditionally authenticate users per-repository so just
        // enabling http.receivepack is insufficient.
        AuthenticatedRequest authenticatedRequest = new AuthenticatedRequest(httpRequest);
        UserModel user = getUser(httpRequest);
        if (user != null) {
            authenticatedRequest.setUser(user);
        }

        // BASIC authentication challenge and response processing
        if (model != null) {
            if (model.getAccessRestriction().atLeast(Constants.AccessRestrictionType.VIEW)) {
                if (user == null) {
                    // challenge client to provide credentials. send 401.
                    if (runtimeManager.isDebugMode()) {
                        logger.info(MessageFormat.format("ARF: CHALLENGE {0}", fullUrl));
                    }
                    httpResponse.setHeader("WWW-Authenticate", CHALLENGE);
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                } else {
                    // check user access for request
                    if (user.canView(model)) {
                        // authenticated request permitted.
                        // pass processing to the restricted servlet.
                        newSession(authenticatedRequest, httpResponse);
                        logger.info(MessageFormat.format("ARF: {0} ({1}) authenticated", fullUrl,
                                HttpServletResponse.SC_CONTINUE));
                        chain.doFilter(authenticatedRequest, httpResponse);
                        return;
                    }
                    // valid user, but not for requested access. send 403.
                    if (runtimeManager.isDebugMode()) {
                        logger.info(MessageFormat.format("ARF: {0} forbidden to access {1}",
                                user.getUserId(), fullUrl));
                    }
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }
        }

        if (runtimeManager.isDebugMode()) {
            logger.info(MessageFormat.format("ARF: {0} ({1}) unauthenticated", fullUrl,
                    HttpServletResponse.SC_CONTINUE));
        }
        // unauthenticated request permitted.
        // pass processing to the restricted servlet.
        chain.doFilter(authenticatedRequest, httpResponse);
    }
}
