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
package com.service.service.entity;

import com.service.service.Constants.*;
import com.service.service.utils.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * TeamModel is a serializable model class that represents a group of users and
 * a list of accessible repositories.
 *
 * @author James Moger
 *
 */
public class TeamModel implements Serializable, Comparable<TeamModel> {

	private static final long serialVersionUID = 1L;

	// field names are reflectively mapped in EditTeam page
	public String name;
	public boolean canAdmin;
	public boolean canFork;
	public boolean canCreate;
	public AccountType accountType;
	public final Set<String> users = new HashSet<String>();
	// retained for backwards-compatibility with RPC clients
	@Deprecated
	public final Set<String> repositories = new HashSet<String>();
	public final Map<String, AccessPermission> permissions = new LinkedHashMap<String, AccessPermission>();
	public final Set<String> mailingLists = new HashSet<String>();
	public final List<String> preReceiveScripts = new ArrayList<String>();
	public final List<String> postReceiveScripts = new ArrayList<String>();

	public TeamModel(String name) {
		this.name = name;
		this.accountType = AccountType.LOCAL;
	}

	/**
	 * Returns a list of repository permissions for this team.
	 *
	 * @return the team's list of permissions
	 */
	public List<RegistrantAccessPermission> getRepositoryPermissions() {
		List<RegistrantAccessPermission> list = new ArrayList<RegistrantAccessPermission>();
		if (canAdmin) {
			// team has REWIND access to all repositories
			return list;
		}
		for (Map.Entry<String, AccessPermission> entry : permissions.entrySet()) {
			String registrant = entry.getKey();
			String source = null;
			boolean editable = true;
			PermissionType pType = PermissionType.EXPLICIT;
			if (StringUtils.findInvalidCharacter(registrant) != null) {
				// a regex will have at least 1 invalid character
				pType = PermissionType.REGEX;
				source = registrant;
			}
			list.add(new RegistrantAccessPermission(registrant, entry.getValue(), pType, RegistrantType.REPOSITORY, source, editable));
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * Returns true if the team has any type of specified access permission for
	 * this repository.
	 *
	 * @param name
	 * @return true if team has a specified access permission for the repository
	 */
	public boolean hasRepositoryPermission(String name) {
		String repository = AccessPermission.repositoryFromRole(name).toLowerCase();
		if (permissions.containsKey(repository)) {
			// exact repository permission specified
			return true;
		} else {
			// search for regex permission match
			for (String key : permissions.keySet()) {
				if (name.matches(key)) {
					AccessPermission p = permissions.get(key);
					if (p != null) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if the team has an explicitly specified access permission for
	 * this repository.
	 *
	 * @param name
	 * @return if the team has an explicitly specified access permission
	 */
	public boolean hasExplicitRepositoryPermission(String name) {
		String repository = AccessPermission.repositoryFromRole(name).toLowerCase();
		return permissions.containsKey(repository);
	}

	/**
	 * Adds a repository permission to the team.
	 * <p>
	 * Role may be formatted as:
	 * <ul>
	 * <li> myrepo.git <i>(this is implicitly RW+)</i>
	 * <li> RW+:myrepo.git
	 * </ul>
	 * @param role
	 */
	public void addRepositoryPermission(String role) {
		AccessPermission permission = AccessPermission.permissionFromRole(role);
		String repository = AccessPermission.repositoryFromRole(role).toLowerCase();
		repositories.add(repository);
		permissions.put(repository, permission);
	}

	public void addRepositoryPermissions(Collection<String> roles) {
		for (String role:roles) {
			addRepositoryPermission(role);
		}
	}

	public AccessPermission removeRepositoryPermission(String name) {
		String repository = AccessPermission.repositoryFromRole(name).toLowerCase();
		repositories.remove(repository);
		return permissions.remove(repository);
	}

	public void setRepositoryPermission(String repository, AccessPermission permission) {
		if (permission == null) {
			// remove the permission
			permissions.remove(repository.toLowerCase());
			repositories.remove(repository.toLowerCase());
		} else {
			// set the new permission
			permissions.put(repository.toLowerCase(), permission);
			repositories.add(repository.toLowerCase());
		}
	}

	public RegistrantAccessPermission getRepositoryPermission(TaskEntity repository) {
		RegistrantAccessPermission ap = new RegistrantAccessPermission();
		ap.registrant = name;
		ap.registrantType = RegistrantType.TEAM;
		ap.permission = AccessPermission.NONE;
		ap.mutable = false;

		// determine maximum permission for the repository
		final AccessPermission maxPermission =
				(repository.isFrozen() || !repository.isBare() || repository.isMirror()) ?
						AccessPermission.CLONE : AccessPermission.REWIND;

		if (AccessRestrictionType.NONE.equals(repository.getAccessRestriction())) {
			// anonymous rewind
			ap.permissionType = PermissionType.ANONYMOUS;
			if (AccessPermission.REWIND.atMost(maxPermission)) {
				ap.permission = AccessPermission.REWIND;
			} else {
				ap.permission = maxPermission;
			}
			return ap;
		}

		if (canAdmin) {
			ap.permissionType = PermissionType.ADMINISTRATOR;
			if (AccessPermission.REWIND.atMost(maxPermission)) {
				ap.permission = AccessPermission.REWIND;
			} else {
				ap.permission = maxPermission;
			}
			return ap;
		}

		if (permissions.containsKey(repository.getTaskName().toLowerCase())) {
			// exact repository permission specified
			AccessPermission p = permissions.get(repository.getTaskName().toLowerCase());
			if (p != null && repository.getAccessRestriction().isValidPermission(p)) {
				ap.permissionType = PermissionType.EXPLICIT;
				if (p.atMost(maxPermission)) {
					ap.permission = p;
				} else {
					ap.permission = maxPermission;
				}
				ap.mutable = true;
				return ap;
			}
		} else {
			// search for case-insensitive regex permission match
			for (String key : permissions.keySet()) {
				if (StringUtils.matchesIgnoreCase(repository.getTaskName(), key)) {
					AccessPermission p = permissions.get(key);
					if (p != null && repository.getAccessRestriction().isValidPermission(p)) {
						// take first match
						ap.permissionType = PermissionType.REGEX;
						if (p.atMost(maxPermission)) {
							ap.permission = p;
						} else {
							ap.permission = maxPermission;
						}
						ap.source = key;
						return ap;
					}
				}
			}
		}

		// still no explicit or regex, check for implicit permissions
		if (AccessPermission.NONE == ap.permission) {
			switch (repository.getAccessRestriction()) {
			case VIEW:
				// no implicit permissions possible
				break;
			case CLONE:
				// implied view permission
				ap.permission = AccessPermission.VIEW;
				ap.permissionType = PermissionType.ANONYMOUS;
				break;
			case PUSH:
				// implied clone permission
				ap.permission = AccessPermission.CLONE;
				ap.permissionType = PermissionType.ANONYMOUS;
				break;
			case NONE:
				// implied REWIND or CLONE
				ap.permission = maxPermission;
				ap.permissionType = PermissionType.ANONYMOUS;
				break;
			}
		}

		return ap;
	}

	protected boolean canAccess(TaskEntity repository, AccessRestrictionType ifRestriction, AccessPermission requirePermission) {
		if (repository.getAccessRestriction().atLeast(ifRestriction)) {
			RegistrantAccessPermission ap = getRepositoryPermission(repository);
			return ap.permission.atLeast(requirePermission);
		}
		return true;
	}

	public boolean canView(TaskEntity repository) {
		return canAccess(repository, AccessRestrictionType.VIEW, AccessPermission.VIEW);
	}

	public boolean canClone(TaskEntity repository) {
		return canAccess(repository, AccessRestrictionType.CLONE, AccessPermission.CLONE);
	}

	public boolean canPush(TaskEntity repository) {
		if (repository.isFrozen()) {
			return false;
		}
		return canAccess(repository, AccessRestrictionType.PUSH, AccessPermission.PUSH);
	}

	public boolean canCreateRef(TaskEntity repository) {
		if (repository.isFrozen()) {
			return false;
		}
		return canAccess(repository, AccessRestrictionType.PUSH, AccessPermission.CREATE);
	}

	public boolean canDeleteRef(TaskEntity repository) {
		if (repository.isFrozen()) {
			return false;
		}
		return canAccess(repository, AccessRestrictionType.PUSH, AccessPermission.DELETE);
	}

	public boolean canRewindRef(TaskEntity repository) {
		if (repository.isFrozen()) {
			return false;
		}
		return canAccess(repository, AccessRestrictionType.PUSH, AccessPermission.REWIND);
	}

	public boolean hasUser(String name) {
		return users.contains(name.toLowerCase());
	}

	public void addUser(String name) {
		users.add(name.toLowerCase());
	}

	public void addUsers(Collection<String> names) {
		for (String name:names) {
			users.add(name.toLowerCase());
		}
	}

	public void removeUser(String name) {
		users.remove(name.toLowerCase());
	}

	public void addMailingLists(Collection<String> addresses) {
		for (String address:addresses) {
			mailingLists.add(address.toLowerCase());
		}
	}

	public boolean isLocalTeam() {
		return accountType.isLocal();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(TeamModel o) {
		return name.compareTo(o.name);
	}
}
