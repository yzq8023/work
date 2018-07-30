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

import com.service.service.entity.TaskEntity;
import com.service.service.managers.IAuthenticationManager;
import com.service.service.managers.IRepositoryManager;
import com.service.service.managers.IRuntimeManager;
import com.service.service.entity.UserModel;
import com.service.service.utils.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Iterator;

/**
 * The AccessRestrictionFilter is an AuthenticationFilter that confirms that the
 * requested repository can be accessed by the anonymous or named user.
 *
 * The filter extracts the name of the repository from the url and determines if
 * the requested action for the repository requires a Basic authentication
 * prompt. If authentication is required and no credentials are stored in the
 * "Authorization" header, then a basic authentication challenge is issued.
 *
 * http://en.wikipedia.org/wiki/Basic_access_authentication
 *
 * @author James Moger
 *
 */

public abstract class AccessRestrictionFilter extends AuthenticationFilter {

	protected IRuntimeManager runtimeManager;

	protected IRepositoryManager repositoryManager;

	protected AccessRestrictionFilter(
			IRuntimeManager runtimeManager,
			IAuthenticationManager authenticationManager,
			IRepositoryManager repositoryManager) {

		super(authenticationManager);

		this.runtimeManager = runtimeManager;
		this.repositoryManager = repositoryManager;
	}

	/**
	 * Extract the repository name from the url.
	 *
	 * @param url
	 * @return repository name
	 */
	protected abstract String extractRepositoryName(String url);

	/**
	 * Analyze the url and returns the action of the request.
	 *
	 * @param url
	 * @return action of the request
	 */
	protected abstract String getUrlRequestAction(String url);

	/**
	 * Determine if a non-existing repository can be created using this filter.
	 *
	 * @return true if the filter allows repository creation
	 */
	protected abstract boolean isCreationAllowed(String action);

	/**
	 * Determine if the action may be executed on the repository.
	 *
	 * @param repository
	 * @param action
	 * @param method
	 * @return true if the action may be performed
	 */
	protected abstract boolean isActionAllowed(TaskEntity repository, String action, String method);

	/**
	 * Determine if the repository requires authentication.
	 *
	 * @param repository
	 * @param action
	 * @return true if authentication required
	 */
	protected abstract boolean requiresAuthentication(TaskEntity repository, String action, String method);

	/**
	 * Determine if the user can access the repository and perform the specified
	 * action.
	 *
	 * @param repository
	 * @param user
	 * @param action
	 * @return true if user may execute the action on the repository
	 */
	protected abstract boolean canAccess(TaskEntity repository, UserModel user, String action);

	/**
	 * Allows a filter to create a repository, if one does not exist.
	 *
	 * @param user
	 * @param repository
	 * @param action
	 * @return the repository model, if it is created, null otherwise
	 */
	protected TaskEntity createRepository(UserModel user, String repository, String action) {
		return null;
	}
	
	/**
	 * Allows authentication header to be altered based on the action requested
	 * Default is WWW-Authenticate
	 * @param httpRequest
	 * @param action
	 * @return authentication type header
	 */
	protected String getAuthenticationHeader(HttpServletRequest httpRequest, String action) {
		return "WWW-Authenticate";
	}
	
	/**
	 * Allows request headers to be used as part of filtering
	 * @param request
	 * @return true (default) if headers are valid, false otherwise
	 */
	protected boolean hasValidRequestHeader(String action, HttpServletRequest request) {
		return true;
	}
	
	/**
	 * doFilter does the actual work of preprocessing the request to ensure that
	 * the user may proceed.
	 *
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String fullUrl = getFullUrl(httpRequest);
		String repository = extractRepositoryName(fullUrl);
		if (StringUtils.isEmpty(repository)) {
			httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		// 确定请求 URL 是否受到限制
		String fullSuffix = fullUrl.substring(repository.length());
		String urlRequestType = getUrlRequestAction(fullSuffix);

		UserModel user = getUser(httpRequest);

		// 加载库
		TaskEntity model = repositoryManager.getRepositoryModel(repository);
		if (model == null) {
			if (isCreationAllowed(urlRequestType)) {
				if (user == null) {
					// 客户提供创建凭据。发送401。
					if (runtimeManager.isDebugMode()) {
						logger.info(MessageFormat.format("ARF: CREATE CHALLENGE {0}", fullUrl));
					}
					
					httpResponse.setHeader(getAuthenticationHeader(httpRequest, urlRequestType), CHALLENGE);
					httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				} else {
					// 查看是否可以为此请求创建存储库
					model = createRepository(user, repository, urlRequestType);
				}
			}

			if (model == null) {
				// 仓库没有找到，返回404。
				logger.info(MessageFormat.format("ARF: {0} ({1})", fullUrl,
						HttpServletResponse.SC_NOT_FOUND));
				httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}

		// 确认操作可能在存储库上执行
		if (!isActionAllowed(model, urlRequestType, httpRequest.getMethod())) {
			logger.info(MessageFormat.format("ARF: action {0} on {1} forbidden ({2})",
					urlRequestType, model, HttpServletResponse.SC_FORBIDDEN));
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		// 用access限制性请求包装HttpServletRequest，它覆盖servlet容器用户主体方法。
		// JGit requires either:
		//
		// 1. servlet container authenticated user
		// 2. http.receivepack = true in each repository's config
		//
		// Gitblit必须有条件地对每个存储库进行身份验证，以便启用http.receivepack是不够的。
		AuthenticatedRequest authenticatedRequest = new AuthenticatedRequest(httpRequest);
		if (user != null) {
			authenticatedRequest.setUser(user);
		}

		// 基本身份验证和响应处理
		if (!StringUtils.isEmpty(urlRequestType) && requiresAuthentication(model, urlRequestType,  httpRequest.getMethod())) {
			if (user == null) {
				// 对客户端凭证产生质疑，发送401
				if (runtimeManager.isDebugMode()) {
					logger.info(MessageFormat.format("ARF: CHALLENGE {0}", fullUrl));
				}
				httpResponse.setHeader(getAuthenticationHeader(httpRequest, urlRequestType), CHALLENGE);
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			} else {
				// 检查用户访问请求
				if (user.canAdmin() || canAccess(model, user, urlRequestType)) {
					// 身份验证请求允许。
					// 将处理传递给受限的servlet。
					newSession(authenticatedRequest, httpResponse);
					logger.info(MessageFormat.format("ARF: authenticated {0} to {1} ({2})", user.getUserId(),
							fullUrl, HttpServletResponse.SC_CONTINUE));
					chain.doFilter(authenticatedRequest, httpResponse);
					return;
				}
				// 有效用户，但不是请求访问。送403。
				if (runtimeManager.isDebugMode()) {
					logger.info(MessageFormat.format("ARF: {0} forbidden to access {1}",
							user.getUserId(), fullUrl));
				}
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		}

		if (runtimeManager.isDebugMode()) {
			logger.info(MessageFormat.format("ARF: {0} ({1}) unauthenticated", fullUrl,
					HttpServletResponse.SC_CONTINUE));
		}
		// 未经身份验证的请求允许。
		// 将处理传递给受限的servlet。
		chain.doFilter(authenticatedRequest, httpResponse);
	}
	
	public static boolean hasContentInRequestHeader(HttpServletRequest request, String headerName, String content)
	{
		Iterator<String> headerItr = Collections.list(request.getHeaders(headerName)).iterator();
		
		while (headerItr.hasNext()) {
			if (headerItr.next().contains(content)) {
				return true;
			}
		}

		return false;
	}
}