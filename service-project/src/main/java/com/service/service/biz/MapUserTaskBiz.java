package com.service.service.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.entity.MapUserTask;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;
import com.service.service.exception.GitBlitException;
import com.service.service.managers.IWorkHub;
import com.service.service.managers.UserManager;
import com.service.service.mapper.MapUserTaskMapper;
import com.service.service.utils.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public boolean updateTasksInUser(Integer userId, String taskIds){
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
     * @param taskId
     * @param userIds
     */
    public boolean updateUsersInTask(Integer taskId, String userIds) throws GitBlitException {
        mapper.deleteTasksByUserId(taskId);
        if (!StringUtils.isEmpty(userIds)) {
            String[] users = userIds.split(",");
            TaskEntity taskEntity = taskBiz.selectById(taskId);
            for (String u : users) {
                MapUserTask mapUserTask = new MapUserTask();
                UserModel userModel = workHub.getUserModel(Integer.valueOf(u));
                mapUserTask.setTaskId(Integer.valueOf(taskId));
                mapUserTask.setTaskName(taskEntity.getTaskName());
                mapUserTask.setUserId(Integer.valueOf(u));
                mapUserTask.setUserName(userModel.getName());
                mapUserTask.setPermission("3");
                insert(mapUserTask);
                taskEntity.addOwner(u);
            }
            workHub.updateRepositoryModel(taskEntity.getTaskName(), taskEntity, false);
        }
        return true;
    }

    /**
     * 在任务中删除用户，删除数据库内容的同时删除仓库的配置文件owner
     * @param mapUserTask
     * @return
     */
    public boolean deleteUserInTask(MapUserTask mapUserTask) throws GitBlitException {
        if (mapUserTask.getId() != null){
            super.delete(mapUserTask);

            Example example = new Example(MapUserTask.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", mapUserTask.getTaskId());
            List<MapUserTask> mapUserTasks = super.selectByExample(example);

            Integer[] userIds = new Integer[mapUserTasks.size()];
            if (mapUserTasks != null && mapUserTasks.size() > 0){
                for (int z = 0; z < userIds.length; z++) {
                    userIds[z] = mapUserTasks.get(z).getUserId();
                }
            }

            return updateUsersInTask(mapUserTask.getTaskId(), Arrays.toString(userIds));
        }else {
            return false;
        }


    }

    @Override
    protected String getPageName() {
        return null;
    }

    public boolean canView(){

        return false;
    }
    public boolean canClone(){
        return false;
    }
    public boolean canPush(){
        return false;
    }
}
