package com.service.service.filter;

import ro.fortsoft.pf4j.ExtensionPoint;

import javax.servlet.*;
import java.io.IOException;

/**
 * 用于拦截通过服务器传递的 HTTP 请求的扩展点。
 *
 * @author David Ostrovsky
 * @since 1.6.0
 *
 */
public abstract class HttpRequestFilter implements Filter, ExtensionPoint {

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public abstract void doFilter(ServletRequest request, ServletResponse response,
                                  FilterChain chain) throws IOException, ServletException;
}
