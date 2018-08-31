package com.service.service.entity;

import com.service.service.Constants.*;
import com.service.service.utils.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * TeamModel is a serializable model class that represents a group of users and
 * a list of accessible repositories.
 *
 * @author James Moger
 *
 */
@Table(name = "team")
public class TeamModel implements Serializable, Comparable<TeamModel> {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private String name;
	@Column(name = "lower_name")
	private String lowerName;

	@Column(name = "num_task")
	private Integer numTask;

	@Column(name = "num_members")
	private Integer numMembers;

	@Column(name = "crt_time")
	private Date crtTime;

	@Column(name = "crt_user")
	private String crtUser;

	@Column(name = "crt_name")
	private String crtName;

	@Column(name = "crt_host")
	private String crtHost;

	@Column(name = "upd_time")
	private Date updTime;

	@Column(name = "upd_user")
	private String updUser;

	@Column(name = "upd_name")
	private String updName;

	@Column(name = "upd_host")
	private String updHost;

	private String description;

	@Column(name = "can_admin")
	private boolean canAdmin;

	@Column(name = "can_fork")
	private boolean canFork;

	@Column(name = "can_create")
	private boolean canCreate;
	@Transient
	private AccountType accountType;
	@Transient
	private final Set<String> users = new HashSet<String>();
	/**
	 * 与RPC客户端保持向后兼容
	 */
	@Deprecated
	@Transient
	private final Set<String> repositories = new HashSet<String>();
	@Transient
	private final Map<String, AccessPermission> permissions = new LinkedHashMap<String, AccessPermission>();
	@Transient
	private final Set<String> mailingLists = new HashSet<String>();
	@Transient
	private final List<String> preReceiveScripts = new ArrayList<String>();
	@Transient
	private final List<String> postReceiveScripts = new ArrayList<String>();

	public TeamModel(String name) {
		this.name = name;
		this.accountType = AccountType.LOCAL;
	}

	public TeamModel() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLowerName() {
		return lowerName;
	}

	public void setLowerName(String lowerName) {
		this.lowerName = lowerName;
	}

	public Integer getNumTask() {
		return numTask;
	}

	public void setNumTask(Integer numTask) {
		this.numTask = numTask;
	}

	public Integer getNumMembers() {
		return numMembers;
	}

	public void setNumMembers(Integer numMembers) {
		this.numMembers = numMembers;
	}

	public Date getCrtTime() {
		return crtTime;
	}

	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}

	public String getCrtUser() {
		return crtUser;
	}

	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}

	public String getCrtName() {
		return crtName;
	}

	public void setCrtName(String crtName) {
		this.crtName = crtName;
	}

	public String getCrtHost() {
		return crtHost;
	}

	public void setCrtHost(String crtHost) {
		this.crtHost = crtHost;
	}

	public Date getUpdTime() {
		return updTime;
	}

	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

	public String getUpdUser() {
		return updUser;
	}

	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}

	public String getUpdName() {
		return updName;
	}

	public void setUpdName(String updName) {
		this.updName = updName;
	}

	public String getUpdHost() {
		return updHost;
	}

	public void setUpdHost(String updHost) {
		this.updHost = updHost;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public Set<String> getUsers() {
		return users;
	}

	public Set<String> getRepositories() {
		return repositories;
	}

	public Map<String, AccessPermission> getPermissions() {
		return permissions;
	}

	public Set<String> getMailingLists() {
		return mailingLists;
	}

	public List<String> getPreReceiveScripts() {
		return preReceiveScripts;
	}

	public List<String> getPostReceiveScripts() {
		return postReceiveScripts;
	}

	/**
	 * 返回本团队的存储库权限列表。
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
	 * 如果团队对这个存储库有任何类型的指定访问权限，则返回true。
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
	 * 如果团队对这个存储库有明确指定的访问权限，则返回true。
	 *
	 * @param name
	 * @return if the team has an explicitly specified access permission
	 */
	public boolean hasExplicitRepositoryPermission(String name) {
		String repository = AccessPermission.repositoryFromRole(name).toLowerCase();
		return permissions.containsKey(repository);
	}

	/**
	 * 向团队添加一个存储库权限。
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

	public void addUser(String id) {
		users.add(id);
	}

	public void addUsers(Collection<String> ids) {
		for (String id:ids) {
			users.add(id);
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
