package com.service.service.managers;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.service.service.Constants;
import com.service.service.IStoredSettings;
import com.service.service.entity.*;
import com.service.service.exception.GitBlitException;
import com.service.service.extensions.RepositoryLifeCycleListener;
import com.service.service.tickets.ITicketService;
import com.service.service.utils.*;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.RefSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.fortsoft.pf4j.PluginState;
import ro.fortsoft.pf4j.PluginWrapper;
import ro.fortsoft.pf4j.Version;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.*;

@Component
public class WorkHubManager implements IWorkHub {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final ObjectCache<Collection<GitClientApplication>> clientApplications = new ObjectCache<Collection<GitClientApplication>>();
    protected final IStoredSettings settings;
    protected final IRuntimeManager runtimeManager;
    protected final INotificationManager notificationManager;
    protected final IUserManager userManager;
    protected final IAuthenticationManager authenticationManager;
    protected final IRepositoryManager repositoryManager;
    protected final IProjectManager projectManager;
    protected final IFilestoreManager filestoreManager;
    protected final IPluginManager pluginManager;
    protected final ITicketService ticketServiceProvider;

    @Autowired
    public WorkHubManager(IRuntimeManager runtimeManager,
                          INotificationManager notificationManager,
                          IUserManager userManager,
                          IAuthenticationManager authenticationManager,
                          IRepositoryManager repositoryManager,
                          IProjectManager projectManager,
                          IFilestoreManager filestoreManager,
                          IPluginManager pluginManager,
                          ITicketService ticketServiceProvider) {
        this.settings = runtimeManager.getSettings();
        this.runtimeManager = runtimeManager;
        this.notificationManager = notificationManager;
        this.userManager = userManager;
        this.authenticationManager = authenticationManager;
        this.repositoryManager = repositoryManager;
        this.projectManager = projectManager;
        this.filestoreManager = filestoreManager;
        this.pluginManager = pluginManager;
        this.ticketServiceProvider = ticketServiceProvider;
        this.start();
    }


    @Override
    public WorkHubManager start() {
        loadSettingModels(runtimeManager.getSettingsModel());
        return this;
    }

    @Override
    public WorkHubManager stop() {
        return this;
    }

    /**
     * Parse the properties file and aggregate all the comments by the setting
     * key. A setting model tracks the current value, the default value, the
     * description of the setting and and directives about the setting.
     *
     * @return Map<String               ,                               SettingModel>
     */
    private void loadSettingModels(ServerSettings settingsModel) {
        try {
            // Read bundled Gitblit properties to extract setting descriptions.
            // This copy is pristine and only used for populating the setting
            // models map.
            InputStream is = WorkHubManager.class.getResourceAsStream("/defaults.properties");
            BufferedReader propertiesReader = new BufferedReader(new InputStreamReader(is));
            StringBuilder description = new StringBuilder();
            SettingModel setting = new SettingModel();
            String line = null;
            while ((line = propertiesReader.readLine()) != null) {
                if (line.length() == 0) {
                    description.setLength(0);
                    setting = new SettingModel();
                } else {
                    if (line.charAt(0) == '#') {
                        if (line.length() > 1) {
                            String text = line.substring(1).trim();
                            if (SettingModel.CASE_SENSITIVE.equals(text)) {
                                setting.caseSensitive = true;
                            } else if (SettingModel.RESTART_REQUIRED.equals(text)) {
                                setting.restartRequired = true;
                            } else if (SettingModel.SPACE_DELIMITED.equals(text)) {
                                setting.spaceDelimited = true;
                            } else if (text.startsWith(SettingModel.SINCE)) {
                                try {
                                    setting.since = text.split(" ")[1];
                                } catch (Exception e) {
                                    setting.since = text;
                                }
                            } else {
                                description.append(text);
                                description.append('\n');
                            }
                        }
                    } else {
                        String[] kvp = line.split("=", 2);
                        String key = kvp[0].trim();
                        setting.name = key;
                        setting.defaultValue = kvp[1].trim();
                        setting.currentValue = setting.defaultValue;
                        setting.description = description.toString().trim();
                        settingsModel.add(setting);
                        description.setLength(0);
                        setting = new SettingModel();
                    }
                }
            }
            propertiesReader.close();
        } catch (NullPointerException e) {
            logger.error("Failed to find classpath resource 'defaults.properties'");
        } catch (IOException e) {
            logger.error("Failed to load classpath resource 'defaults.properties'");
        }
    }

    private Collection<GitClientApplication> readClientApplications(InputStream is) {
        try {
            Type type = new TypeToken<Collection<GitClientApplication>>() {
            }.getType();
            InputStreamReader reader = new InputStreamReader(is);
            Gson gson = JsonUtils.gson();
            Collection<GitClientApplication> links = gson.fromJson(reader, type);
            return links;
        } catch (JsonIOException e) {
            logger.error("Error deserializing client applications!", e);
        } catch (JsonSyntaxException e) {
            logger.error("Error deserializing client applications!", e);
        }
        return null;
    }

    /****************************************************************************************
     *                                     IWorkHub start                                   *
     ****************************************************************************************/
    @Override
    public void addUser(UserModel user) throws GitBlitException {
        if (!userManager.updateUserModel(user)) {
            throw new GitBlitException("Failed to add user!");
        }
    }

    @Override
    public void reviseUser(String username, UserModel user) throws GitBlitException {
        if (!username.equalsIgnoreCase(user.getUserId())) {
            if (userManager.getUserModel(user.getUserId()) != null) {
                throw new GitBlitException(MessageFormat.format(
                        "Failed to rename ''{0}'' because ''{1}'' already exists.", username,
                        user.getUserId()));
            }

            // rename repositories and owner fields for all repositories
            for (TaskEntity model : repositoryManager.getRepositoryModels(user)) {
                if (model.isUsersPersonalRepository(username)) {
                    // personal repository
                    model.addOwner(user.getUserId());
                    String oldRepositoryName = model.getTaskName();
                    model.setTaskName(user.getPersonalPath() + model.getTaskName().substring(model.getProjectPath().length()));
                    model.setProjectPath(user.getPersonalPath());
                    repositoryManager.updateRepositoryModel(oldRepositoryName, model, false);
                } else if (model.isOwner(username)) {
                    // common/shared repo
                    model.addOwner(user.getUserId());
                    repositoryManager.updateRepositoryModel(model.getTaskName(), model, false);
                }
            }
        }
        if (!userManager.updateUserModel(username, user)) {
            throw new GitBlitException("Failed to update user!");
        }
    }

    @Override
    public void addTeam(TeamModel team) throws GitBlitException {
        if (!userManager.updateTeamModel(team)) {
            throw new GitBlitException("Failed to add team!");
        }
    }

    @Override
    public void reviseTeam(String teamname, TeamModel team) throws GitBlitException {
        if (!teamname.equals(team.getId())) {
            if (userManager.getTeamModel(String.valueOf(team.getId())) != null) {
                throw new GitBlitException(MessageFormat.format(
                        "Failed to rename ''{0}'' because ''{1}'' already exists.", teamname,
                        team.getId()));
            }
        }
        if (!userManager.updateTeamModel(teamname, team)) {
            throw new GitBlitException("Failed to update team!");
        }
    }

    /**
     * @param repository
     * @param user
     * @return 返回仓库的fork model
     * @throws GitBlitException
     * @des 创建指定存储库的个人分支。默认情况下, 克隆是受限视图, 源存储库的所有者被授予对该克隆的访问权限。
     */
    @Override
    public TaskEntity fork(TaskEntity repository, UserModel user) throws GitBlitException {
        String cloneName = MessageFormat.format("{0}/{1}.git", user.getPersonalPath(), StringUtils.stripDotGit(StringUtils.getLastPathElement(repository.getTaskName())));
        String fromUrl = MessageFormat.format("file://{0}/{1}", repositoryManager.getRepositoriesFolder().getAbsolutePath(), repository.getTaskName());

        // clone the repository
        try {
            Repository canonical = getRepository(repository.getTaskName());
            File folder = new File(repositoryManager.getRepositoriesFolder(), cloneName);
            CloneCommand clone = new CloneCommand();
            clone.setBare(true);

            // fetch branches with exclusions
            Collection<Ref> branches = canonical.getRefDatabase().getRefs(Constants.R_HEADS).values();
            List<String> branchesToClone = new ArrayList<String>();
            for (Ref branch : branches) {
                String name = branch.getName();
                if (name.startsWith(Constants.R_TICKET)) {
                    // exclude ticket branches
                    continue;
                }
                branchesToClone.add(name);
            }
            clone.setBranchesToClone(branchesToClone);
            clone.setURI(fromUrl);
            clone.setDirectory(folder);
            Git git = clone.call();

            // fetch tags
            FetchCommand fetch = git.fetch();
            fetch.setRefSpecs(new RefSpec("+refs/tags/*:refs/tags/*"));
            fetch.call();

            git.getRepository().close();
        } catch (Exception e) {
            throw new GitBlitException(e);
        }

        // create a Gitblit repository model for the clone
        TaskEntity cloneModel = repository.cloneAs(cloneName);
        // owner has REWIND/RW+ permissions
        cloneModel.addOwner(user.getUserId());

        // ensure initial access restriction of the fork
        // is not lower than the source repository  (issue-495/ticket-167)
        if (repository.getAccessRestriction().exceeds(cloneModel.getAccessRestriction())) {
            cloneModel.setAccessRestriction(repository.getAccessRestriction());
        }

        repositoryManager.updateRepositoryModel(cloneName, cloneModel, false);

        // add the owner of the source repository to the clone's access list
        if (!ArrayUtils.isEmpty(repository.getOwners())) {
            for (String owner : repository.getOwners()) {
                UserModel originOwner = userManager.getUserModel(owner);
                if (originOwner != null && !originOwner.canClone(cloneModel)) {
                    // origin owner can't yet clone fork, grant explicit clone access
                    originOwner.setRepositoryPermission(cloneName, Constants.AccessPermission.CLONE);
                    reviseUser(originOwner.getUserId(), originOwner);
                }
            }
        }

        // grant origin's user list clone permission to fork
        List<String> users = repositoryManager.getRepositoryUsers(repository);
        List<UserModel> cloneUsers = new ArrayList<UserModel>();
        for (String name : users) {
            if (!name.equalsIgnoreCase(user.getUserId())) {
                UserModel cloneUser = userManager.getUserModel(name);
                if (cloneUser.canClone(repository) && !cloneUser.canClone(cloneModel)) {
                    // origin user can't yet clone fork, grant explicit clone access
                    cloneUser.setRepositoryPermission(cloneName, Constants.AccessPermission.CLONE);
                }
                cloneUsers.add(cloneUser);
            }
        }
        userManager.updateUserModels(cloneUsers);

        // grant origin's team list clone permission to fork
        List<String> teams = repositoryManager.getRepositoryTeams(repository);
        List<TeamModel> cloneTeams = new ArrayList<TeamModel>();
        for (String name : teams) {
            TeamModel cloneTeam = userManager.getTeamModel(name);
            if (cloneTeam.canClone(repository) && !cloneTeam.canClone(cloneModel)) {
                // origin team can't yet clone fork, grant explicit clone access
                cloneTeam.setRepositoryPermission(cloneName, Constants.AccessPermission.CLONE);
            }
            cloneTeams.add(cloneTeam);
        }
        userManager.updateTeamModels(cloneTeams);

        // add this clone to the cached model
        repositoryManager.addToCachedRepositoryList(cloneModel);

        if (pluginManager != null) {
            for (RepositoryLifeCycleListener listener : pluginManager.getExtensions(RepositoryLifeCycleListener.class)) {
                try {
                    listener.onFork(repository, cloneModel);
                } catch (Throwable t) {
                    logger.error(String.format("failed to call plugin onFork %s", repository.getTaskName()), t);
                }
            }
        }
        return cloneModel;
    }

    @Override
    public Collection<GitClientApplication> getClientApplications() {
        // prefer user definitions, if they exist
        File userDefs = new File(runtimeManager.getBaseFolder(), "clientapps.json");
        if (userDefs.exists()) {
            Date lastModified = new Date(userDefs.lastModified());
            if (clientApplications.hasCurrent("user", lastModified)) {
                return clientApplications.getObject("user");
            } else {
                // (re)load user definitions
                try {
                    InputStream is = new FileInputStream(userDefs);
                    Collection<GitClientApplication> clients = readClientApplications(is);
                    is.close();
                    if (clients != null) {
                        clientApplications.updateObject("user", lastModified, clients);
                        return clients;
                    }
                } catch (IOException e) {
                    logger.error("Failed to deserialize " + userDefs.getAbsolutePath(), e);
                }
            }
        }

        // no user definitions, use system definitions
        if (!clientApplications.hasCurrent("system", new Date(0))) {
            try {
                InputStream is = WorkHubManager.class.getResourceAsStream("/clientapps.json");
                Collection<GitClientApplication> clients = readClientApplications(is);
                is.close();
                if (clients != null) {
                    clientApplications.updateObject("system", new Date(0), clients);
                }
            } catch (IOException e) {
                logger.error("Failed to deserialize clientapps.json resource!", e);
            }
        }

        return clientApplications.getObject("system");
    }

    @Override
    public ITicketService getTicketService() {
        return null;
    }
    /****************************************************************************************
     *                                     IWorkHub end                                     *
     ****************************************************************************************/

    /****************************************************************************************
     *                                    IStoreSetting start                               *
     ****************************************************************************************/
    public boolean getBoolean(String key, boolean defaultValue) {
        return runtimeManager.getSettings().getBoolean(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return runtimeManager.getSettings().getString(key, defaultValue);
    }

    public int getInteger(String key, int defaultValue) {
        return runtimeManager.getSettings().getInteger(key, defaultValue);
    }

    public List<String> getStrings(String key) {
        return runtimeManager.getSettings().getStrings(key);
    }
    /****************************************************************************************
     *                                    IStoreSetting end                                 *
     ****************************************************************************************/

    /****************************************************************************************
     *                                    RuntimeManager start                              *
     ****************************************************************************************/
    @Override
    public void setBaseFolder(File folder) {
        runtimeManager.setBaseFolder(folder);
    }

    @Override
    public File getBaseFolder() {
        return runtimeManager.getBaseFolder();
    }

    @Override
    public TimeZone getTimezone() {
        return runtimeManager.getTimezone();
    }

    @Override
    public Locale getLocale() {
        return runtimeManager.getLocale();
    }

    @Override
    public boolean isDebugMode() {
        return runtimeManager.isDebugMode();
    }

    @Override
    public Date getBootDate() {
        return runtimeManager.getBootDate();
    }

    @Override
    public ServerStatus getStatus() {
        return runtimeManager.getStatus();
    }

    @Override
    public ServerSettings getSettingsModel() {
        return runtimeManager.getSettingsModel();
    }

    @Override
    public File getFileOrFolder(String key, String defaultFileOrFolder) {
        return runtimeManager.getFileOrFolder(key, defaultFileOrFolder);
    }

    @Override
    public File getFileOrFolder(String fileOrFolder) {
        return runtimeManager.getFileOrFolder(fileOrFolder);
    }

    @Override
    public IStoredSettings getSettings() {
        return runtimeManager.getSettings();
    }

    @Override
    public boolean updateSettings(Map<String, String> updatedSettings) {
        return runtimeManager.updateSettings(updatedSettings);
    }

    @Override
    public XssFilter getXssFilter() {
        return runtimeManager.getXssFilter();
    }

    /****************************************************************************************
     *                                    RuntimeManager end                                *
     ****************************************************************************************/

    /****************************************************************************************
     *                                 NotifitionManager start                              *
     ****************************************************************************************/

    @Override
    public boolean isSendingMail() {
        return notificationManager.isSendingMail();
    }

    @Override
    public void sendMailToAdministrators(String subject, String message) {
        notificationManager.sendMailToAdministrators(subject, message);
    }

    @Override
    public void sendMail(String subject, String message, Collection<String> toAddresses) {
        notificationManager.sendMail(subject, message, toAddresses);
    }

    @Override
    public void sendHtmlMail(String subject, String message, Collection<String> toAddresses) {
        notificationManager.sendHtmlMail(subject, message, toAddresses);
    }

    @Override
    public void send(Mailing mailing) {
        notificationManager.send(mailing);
    }

    /****************************************************************************************
     *                                 NotifitionManager end                                *
     ****************************************************************************************/

    /****************************************************************************************
     *                             AuthenticationManager start                              *
     ****************************************************************************************/

    @Override
    public UserModel authenticate(HttpServletRequest httpRequest) {
        UserModel user = authenticationManager.authenticate(httpRequest, false);
//        if (user == null) {
//            user = federationManager.authenticate(httpRequest);
//        }
        return user;
    }

    @Override
    public UserModel authenticate(HttpServletRequest httpRequest, boolean requiresCertificate) {
        UserModel user = authenticationManager.authenticate(httpRequest, requiresCertificate);
//        if (user == null) {
//            user = federationManager.authenticate(httpRequest);
//        }
        return user;
    }

    @Override
    public UserModel authenticate(String username, char[] password, String remoteIP) {
        return authenticationManager.authenticate(username, password, remoteIP);
    }

    @Override
    public UserModel authenticate(String username) {
        return authenticationManager.authenticate(username);
    }

    @Override
    public String getCookie(HttpServletRequest request) {
        return authenticationManager.getCookie(request);
    }

    @Override
    @Deprecated
    public void setCookie(HttpServletResponse response, UserModel user) {
        authenticationManager.setCookie(response, user);
    }

    @Override
    public void setCookie(HttpServletRequest request, HttpServletResponse response, UserModel user) {
        authenticationManager.setCookie(request, response, user);
    }

    @Override
    @Deprecated
    public void logout(HttpServletResponse response, UserModel user) {
        authenticationManager.logout(response, user);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, UserModel user) {
        authenticationManager.logout(request, response, user);
    }

    @Override
    public boolean supportsCredentialChanges(UserModel user) {
        return authenticationManager.supportsCredentialChanges(user);
    }

    @Override
    public boolean supportsDisplayNameChanges(UserModel user) {
        return authenticationManager.supportsDisplayNameChanges(user);
    }

    @Override
    public boolean supportsEmailAddressChanges(UserModel user) {
        return authenticationManager.supportsEmailAddressChanges(user);
    }

    @Override
    public boolean supportsTeamMembershipChanges(UserModel user) {
        return authenticationManager.supportsTeamMembershipChanges(user);
    }

    @Override
    public boolean supportsTeamMembershipChanges(TeamModel team) {
        return authenticationManager.supportsTeamMembershipChanges(team);
    }

    @Override
    public boolean supportsRoleChanges(UserModel user, Constants.Role role) {
        return authenticationManager.supportsRoleChanges(user, role);
    }

    @Override
    public boolean supportsRoleChanges(TeamModel team, Constants.Role role) {
        return authenticationManager.supportsRoleChanges(team, role);
    }

    /****************************************************************************************
     *                             AuthenticationManager end                                *
     ****************************************************************************************/

    /****************************************************************************************
     *                             UserManager start                                        *
     ****************************************************************************************/

    @Override
    public void setup(IRuntimeManager runtimeManager) {

    }

    @Override
    public String getCookie(UserModel model) {
        return userManager.getCookie(model);
    }

    @Override
    public UserModel getUserModel(char[] cookie) {
        return userManager.getUserModel(cookie);
    }

    @Override
    public UserModel getUserModel(String username) {
        return userManager.getUserModel(username);
    }

    @Override
    public UserModel getUserModel(Integer userId) {
        return userManager.getUserModel(userId);
    }

    @Override
    public boolean updateUserModel(UserModel model) {
        return userManager.updateUserModel(model);
    }

    @Override
    public boolean updateUserModels(Collection<UserModel> models) {
        return userManager.updateUserModels(models);
    }

    @Override
    public boolean updateUserModel(String username, UserModel model) {
        return userManager.updateUserModel(username, model);
    }

    @Override
    public boolean deleteUserModel(UserModel model) {
        boolean success = userManager.deleteUserModel(model);
//        if (success) {
//            getPublicKeyManager().removeAllKeys(model.username);
//        }
        return success;
    }

    @Override
    public boolean deleteUser(String username) {
        UserModel user = userManager.getUserModel(username);
        return deleteUserModel(user);
    }

    @Override
    public List<String> getAllUsernames() {
        return userManager.getAllUsernames();
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userManager.getAllUsers();
    }

    @Override
    public List<String> getAllTeamNames() {
        return userManager.getAllTeamNames();
    }

    @Override
    public List<TeamModel> getAllTeams() {
        return userManager.getAllTeams();
    }

    @Override
    public List<String> getTeamNamesForRepositoryRole(String role) {
        return userManager.getTeamNamesForRepositoryRole(role);
    }

    @Override
    public TeamModel getTeamModel(String teamname) {
        return userManager.getTeamModel(teamname);
    }

    @Override
    public boolean updateTeamModel(TeamModel model) {
        return userManager.updateTeamModel(model);
    }

    @Override
    public boolean updateTeamModels(Collection<TeamModel> models) {
        return userManager.updateTeamModels(models);
    }

    @Override
    public boolean updateTeamModel(String teamname, TeamModel model) {
        return userManager.updateTeamModel(teamname, model);
    }

    @Override
    public boolean deleteTeamModel(TeamModel model) {
        return userManager.deleteTeamModel(model);
    }

    @Override
    public boolean deleteTeam(String teamname) {
        return userManager.deleteTeam(teamname);
    }

    @Override
    public List<String> getUsernamesForRepositoryRole(String role) {
        return userManager.getUsernamesForRepositoryRole(role);
    }

    @Override
    public boolean renameRepositoryRole(String oldRole, String newRole) {
        return userManager.renameRepositoryRole(oldRole, newRole);
    }

    @Override
    public boolean deleteRepositoryRole(String role) {
        return userManager.deleteRepositoryRole(role);
    }

    @Override
    public boolean isInternalAccount(String username) {
        return userManager.isInternalAccount(username);
    }

    /****************************************************************************************
     *                             UserManager end                                          *
     ****************************************************************************************/

    /****************************************************************************************
     *                          RepositoryManager start                                     *
     ****************************************************************************************/

    @Override
    public File getRepositoriesFolder() {
        return repositoryManager.getRepositoriesFolder();
    }

    @Override
    public File getHooksFolder() {
        return repositoryManager.getHooksFolder();
    }

    @Override
    public File getGrapesFolder() {
        return repositoryManager.getGrapesFolder();
    }

    @Override
    public Date getLastActivityDate() {
        return repositoryManager.getLastActivityDate();
    }

    @Override
    public List<RegistrantAccessPermission> getUserAccessPermissions(UserModel user) {
        return repositoryManager.getUserAccessPermissions(user);
    }

    @Override
    public List<RegistrantAccessPermission> getUserAccessPermissions(TaskEntity repository) {
        return repositoryManager.getUserAccessPermissions(repository);
    }

    @Override
    public boolean setUserAccessPermissions(TaskEntity repository, Collection<RegistrantAccessPermission> permissions) {
        return repositoryManager.setUserAccessPermissions(repository, permissions);
    }

    @Override
    public List<String> getRepositoryUsers(TaskEntity repository) {
        return repositoryManager.getRepositoryUsers(repository);
    }

    @Override
    public List<RegistrantAccessPermission> getTeamAccessPermissions(TaskEntity repository) {
        return repositoryManager.getTeamAccessPermissions(repository);
    }

    @Override
    public boolean setTeamAccessPermissions(TaskEntity repository, Collection<RegistrantAccessPermission> permissions) {
        return repositoryManager.setTeamAccessPermissions(repository, permissions);
    }

    @Override
    public List<String> getRepositoryTeams(TaskEntity repository) {
        return repositoryManager.getRepositoryTeams(repository);
    }

    @Override
    public void addToCachedRepositoryList(TaskEntity model) {
        repositoryManager.addToCachedRepositoryList(model);
    }

    @Override
    public void resetRepositoryListCache() {
        repositoryManager.resetRepositoryListCache();
    }

    @Override
    public void resetRepositoryCache(String repositoryName) {
        repositoryManager.resetRepositoryCache(repositoryName);
    }

    @Override
    public List<String> getRepositoryList() {
        return repositoryManager.getRepositoryList();
    }

    @Override
    public Repository getRepository(String repositoryName) {
        return repositoryManager.getRepository(repositoryName);
    }

    @Override
    public Repository getRepository(String repositoryName, boolean logError) {
        return repositoryManager.getRepository(repositoryName, logError);
    }

    @Override
    public List<TaskEntity> getRepositoryModels() {
        return repositoryManager.getRepositoryModels();
    }

    @Override
    public List<TaskEntity> getRepositoryModels(UserModel user) {
        return repositoryManager.getRepositoryModels(user);
    }

    @Override
    public TaskEntity getRepositoryModel(UserModel user, String repositoryName) {
        return repositoryManager.getRepositoryModel(repositoryName);
    }

    @Override
    public TaskEntity getRepositoryModel(String repositoryName) {
        return repositoryManager.getRepositoryModel(repositoryName);
    }

//    @Override
//    public long getStarCount(TaskEntity repository) {
//        return repositoryManager.getStarCount(repository);
//    }

    @Override
    public boolean hasRepository(String repositoryName) {
        return repositoryManager.hasRepository(repositoryName);
    }

    @Override
    public boolean hasRepository(String repositoryName, boolean caseSensitiveCheck) {
        return repositoryManager.hasRepository(repositoryName, caseSensitiveCheck);
    }

    @Override
    public boolean hasFork(String username, String origin) {
        return repositoryManager.hasFork(username, origin);
    }

    @Override
    public String getFork(String username, String origin) {
        return repositoryManager.getFork(username, origin);
    }

    @Override
    public ForkModel getForkNetwork(String repository) {
        return repositoryManager.getForkNetwork(repository);
    }

    @Override
    public long updateLastChangeFields(Repository r, TaskEntity model) {
        return repositoryManager.updateLastChangeFields(r, model);
    }

    @Override
    public List<Metric> getRepositoryDefaultMetrics(TaskEntity model, Repository repository) {
        return repositoryManager.getRepositoryDefaultMetrics(model, repository);
    }

    @Override
    public void updateRepositoryModel(String repositoryName, TaskEntity repository, boolean isCreate) throws GitBlitException {
        TaskEntity oldModel = null;
        boolean isRename = !isCreate && !repositoryName.equalsIgnoreCase(repository.getTaskName());
        if (isRename) {
            oldModel = repositoryManager.getRepositoryModel(repositoryName);
        }

        repositoryManager.updateRepositoryModel(repositoryName, repository, isCreate);

//        if (isRename && ticketServiceProvider.get() != null) {
//            ticketServiceProvider.get().rename(oldModel, repository);
//        }
    }

    @Override
    public void updateConfiguration(Repository r, TaskEntity repository) {
        repositoryManager.updateConfiguration(r, repository);
    }

    @Override
    public boolean canDelete(TaskEntity model) {
        return repositoryManager.canDelete(model);
    }

    @Override
    public boolean deleteRepositoryModel(TaskEntity model) {
        boolean success = repositoryManager.deleteRepositoryModel(model);
//        if (success && ticketServiceProvider.get() != null) {
//            ticketServiceProvider.get().deleteAll(model);
//        }
        return success;
    }

    @Override
    public boolean deleteRepository(String repositoryName) {
        TaskEntity repository = repositoryManager.getRepositoryModel(repositoryName);
        return deleteRepositoryModel(repository);
    }

    @Override
    public List<String> getAllScripts() {
        return repositoryManager.getAllScripts();
    }

    @Override
    public List<String> getPreReceiveScriptsInherited(TaskEntity repository) {
        return repositoryManager.getPreReceiveScriptsInherited(repository);
    }

    @Override
    public List<String> getPreReceiveScriptsUnused(TaskEntity repository) {
        return repositoryManager.getPreReceiveScriptsUnused(repository);
    }

    @Override
    public List<String> getPostReceiveScriptsInherited(TaskEntity repository) {
        return repositoryManager.getPostReceiveScriptsInherited(repository);
    }

    @Override
    public List<String> getPostReceiveScriptsUnused(TaskEntity repository) {
        return repositoryManager.getPreReceiveScriptsUnused(repository);
    }

    @Override
    public List<SearchResult> search(String query, int page, int pageSize, List<String> repositories) {
        return repositoryManager.search(query, page, pageSize, repositories);
    }

    @Override
    public boolean isCollectingGarbage() {
        return repositoryManager.isCollectingGarbage();
    }

    @Override
    public boolean isCollectingGarbage(String repositoryName) {
        return repositoryManager.isCollectingGarbage(repositoryName);
    }

    @Override
    public void closeAll() {

    }

    @Override
    public void close(String repository) {

    }

    @Override
    public boolean isIdle(Repository repository) {
        return false;
    }
    /****************************************************************************************
     *                          RepositoryManager end                                       *
     ****************************************************************************************/

    /****************************************************************************************
     *                          ProjectManager start                                        *
     ****************************************************************************************/

    @Override
    public List<ProjectModel> getProjectModels(UserModel user, boolean includeUsers) {
        return projectManager.getProjectModels(user, includeUsers);
    }

    @Override
    public ProjectModel getProjectModel(String name, UserModel user) {
        return projectManager.getProjectModel(name, user);
    }

    @Override
    public ProjectModel getProjectModel(String name) {
        return projectManager.getProjectModel(name);
    }

    @Override
    public List<ProjectModel> getProjectModels(List<TaskEntity> repositoryModels, boolean includeUsers) {
        return projectManager.getProjectModels(repositoryModels, includeUsers);
    }
    /****************************************************************************************
     *                          ProjectManager end                                          *
     ****************************************************************************************/

    /****************************************************************************************
     *                          FileStoreManager start                                      *
     ****************************************************************************************/

    @Override
    public boolean isValidOid(String oid) {
        return filestoreManager.isValidOid(oid);
    }

    @Override
    public FilestoreModel.Status addObject(String oid, long size, UserModel user, TaskEntity repo) {
        return filestoreManager.addObject(oid, size, user, repo);
    }

    @Override
    public FilestoreModel getObject(String oid, UserModel user, TaskEntity repo) {
        return filestoreManager.getObject(oid, user, repo);
    }

    @Override
    public FilestoreModel.Status uploadBlob(String oid, long size, UserModel user, TaskEntity repo, InputStream streamIn) {
        return filestoreManager.uploadBlob(oid, size, user, repo, streamIn);
    }

    @Override
    public FilestoreModel.Status downloadBlob(String oid, UserModel user, TaskEntity repo, OutputStream streamOut) {
        return filestoreManager.downloadBlob(oid, user, repo, streamOut);
    }

    @Override
    public List<FilestoreModel> getAllObjects(UserModel user) {
        return filestoreManager.getAllObjects(user);
    }

    @Override
    public File getStorageFolder() {
        return filestoreManager.getStorageFolder();
    }

    @Override
    public File getStoragePath(String oid) {
        return filestoreManager.getStoragePath(oid);
    }

    @Override
    public long getMaxUploadSize() {
        return filestoreManager.getMaxUploadSize();
    }

    @Override
    public void clearFilestoreCache() {
        filestoreManager.clearFilestoreCache();
    }

    @Override
    public long getFilestoreUsedByteCount() {
        return filestoreManager.getFilestoreUsedByteCount();
    }

    @Override
    public long getFilestoreAvailableByteCount() {
        return filestoreManager.getFilestoreAvailableByteCount();
    }

    /****************************************************************************************
     *                          FileStoreManager end                                        *
     ****************************************************************************************/

    /****************************************************************************************
     *                          PluginManager start                                         *
     ****************************************************************************************/

    @Override
    public Version getSystemVersion() {
        return pluginManager.getSystemVersion();
    }

    @Override
    public void startPlugins() {
        pluginManager.startPlugins();
    }

    @Override
    public void stopPlugins() {
        pluginManager.stopPlugins();
    }

    @Override
    public PluginState startPlugin(String pluginId) {
        return pluginManager.startPlugin(pluginId);
    }

    @Override
    public PluginState stopPlugin(String pluginId) {
        return pluginManager.stopPlugin(pluginId);
    }

    @Override
    public List<Class<?>> getExtensionClasses(String pluginId) {
        return pluginManager.getExtensionClasses(pluginId);
    }

    @Override
    public <T> List<T> getExtensions(Class<T> type) {
        return pluginManager.getExtensions(type);
    }

    @Override
    public List<PluginWrapper> getPlugins() {
        return pluginManager.getPlugins();
    }

    @Override
    public PluginWrapper getPlugin(String pluginId) {
        return pluginManager.getPlugin(pluginId);
    }

    @Override
    public PluginWrapper whichPlugin(Class<?> clazz) {
        return pluginManager.whichPlugin(clazz);
    }

    @Override
    public boolean disablePlugin(String pluginId) {
        return pluginManager.disablePlugin(pluginId);
    }

    @Override
    public boolean enablePlugin(String pluginId) {
        return pluginManager.enablePlugin(pluginId);
    }

    @Override
    public boolean uninstallPlugin(String pluginId) {
        return pluginManager.uninstallPlugin(pluginId);
    }

    @Override
    public boolean refreshRegistry(boolean verifyChecksum) {
        return pluginManager.refreshRegistry(verifyChecksum);
    }

    @Override
    public boolean installPlugin(String url, boolean verifyChecksum) throws IOException {
        return pluginManager.installPlugin(url, verifyChecksum);
    }

    @Override
    public boolean upgradePlugin(String pluginId, String url, boolean verifyChecksum) throws IOException {
        return pluginManager.upgradePlugin(pluginId, url, verifyChecksum);
    }

    @Override
    public List<PluginRegistry.PluginRegistration> getRegisteredPlugins() {
        return pluginManager.getRegisteredPlugins();
    }

    @Override
    public List<PluginRegistry.PluginRegistration> getRegisteredPlugins(PluginRegistry.InstallState state) {
        return pluginManager.getRegisteredPlugins(state);
    }

    @Override
    public PluginRegistry.PluginRegistration lookupPlugin(String idOrName) {
        return pluginManager.lookupPlugin(idOrName);
    }

    @Override
    public PluginRegistry.PluginRelease lookupRelease(String idOrName, String version) {
        return pluginManager.lookupRelease(idOrName, version);
    }
    /****************************************************************************************
     *                          PluginManager end                                           *
     ****************************************************************************************/
}
