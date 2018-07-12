package com.service.service.biz;

import com.ace.cache.annotation.CacheClear;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.Constants;
import com.service.service.entity.ProjectModel;
import com.service.service.entity.RepositoryModel;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;
import com.service.service.exception.GitBlitException;
import com.service.service.managers.IRepositoryManager;
import com.service.service.managers.IRuntimeManager;
import com.service.service.managers.IUserManager;
import com.service.service.managers.IWorkHub;
import com.service.service.mapper.TaskEntityMapper;
import com.service.service.utils.InitialCommit;
import com.service.service.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 业务逻辑类
 * 描述：实现在增删改中对事务的管理和回滚，所有需要实现事物的方法均要放到这里
 * @author dk
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskBiz extends BaseBiz<TaskEntityMapper, TaskEntity> {

    IUserManager userManager;
    IWorkHub workHub;
    private final RepositoryModel repositoryModel;

    public TaskBiz() {
        repositoryModel = new RepositoryModel();
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
}

    /**
     * 根据用户及名称fork任务
     * @param userName 用户名
     * @param taskName 任务名
     */
    /*
    public void forkTask(String userName, String taskName) {
        try {

            UserModel user = userManager.getUserModel(userName);
            RepositoryModel repositoryModel = repositoryManager.getRepositoryModel(taskName);

            gitblit.fork(repositoryModel, user);
        } catch (GitBlitException e) {
            System.out.println(e.getMessage());
        }
    }
    */
