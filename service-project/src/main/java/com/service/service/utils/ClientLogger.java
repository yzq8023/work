/*
 * Copyright 2012 John Crygier
 * Copyright 2012 gitblit.com
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
package com.service.service.utils;

import org.eclipse.jgit.transport.ReceivePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 将消息记录到push Git客户端的类。打算用于Groovy钩子。
 *
 * @author John Crygier
 *
 */
public class ClientLogger {

	static final Logger logger = LoggerFactory.getLogger(ClientLogger.class);
	private ReceivePack rp;

	public ClientLogger(ReceivePack rp) {
		this.rp = rp;
	}

	/**
	 * Sends an info/warning message to the git client.
	 *
	 * @param message
	 */
	public void info(String message) {
		rp.sendMessage(message);
	}

	/**
	 * Sends an error message to the git client.
	 *
	 * @param message
	 */
	public void error(String message) {
		rp.sendError(message);
	}

	/**
	 * Sends an error message to the git client with an exception.
	 *
	 * @param message
	 * @param t
	 *            an exception
	 */
	public void error(String message, Throwable t) {
		PrintWriter writer = new PrintWriter(new StringWriter());
		if (!StringUtils.isEmpty(message)) {
			writer.append(message);
			writer.append('\n');
		}
		t.printStackTrace(writer);
		rp.sendError(writer.toString());
	}

}
