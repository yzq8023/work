package com.service.service.biz;

import com.ace.cache.annotation.CacheClear;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.api.vo.user.UserInfo;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.Constants;
import com.service.service.IStoredSettings;
import com.service.service.Keys;
import com.service.service.entity.ProjectModel;
import com.service.service.entity.RepositoryModel;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;
import com.service.service.exception.GitBlitException;
import com.service.service.feign.IUserFeignClient;
import com.service.service.managers.IRepositoryManager;
import com.service.service.managers.IRuntimeManager;
import com.service.service.managers.IUserManager;
import com.service.service.managers.IWorkHub;
import com.service.service.mapper.TaskEntityMapper;
import com.service.service.utils.InitialCommit;
import com.service.service.utils.ModelUtils;
import com.service.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 业务逻辑类
 * 描述：实现在增删改中对事务的管理和回滚，所有需要实现事物的方法均要放到这里
 * @author dk
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskBiz extends BaseBiz<TaskEntityMapper, TaskEntity> {

    private IUserManager userManager;
    private IWorkHub workHub;
    private IStoredSettings settings;
    private IUserFeignClient userFeignClient;
    private IRepositoryManager repositoryManager;
    List<TaskEntity> repositoryModels = new ArrayList<TaskEntity>();

    @Autowired
    public TaskBiz(IUserManager userManager,
                   IWorkHub workHub,
                   IStoredSettings settings,
                   IUserFeignClient userFeignClient,
                   IRepositoryManager repositoryManager) {
        this.userManager = userManager;
        this.workHub = workHub;
        this.settings = settings;
        this.userFeignClient = userFeignClient;
        this.repositoryManager = repositoryManager;
    }

    /**
     * 更改任务关联团队
     *
     * @param taskId 任务id
     * @param teams  队伍
     */
    @CacheClear(pre = "permission")
    public void modifyTaskTeams(Integer taskId, String teams) {
        mapper.deleteTaskTeamsById(taskId);
        if (!StringUtils.isEmpty(teams)) {
            String[] team = teams.split(",");
            for (String t : team) {
                mapper.insertTaskTeamsById(taskId, Integer.parseInt(t));
            }
        }
    }

    /**
     * 根据用户id,项目id查看所分配的任务分页
     *
     * @param query     dao接口
     * @return 任务
     */
    @CacheClear(pre = "permission")
    public TableResultResponse<TaskEntity> selectTaskById(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<TaskEntity> list = mapper.selectTaskById(query.getTaskExecutorId(), query.getProjectId());
        return new TableResultResponse<>(result.getTotal(), list);
    }

    /**
     * 根据用户及名称创建任务
     *
     * @param taskEntity 任务实体
     */
    public TaskEntity createTask(TaskEntity taskEntity, String userId) {
        try {

            //transfer
            taskEntity.setCrtUser(userId);
            taskEntity.setHead(Constants.R_MASTER);
            taskEntity.setMergeTo(Constants.MASTER);
            //update
            workHub.updateRepositoryModel(taskEntity.getTaskName(), taskEntity, true);

            InitialCommit initialCommit = new InitialCommit();
            UserModel user = userManager.getUserModel(Integer.valueOf(userId));
            boolean isSuccess = initialCommit.initialCommit(taskEntity, user);
            return taskEntity;
        } catch (GitBlitException e) {
            return null;
        }
    }

    public List<TaskEntity> getRepositories(Query query) {
        String userId = String.valueOf(query.getTaskExecutorId());
        if (query == null) {
            return getRepositoryModels(userId);
        }

        boolean hasParameter = false;
        String projectName = query.getProjectName();
        if (StringUtils.isEmpty(projectName)) {
            if (!StringUtils.isEmpty(userId)) {
                projectName = ModelUtils.getPersonalPath(userId);
            }
        }
        String repositoryName = query.getTaskName();
//        String set = WicketUtils.getSet(params);
//        String regex = WicketUtils.getRegEx(params);
//        String team = WicketUtils.getTeam(params);
//        int daysBack = params.getInt("db", 0);
//        int maxDaysBack = app().settings().getInteger(Keys.web.activityDurationMaximum, 30);

        List<TaskEntity> availableModels = getRepositoryModels(userId);
        Set<TaskEntity> models = new HashSet<TaskEntity>();

        if (!StringUtils.isEmpty(repositoryName)) {
            // try named repository
            hasParameter = true;
            for (TaskEntity model : availableModels) {
                if (model.getTaskName().equalsIgnoreCase(repositoryName)) {
                    models.add(model);
                    break;
                }
            }
        }

        if (!StringUtils.isEmpty(projectName)) {
            // try named project
            hasParameter = true;
            if (projectName.equalsIgnoreCase(settings.getString(Keys.web.repositoryRootGroupName, "main"))) {
                // root project/group
                for (TaskEntity model : availableModels) {
                    if (model.getTaskName().indexOf('/') == -1) {
                        models.add(model);
                    }
                }
            } else {
                // named project/group
                String group = projectName.toLowerCase() + "/";
                for (TaskEntity model : availableModels) {
                    if (model.getTaskName().toLowerCase().startsWith(group)) {
                        models.add(model);
                    }
                }
            }
        }

//        if (!StringUtils.isEmpty(team)) {
//            // filter the repositories by the specified teams
//            hasParameter = true;
//            List<String> teams = StringUtils.getStringsFromValue(team, ",");
//
//            // need TeamModels first
//            List<TeamModel> teamModels = new ArrayList<TeamModel>();
//            for (String name : teams) {
//                TeamModel teamModel = app().users().getTeamModel(name);
//                if (teamModel != null) {
//                    teamModels.add(teamModel);
//                }
//            }
//
//            // brute-force our way through finding the matching models
//            for (TaskEntity repositoryModel : availableModels) {
//                for (TeamModel teamModel : teamModels) {
//                    if (teamModel.hasRepositoryPermission(repositoryModel.name)) {
//                        models.add(repositoryModel);
//                    }
//                }
//            }
//        }

        if (!hasParameter) {
            models.addAll(availableModels);
        }

        List<TaskEntity> list = new ArrayList<TaskEntity>(models);
        Collections.sort(list);
        return list;
    }

    protected List<TaskEntity> getRepositoryModels(String userId) {
        if (repositoryModels.isEmpty()) {
            UserInfo userInfo = userFeignClient.info(Integer.valueOf(userId));
            final UserModel user = userSwitch(userInfo);
            List<TaskEntity> repositories = repositoryManager.getRepositoryModels(user);
            repositoryModels.addAll(repositories);
            Collections.sort(repositoryModels);
        }
        return repositoryModels;
    }

    public UserModel userSwitch(UserInfo userInfo){
        UserModel userModel = new UserModel(userInfo.getUsername());
        userModel.username = userInfo.getUsername();
        userModel.password = userInfo.getPassword();
        userModel.cookie = null;
        userModel.displayName = null;
        userModel.emailAddress = "hollykunge@163.com";
        userModel.organizationalUnit = null;
        userModel.organization = null;
        userModel.locality = null;
        userModel.stateProvince = null;
        userModel.countryCode = "+86";
        userModel.canAdmin = true;
        userModel.canFork = true;
        userModel.canCreate = true;
        userModel.excludeFromFederation = false;
        userModel.disabled = false;
        return userModel;
    }
}

