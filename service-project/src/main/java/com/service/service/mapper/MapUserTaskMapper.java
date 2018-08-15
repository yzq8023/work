package com.service.service.mapper;

import com.service.service.entity.MapUserTask;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MapUserTaskMapper extends Mapper<MapUserTask> {
    public List<String> getUserIds();
    public void deleteTasksByUserId(@Param("userId") Integer userId);
    public void insertTasksByUserId(@Param("taskId") Integer taskId, @Param("userId") Integer userId);
}