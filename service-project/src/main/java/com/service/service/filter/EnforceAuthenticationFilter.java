package com.service.service.filter;

import com.service.service.IStoredSettings;
import com.service.service.Keys;
import com.service.service.managers.IAuthenticationManager;
import com.service.service.entity.UserModel;

import com.service.service.managers.IRuntimeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

import static com.service.service.Constants.ALL;

/**
 * 这个过滤器通过HTTP基本身份验证强制认证，如果设置表明是这样的话。
 * 它查看设置“web.authenticateViewPages”和“web.enforceHttpBasicAuthentication”;
 * 如果两者都是正确的，那么任何未经授权的访问都将通过一个HTTP基本身份验证头来实现。
 *
 * @author Laurens Vrijnsen
 *
 */
@WebFilter(urlPatterns = ALL)
@Order(2)
public class EnforceAuthenticationFilter implements Filter {

	protected transient Logger logger = LoggerFactory.getLogger(getClass());

	private IStoredSettings settings;

	private IAuthenticationManager authenticationManager;

	@Autowired
	public EnforceAuthenticationFilter(
			IRuntimeManager runtimeManager,
			IAuthenticationManager authenticationManager) {

		this.settings = runtimeManager.getSettings();
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}

	/**
	 * 这是实际的过滤：用户是经过身份验证的吗？如果没有，则强制执行HTTP认证（401）
	 *
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		Boolean mustForceAuth = settings.getBoolean(Keys.web.authenticateViewPages, false)
								&& settings.getBoolean(Keys.web.enforceHttpBasicAuthentication, false);

		HttpServletRequest httpRequest  = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		UserModel user = authenticationManager.authenticate(httpRequest);

		if (mustForceAuth && (user == null)) {
			// not authenticated, enforce now:
			logger.debug(MessageFormat.format("EnforceAuthFilter: user not authenticated for URL {0}!", request.toString()));
			String challenge = MessageFormat.format("Basic realm=\"{0}\"", settings.getString(Keys.web.siteName, ""));
			httpResponse.setHeader("WWW-Authenticate", challenge);
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;

		} else {
			// user is authenticated, or don't care, continue handling
			chain.doFilter(request, response);
		}
	}
}
