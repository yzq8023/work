package com.service.service.mapper;

import com.service.service.entity.TaskEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TaskEntityMapper extends Mapper<TaskEntity> {
    public void deleteTaskTeamsById(@Param("taskId") Integer taskId);
    public void insertTaskTeamsById(@Param("taskId") Integer taskId, @Param("teamId") Integer teamId);
    public List<Map<String,Object>> selectTaskByPIdAndUId(@Param("userId") Integer userId, @Param("projectId") Integer projectId,@Param("taskName") String taskName,@Param("taskProcess") Integer taskProcess);
    public List<TaskEntity> selectJoinedTaskById(@Param("userId") Integer userId);
    public int selectTaskByTIdAndCtrUser(@Param("crtUser") Integer crtUser,@Param("taskId") Integer taskId);
    public int selectMapTaskByTIdAndCtrUser(@Param("crtUser") Integer crtUser,@Param("taskId") Integer taskId);
}