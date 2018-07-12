package com.service.service.mapper;

import com.service.service.entity.ProjectEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProjectEntityMapper extends Mapper<ProjectEntity> {
    public List<ProjectEntity>  selectProjectByUserId(@Param("userId") Integer userId);
    public void deleteProTeamsById(@Param("projectId") Integer projectId);
    public void insertProTeamsById(@Param("projectId") Integer projectId, @Param("teamId") Integer teamId);
}