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

import com.service.service.entity.RepositoryModel;
import com.service.service.entity.TaskEntity;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 * Extension point to allow plugins to listen to major repository lifecycle events.
 *
 * @author James Moger
 * @since 1.6.0
 */
public abstract class RepositoryLifeCycleListener implements ExtensionPoint {

	/**
	 * Called after a repository has been created.
	 *
	 * @param repository
	 * @since 1.6.0
	 */
	public abstract void onCreation(TaskEntity repository);

	/**
	 * Called after a repository has been forked.
	 *
	 * @param origin
	 * @param fork
	 * @since 1.7.0
	 */
	public abstract void onFork(TaskEntity origin, TaskEntity fork);

	/**
	 * Called after a repository has been renamed.
	 *
	 * @param oldName
	 * @param repository
	 * @since 1.7.0
	 */
	public abstract void onRename(String oldName, TaskEntity repository);

	/**
	 * Called after a repository has been deleted.
	 *
	 * @param repository
	 * @since 1.6.0
	 */
	public abstract void onDeletion(TaskEntity repository);
}
