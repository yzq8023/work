package com.service.service.filter;

import com.service.service.Constants;
import com.service.service.IStoredSettings;
import com.service.service.Keys;
import com.service.service.entity.UserModel;
import com.service.service.managers.IAuthenticationManager;
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

import static com.service.service.Constants.RPC_PATH;

/**
 * @auther: dk
 * @date: 2018/7/6
 * @des: RpcFilter 是一个用于保护 RpcServlet 的 servlet 过滤器。
 *       过滤器从 url 中提取 rpc 请求类型, 并确定请求的操作是否需要基本身份验证。
 *       如果需要身份验证并且没有凭据存储在 "授权" 标头中, 则会发出基本的身份验证质询。
 */
@WebFilter(RPC_PATH)
@Order(6)
public class RpcFilter extends AuthenticationFilter {

    private IStoredSettings settings;

    private IRuntimeManager runtimeManager;

    @Autowired
    protected RpcFilter(
            IStoredSettings settings,
            IRuntimeManager runtimeManager,
            IAuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.settings = settings;
        this.runtimeManager = runtimeManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String fullUrl = getFullUrl(httpRequest);
        Constants.RpcRequest requestType = Constants.RpcRequest.fromName(httpRequest.getParameter("req"));
        if (requestType == null) {
            httpResponse.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
            return;
        }

        boolean adminRequest = requestType.exceeds(Constants.RpcRequest.LIST_SETTINGS);

        // conditionally reject all rpc requests
        if (!settings.getBoolean(Keys.web.enableRpcServlet, true)) {
            logger.warn(Keys.web.enableRpcServlet + " must be set TRUE for rpc requests.");
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        boolean authenticateView = settings.getBoolean(Keys.web.authenticateViewPages, false);
        boolean authenticateAdmin = settings.getBoolean(Keys.web.authenticateAdminPages, true);

        // Wrap the HttpServletRequest with the RpcServletRequest which
        // overrides the servlet container user principal methods.
        AuthenticatedRequest authenticatedRequest = new AuthenticatedRequest(httpRequest);
        UserModel user = getUser(httpRequest);
        if (user != null) {
            authenticatedRequest.setUser(user);
        }

        // conditionally reject rpc management/administration requests
        if (adminRequest && !settings.getBoolean(Keys.web.enableRpcManagement, false)) {
            logger.warn(MessageFormat.format("{0} must be set TRUE for {1} rpc requests.",
                    Keys.web.enableRpcManagement, requestType.toString()));
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // BASIC authentication challenge and response processing
        if ((adminRequest && authenticateAdmin) || (!adminRequest && authenticateView)) {
            if (user == null) {
                // challenge client to provide credentials. send 401.
                if (runtimeManager.isDebugMode()) {
                    logger.info(MessageFormat.format("RPC: CHALLENGE {0}", fullUrl));

                }
                httpResponse.setHeader("WWW-Authenticate", CHALLENGE);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } else {
                // check user access for request
                if (user.canAdmin() || !adminRequest) {
                    // authenticated request permitted.
                    // pass processing to the restricted servlet.
                    newSession(authenticatedRequest, httpResponse);
                    logger.info(MessageFormat.format("RPC: {0} ({1}) authenticated", fullUrl,
                            HttpServletResponse.SC_CONTINUE));
                    chain.doFilter(authenticatedRequest, httpResponse);
                    return;
                }
                // valid user, but not for requested access. send 403.
                logger.warn(MessageFormat.format("RPC: {0} forbidden to access {1}",
                        user.username, fullUrl));
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        if (runtimeManager.isDebugMode()) {
            logger.info(MessageFormat.format("RPC: {0} ({1}) unauthenticated", fullUrl,
                    HttpServletResponse.SC_CONTINUE));
        }
        // unauthenticated request permitted.
        // pass processing to the restricted servlet.
        chain.doFilter(authenticatedRequest, httpResponse);
    }
}
