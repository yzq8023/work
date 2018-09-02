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
package com.service.service.git;

import com.service.service.managers.IAuthenticationManager;
import com.service.service.transport.git.GitDaemonClient;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UploadPack;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.eclipse.jgit.transport.resolver.UploadPackFactory;

/**
 * 上传任务包工厂，创建一个上传任务包来控制refs，告知正在clone或pull的客户端
 * @author hollykunge
 *
 * @param <X> the connection type
 */
public class GitblitUploadPackFactory<X> implements UploadPackFactory<X> {

	private final IAuthenticationManager authenticationManager;

	public GitblitUploadPackFactory(IAuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public UploadPack create(X req, Repository db)
			throws ServiceNotEnabledException, ServiceNotAuthorizedException {

		int timeout = 0;

		if (req instanceof GitDaemonClient) {
			// git daemon request is always anonymous
			GitDaemonClient client = (GitDaemonClient) req;
			// set timeout from Git daemon
			timeout = client.getDaemon().getTimeout();
		}

		UploadPack up = new UploadPack(db);
		up.setTimeout(timeout);

		return up;
	}
}