package com.service.service.mapper;

import com.service.service.entity.TaskEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TaskEntityMapper extends Mapper<TaskEntity> {
    public void deleteTaskTeamsById(@Param("taskId") Integer taskId);
    public void insertTaskTeamsById(@Param("taskId") Integer taskId, @Param("teamId") Integer teamId);
    public List<TaskEntity> selectTaskByPIdAndUId(@Param("userId") Integer userId, @Param("projectId") Integer projectId);
    public List<TaskEntity> selectJoinedTaskById(@Param("userId") Integer userId);
}