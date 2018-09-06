/*
 * Copyright 2014 gitblit.com.
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
package com.service.service.extensions;

import com.service.service.git.GitblitReceivePack;
import org.eclipse.jgit.transport.ReceiveCommand;
import ro.fortsoft.pf4j.ExtensionPoint;

import java.util.Collection;

/**
 * 插件的拓展点，用于处理基于Pre- 和 Post- Receive的提交
 *
 * @author James Moger
 * @since 1.5.0
 */
public abstract class ReceiveHook implements ExtensionPoint {

	/**
	 * Called BEFORE received ref update commands have been written to the
	 * repository.  This allows extensions to process or reject incoming pushes
	 * using whatever logic may be appropriate.
	 *
	 * @param receivePack
	 * @param commands
	 * @since 1.5.0
	 */
	public abstract void onPreReceive(GitblitReceivePack receivePack, Collection<ReceiveCommand> commands);

	/**
	 * Called AFTER received ref update commands have been written to the
	 * repository.  This allows extensions to send notifications or trigger
	 * continuous integration systems.
	 *
	 * @param receivePack
	 * @param commands
	 * @since 1.5.0
	 */
	public abstract void onPostReceive(GitblitReceivePack receivePack, Collection<ReceiveCommand> commands);
}
