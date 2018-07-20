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
 *
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
    public void modifyTeamsInTask(Integer taskId, String teams) {
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
     * @param query dao接口
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
    public boolean createTask(TaskEntity taskEntity, String userId) {
        try {
            //transfer
            taskEntity.setCrtUser(userId);
            taskEntity.setHead(Constants.R_MASTER);
            taskEntity.setMergeTo(Constants.MASTER);
            //update
            taskEntity.setTaskName(taskEntity.getTaskProjectName() + "/" + taskEntity.getTaskName());
            workHub.updateRepositoryModel(taskEntity.getTaskName(), taskEntity, true);

            InitialCommit initialCommit = new InitialCommit();
            UserModel user = userManager.getUserModel(Integer.valueOf(userId));
            boolean isSuccess = initialCommit.initialCommit(taskEntity, user);
            return true;
        } catch (GitBlitException e) {
            return false;
        }
    }

    public TableResultResponse<TaskEntity> getRepositories(Query query) {
        String userId = String.valueOf(query.getTaskExecutorId());
//        if (query == null) {
//            return getRepositoryModels(userId);
//        }

        boolean hasParameter = false;
        String projectName = query.getProjectName();
        if (StringUtils.isEmpty(projectName)) {
            if (!StringUtils.isEmpty(userId)) {
                projectName = ModelUtils.getPersonalPath(userId);
            }
        }
        String repositoryName = query.getTaskName();

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

        if (!hasParameter) {
            models.addAll(availableModels);
        }

        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<TaskEntity> list = new ArrayList<TaskEntity>(models);
        return new TableResultResponse<>(result.getTotal(), list);
//
//        List<TaskEntity> list = new ArrayList<TaskEntity>(models);
//        Collections.sort(list);
//        return list;
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

    public UserModel userSwitch(UserInfo userInfo) {
        UserModel userModel = new UserModel(userInfo.getUsername());
        userModel.setUserId(userInfo.getUsername());
        userModel.setPassword(userInfo.getPassword());
        userModel.setCookie(null);
        userModel.setUsername(null);
        userModel.setEmailAddress(null);
        userModel.setOrganizationalUnit(null);
        userModel.setOrganization(null);
        userModel.setLocality(null);
        userModel.setStateProvince(null);
        userModel.setCountryCode(null);
        userModel.setCanAdmin(true);
        userModel.setCanFork(true);
        userModel.setCanCreate(true);
        userModel.setExcludeFromFederation(false);
        userModel.setDisabled(false);
        return userModel;
    }
}

