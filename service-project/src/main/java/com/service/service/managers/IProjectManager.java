/*
 * Copyright 2013 gitblit.com.
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
package com.service.service.managers;

import com.service.service.entity.ProjectModel;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;

import java.util.List;

/**
 * @author workhub
 */

public interface IProjectManager extends IManager {

	/**
	 * Returns a list of project models for the user.
	 *
	 * @param user a
	 * @param includeUsers a
	 * @return list of projects that are accessible to the user
 	 * @since 1.4.0
	 */
	List<ProjectModel> getProjectModels(UserModel user, boolean includeUsers);

	/**
	 * Returns the project model for the specified user.
	 *
	 * @param name a
	 * @param user a
	 * @return a project model, or null if it does not exist
 	 * @since 1.4.0
	 */
	ProjectModel getProjectModel(String name, UserModel user);

	/**
	 * Returns a project model for the Gitblit/system user.
	 *
	 * @param name a project name
	 * @return a project model or null if the project does not exist
 	 * @since 1.4.0
	 */
	ProjectModel getProjectModel(String name);

	/**
	 * Returns the list of project models that are referenced by the supplied
	 * repository model	list.  This is an alternative method exists to ensure
	 * Gitblit does not call getRepositoryModels(UserModel) twice in a request.
	 *
	 * @param repositoryModels a
	 * @param includeUsers a
	 * @return a list of project models
 	 * @since 1.4.0
	 */
	List<ProjectModel> getProjectModels(List<TaskEntity> repositoryModels, boolean includeUsers);

}