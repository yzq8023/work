package com.service.service.filter;

import com.service.service.config.FilterRuntimeConfig;
import com.service.service.managers.IPluginManager;
import com.service.service.managers.IRuntimeManager;

import org.springframework.beans.factory.annotation.Autowired;
import ro.fortsoft.pf4j.PluginWrapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.service.service.Constants.ALL;
import static com.service.service.Constants.GIT_PATH;
import static com.service.service.Constants.R_PATH;

/**
 * 请求过滤器，而不是允许注册的扩展请求过滤器访问请求数据。
 * 其目的是为了服务器监视插件。
 *
 * @author David Ostrovsky
 * @since 1.6.0
 */
@WebFilter(urlPatterns = {R_PATH, GIT_PATH})
public class AProxyFilter implements Filter {
	private final IRuntimeManager runtimeManager;

	private final IPluginManager pluginManager;

	private final List<HttpRequestFilter> filters;

	@Autowired
	public AProxyFilter(
			IRuntimeManager runtimeManager,
			IPluginManager pluginManager) {

		this.runtimeManager = runtimeManager;
		this.pluginManager = pluginManager;
		this.filters = new ArrayList<>();

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		filters.addAll(pluginManager.getExtensions(HttpRequestFilter.class));
		for (HttpRequestFilter f : filters) {
			// wrap the filter config for Gitblit settings retrieval
			PluginWrapper pluginWrapper = pluginManager.whichPlugin(f.getClass());
			FilterConfig runtimeConfig = new FilterRuntimeConfig(runtimeManager,
					pluginWrapper.getPluginId(), filterConfig);

			f.init(runtimeConfig);
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, final FilterChain last)
			throws IOException, ServletException {
		final Iterator<HttpRequestFilter> itr = filters.iterator();
		new FilterChain() {
			@Override
			public void doFilter(ServletRequest req, ServletResponse res) throws IOException,
                    ServletException {
				if (itr.hasNext()) {
					itr.next().doFilter(req, res, this);
				} else {
					last.doFilter(req, res);
				}
			}
		}.doFilter(req, res);
	}

	@Override
	public void destroy() {
		for (HttpRequestFilter f : filters) {
			f.destroy();
		}
	}
}
