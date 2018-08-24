package com.service.service.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.service.service.entity.MapUserTask;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;
import com.service.service.exception.GitBlitException;
import com.service.service.managers.IWorkHub;
import com.service.service.mapper.MapUserTaskMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
 * @author hollykunge
 * @date 2018/7/18
 * @des 用户和任务仓库关系管理类
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MapUserTaskBiz extends BaseBiz<MapUserTaskMapper, MapUserTask> {

    private TaskBiz taskBiz;
    private IWorkHub workHub;

    @Autowired
    public MapUserTaskBiz(TaskBiz taskBiz, IWorkHub workHub) {
        this.taskBiz = taskBiz;
        this.workHub = workHub;
    }

    /**
     * 修改用户所属任务
     *
     * @param userId
     * @param taskIds
     */
    public boolean updateTasksInUser(Integer userId, String taskIds) {
        mapper.deleteTasksByUserId(userId);
        if (!StringUtils.isEmpty(taskIds)) {
            String[] teams = taskIds.split(",");
            for (String t : teams) {
                mapper.insertTasksByUserId(userId, Integer.parseInt(t));
            }
        }
        return true;
    }

    /**
     * 修改任务所包含用户
     *
     * @param mapUserTaskList
     */
    public boolean updateUsersInTask(List<MapUserTask> mapUserTaskList, Integer taskId) throws GitBlitException {
        if (taskId != null) {
            Example example = new Example(MapUserTask.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", taskId);
            mapper.deleteByExample(example);

            TaskEntity taskEntity = taskBiz.selectById(taskId);

            for (MapUserTask mapUserTask : mapUserTaskList) {
                insertSelective(mapUserTask);
                taskEntity.addOwner(String.valueOf(mapUserTask.getUserId()));
            }
            workHub.updateRepositoryModel(taskEntity.getTaskName(), taskEntity, false);
            return true;
        }
        return false;
    }

    @Override
    protected String getPageName() {
        return null;
    }

    public boolean canView() {

        return false;
    }

    public boolean canClone() {
        return false;
    }

    public boolean canPush() {
        return false;
    }
}
