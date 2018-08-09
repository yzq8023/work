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

import com.github.wxiaoqi.security.api.vo.user.UserInfo;
import com.service.service.Constants;
import com.service.service.Constants.*;
import com.service.service.biz.TaskBiz;
import com.service.service.utils.ArrayUtils;
import com.service.service.utils.ModelUtils;
import com.service.service.utils.SecureRandom;
import com.service.service.utils.StringUtils;

import java.io.Serializable;
import java.security.Principal;
import java.util.*;

/**
 * UserModel是一个可序列化的模型类，它代表用户和用户
 * 权限。usermodel的实例也被用作
 * servlet主体。
 *
 * @author hollykunge
 *
 */
public class UserModel implements Principal, Serializable, Comparable<UserModel> {

	private static final long serialVersionUID = 1L;

	public static final UserModel ANONYMOUS = new UserModel();

	private static final SecureRandom RANDOM = new SecureRandom();

	/**
	 * 字段名称在EditUser页面中反射映射
	 */
	private String id;
	private String username;
	private String password;
	private String cookie;
	private String name;
	private String emailAddress;
	private String organizationalUnit;
	private String organization;
	private String locality;
	private String stateProvince;
	private String countryCode;
	private String description;
	private Date updTime;
	private boolean canAdmin;
	private boolean canFork;
	private boolean canCreate;
	private boolean excludeFromFederation;
	private boolean disabled;
	/**
	 * 与RPC客户端保持向后兼容
	 */
	@Deprecated
	private final Set<String> repositories = new HashSet<String>();
	private final Map<String, AccessPermission> permissions = new LinkedHashMap<String, AccessPermission>();
	private final Set<TeamModel> teams = new TreeSet<TeamModel>();
    private TaskBiz taskBiz;
	/**
	 * 非持久性字段
	 */
	private boolean isAuthenticated;
	private AccountType accountType;

	private UserPreferences userPreferences;

	public UserModel(String id) {
		this.id = id;
		this.isAuthenticated = true;
		this.accountType = AccountType.LOCAL;
		this.userPreferences = new UserPreferences(this.username);
	}

	public UserModel() {
		this.id = "$anonymous";
		this.isAuthenticated = false;
		this.accountType = AccountType.LOCAL;
		this.userPreferences = new UserPreferences(this.username);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getOrganizationalUnit() {
		return organizationalUnit;
	}

	public void setOrganizationalUnit(String organizationalUnit) {
		this.organizationalUnit = organizationalUnit;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public boolean isCanAdmin() {
		return canAdmin;
	}

	public void setCanAdmin(boolean canAdmin) {
		this.canAdmin = canAdmin;
	}

	public boolean isCanFork() {
		return canFork;
	}

	public void setCanFork(boolean canFork) {
		this.canFork = canFork;
	}

	public boolean isCanCreate() {
		return canCreate;
	}

	public void setCanCreate(boolean canCreate) {
		this.canCreate = canCreate;
	}

	public boolean isExcludeFromFederation() {
		return excludeFromFederation;
	}

	public void setExcludeFromFederation(boolean excludeFromFederation) {
		this.excludeFromFederation = excludeFromFederation;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Set<String> getRepositories() {
		return repositories;
	}

	public Map<String, AccessPermission> getPermissions() {
		return permissions;
	}

	public Set<TeamModel> getTeams() {
		return teams;
	}

	public void setAuthenticated(boolean authenticated) {
		isAuthenticated = authenticated;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public String getUserId() {
		return id;
	}

	public void setUserId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdTime() {
		return updTime;
	}

	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

	public boolean isLocalAccount() {
		return !Constants.EXTERNAL_ACCOUNT.equals(password)
				|| accountType == null
				|| accountType.isLocal();
	}

	/**
	 * 返回该用户的存储库权限列表，其中不包括从团队成员中继承的权限。
	 *
	 * @return the user's list of permissions
	 */
	public List<RegistrantAccessPermission> getRepositoryPermissions() {
		List<RegistrantAccessPermission> list = new ArrayList<RegistrantAccessPermission>();
		if (canAdmin()) {
			// user has REWIND access to all repositories
			return list;
		}
		for (Map.Entry<String, AccessPermission> entry : permissions.entrySet()) {
			String registrant = entry.getKey();
			AccessPermission ap = entry.getValue();
			String source = null;
			boolean mutable = true;
			PermissionType pType = PermissionType.EXPLICIT;
			if (isMyPersonalRepository(registrant)) {
				pType = PermissionType.OWNER;
				ap = AccessPermission.REWIND;
				mutable = false;
			} else if (StringUtils.findInvalidCharacter(registrant) != null) {
				// a regex will have at least 1 invalid character
				pType = PermissionType.REGEX;
				source = registrant;
			}
			list.add(new RegistrantAccessPermission(registrant, ap, pType, RegistrantType.REPOSITORY, source, mutable));
		}
		Collections.sort(list);

		// include immutable team permissions, being careful to preserve order
		Set<RegistrantAccessPermission> set = new LinkedHashSet<RegistrantAccessPermission>(list);
		for (TeamModel team : teams) {
			for (RegistrantAccessPermission teamPermission : team.getRepositoryPermissions()) {
				// we can not change an inherited team permission, though we can override
				teamPermission.registrantType = RegistrantType.REPOSITORY;
				teamPermission.permissionType = PermissionType.TEAM;
				teamPermission.source = String.valueOf(team.getId());
				teamPermission.mutable = false;
				set.add(teamPermission);
			}
		}
		return new ArrayList<RegistrantAccessPermission>(set);
	}

	/**
	 * 如果用户对这个存储库有任何类型的指定访问权限，则返回true。
	 *
	 * @param name
	 * @return true if user has a specified access permission for the repository
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
	 * 如果用户对这个存储库有明确指定的访问权限，则返回true。
	 *
	 * @param name
	 * @return if the user has an explicitly specified access permission
	 */
	public boolean hasExplicitRepositoryPermission(String name) {
		String repository = AccessPermission.repositoryFromRole(name).toLowerCase();
		return permissions.containsKey(repository);
	}

	/**
	 * 如果用户的团队成员指定了这个存储库的访问权限，则返回true。
	 *
	 * @param name
	 * @return if the user's team memberships specifi an access permission
	 */
	public boolean hasTeamRepositoryPermission(String name) {
		if (teams != null) {
			for (TeamModel team : teams) {
				if (team.hasRepositoryPermission(name)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 为团队添加仓库权限
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

	public AccessPermission removeRepositoryPermission(String name) {
		String repository = AccessPermission.repositoryFromRole(name).toLowerCase();
		repositories.remove(repository);
		return permissions.remove(repository);
	}

	public void setRepositoryPermission(String repository, AccessPermission permission) {
		if (permission == null) {
			// remove the permission
			permissions.remove(repository.toLowerCase());
		} else {
			// set the new permission
			permissions.put(repository.toLowerCase(), permission);
		}
	}

	public RegistrantAccessPermission getRepositoryPermission(TaskEntity repository) {
		RegistrantAccessPermission ap = new RegistrantAccessPermission();
		ap.registrant = id;
		ap.registrantType = RegistrantType.USER;
		ap.permission = AccessPermission.NONE;
		ap.mutable = false;

		// 确定存储库的最大权限
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

		// administrator
		if (canAdmin()) {
			ap.permissionType = PermissionType.ADMINISTRATOR;
			if (AccessPermission.REWIND.atMost(maxPermission)) {
				ap.permission = AccessPermission.REWIND;
			} else {
				ap.permission = maxPermission;
			}
			if (!canAdmin) {
				// administator permission from team membership
				for (TeamModel team : teams) {
					if (team.isCanAdmin()) {
						ap.source = String.valueOf(team.getId());
						break;
					}
				}
			}
			return ap;
		}

		// repository owner - either specified owner or personal repository
		if (repository.isOwner(id) || repository.isUsersPersonalRepository(id)) {
			ap.permissionType = PermissionType.OWNER;
			if (AccessPermission.REWIND.atMost(maxPermission)) {
				ap.permission = AccessPermission.REWIND;
			} else {
				ap.permission = maxPermission;
			}
			return ap;
		}

		if (AuthorizationControl.AUTHENTICATED.equals(repository.getAuthorizationControl()) && isAuthenticated) {
			// AUTHENTICATED is a shortcut for authorizing all logged-in users RW+ access
			if (AccessPermission.REWIND.atMost(maxPermission)) {
				ap.permission = AccessPermission.REWIND;
			} else {
				ap.permission = maxPermission;
			}
			return ap;
		}

		// explicit user permission OR user regex match is used
		// if that fails, then the best team permission is used
		if (permissions.containsKey(repository.getTaskName().toLowerCase())) {
			// exact repository permission specified, use it
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

		// try to find a team match
		for (TeamModel team : teams) {
			RegistrantAccessPermission p = team.getRepositoryPermission(repository);
			if (p.permission.atMost(maxPermission) && p.permission.exceeds(ap.permission) && PermissionType.ANONYMOUS != p.permissionType) {
				// use highest team permission that is not an implicit permission
				ap.permission = p.permission;
				ap.source = String.valueOf(team.getId());
				ap.permissionType = PermissionType.TEAM;
			}
		}

		// still no explicit, regex, or team match, check for implicit permissions
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

	public boolean canView(TaskEntity repository, String ref) {
		// Default UserModel doesn't implement ref-level security.
		// Other Realms (i.e. Gerrit) may override this method.
		return canView(repository);
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

	public boolean canFork(TaskEntity repository) {
		if (repository.isUsersPersonalRepository(id)) {
			// can not fork your own repository
			return false;
		}
		if (canAdmin() || repository.isOwner(id)) {
			return true;
		}
		if (!repository.isAllowForks()) {
			return false;
		}
		if (!isAuthenticated || !canFork()) {
			return false;
		}
		return canClone(repository);
	}

	public boolean canDelete(TaskEntity model) {
		return canAdmin() || model.isUsersPersonalRepository(id);
	}

	public boolean canEdit(TaskEntity model) {
		return canAdmin() || model.isUsersPersonalRepository(id) || model.isOwner(id);
	}

	public boolean canEdit(TicketModel ticket, TaskEntity repository) {
		 return isAuthenticated() &&
				 (canPush(repository)
				 || (ticket != null && id.equals(ticket.responsible))
				 || (ticket != null && id.equals(ticket.createdBy)));
	}

	public boolean canAdmin(TicketModel ticket, TaskEntity repository) {
		 return isAuthenticated() &&
				 (canPush(repository)
				 || ticket != null && id.equals(ticket.responsible));
	}

	public boolean canReviewPatchset(TaskEntity model) {
		return isAuthenticated() && canClone(model);
	}

	public boolean canApprovePatchset(TaskEntity model) {
		return isAuthenticated() && canPush(model);
	}

	public boolean canVetoPatchset(TaskEntity model) {
		return isAuthenticated() && canPush(model);
	}

	/**
	 * 如果用户有fork特权，或者由于团队成员的身份，用户拥有fork特权，则返回true。
	 *
	 * @return true if the user can fork
	 */
	public boolean canFork() {
		if (canFork) {
			return true;
		}
		if (!ArrayUtils.isEmpty(teams)) {
			for (TeamModel team : teams) {
				if (team.isCanFork()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 如果用户具有管理员权限，或者由于团队成员的身份而具有管理员权限，则返回true。
	 *
	 * @return true if the user can admin
	 */
	public boolean canAdmin() {
		if (canAdmin) {
			return true;
		}
		if (!ArrayUtils.isEmpty(teams)) {
			for (TeamModel team : teams) {
				if (team.isCanAdmin()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 如果用户有创建特权，或者用户由于团队成员身份而具有创建特权，那么这将返回true。
	 *
	 * @return true if the user can admin
	 */
	public boolean canCreate() {
		if (canCreate) {
			return true;
		}
		if (!ArrayUtils.isEmpty(teams)) {
			for (TeamModel team : teams) {
				if (team.isCanCreate()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 如果允许用户创建指定的存储库，则返回true。
	 *
	 * @param repository
	 * @return true if the user can create the repository
	 */
	public boolean canCreate(String repository) {
		if (canAdmin()) {
			// admins can create any repository
			return true;
		}
		if (canCreate()) {
			String projectPath = StringUtils.getFirstPathElement(repository);
			if (!StringUtils.isEmpty(projectPath) && projectPath.equalsIgnoreCase(getPersonalPath())) {
				// personal repository
				return true;
			}
		}
		return false;
	}

	/**
	 * 如果允许用户管理指定的存储库，则返回true
	 *
	 * @param repo
	 * @return true if the user can administer the repository
	 */
	public boolean canAdmin(TaskEntity repo) {
		return canAdmin() || repo.isOwner(id) || isMyPersonalRepository(repo.getTaskName());
	}

	public boolean isAuthenticated() {
		return !UserModel.ANONYMOUS.equals(this) && isAuthenticated;
	}

	public boolean isTeamMember(String teamname) {
		for (TeamModel team : teams) {
			//TODO 在比较不同类型数据时可能会有问题
			if (team.getId().equals(teamname)) {
				return true;
			}
		}
		return false;
	}

	public TeamModel getTeam(String teamname) {
		if (teams == null) {
			return null;
		}
		for (TeamModel team : teams) {
			if (team.getId().equals(teamname)) {
				return team;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return id;
	}

	public String getDisplayName() {
		if (StringUtils.isEmpty(name)) {
			return username;
		}
		return name;
	}

	public String getPersonalPath() {
		return ModelUtils.getPersonalPath(id);
	}

	public UserPreferences getPreferences() {
		return userPreferences;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof UserModel) {
			return id.equals(((UserModel) o).id);
		}
		return false;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public int compareTo(UserModel o) {
		return id.compareTo(o.id);
	}

	/**
	 * 如果名称/电子邮件对匹配这个用户帐户，返回true。
	 *
	 * @param name
	 * @param email
	 * @return true, if the name and email address match this account
	 */
	public boolean is(String name, String email) {
		// at a minimum a username or display name AND email address must be supplied
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(email)) {
			return false;
		}
		boolean nameVerified = name.equalsIgnoreCase(id) || name.equalsIgnoreCase(getDisplayName());
		boolean emailVerified = false;
		if (StringUtils.isEmpty(emailAddress)) {
			// user account has not specified an email address
			// fail
			emailVerified = false;
		} else {
			// user account has specified an email address
			emailVerified = email.equalsIgnoreCase(emailAddress);
		}
		return nameVerified && emailVerified;
	}

	public boolean isMyPersonalRepository(String repository) {
		String projectPath = StringUtils.getFirstPathElement(repository);
		return !StringUtils.isEmpty(projectPath) && projectPath.equalsIgnoreCase(getPersonalPath());
	}

	public String createCookie() {
		return StringUtils.getSHA1(RANDOM.randomBytes(32));
	}

	public boolean isAdmin(){
	    return taskBiz.isAdmin(userId,getid);
    }

    public boolean isOwner(){
        return taskBiz.isOwner(userId,getid);
    }
}
