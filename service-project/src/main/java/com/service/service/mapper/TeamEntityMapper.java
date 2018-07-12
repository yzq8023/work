package com.service.service.mapper;

import com.service.service.entity.TeamEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TeamEntityMapper extends Mapper<TeamEntity> {
    public void deleteTeamUsersById(@Param("teamId") int teamId);

    public void insertTeamUsersById(@Param("teamId") Integer teamId, @Param("userId") Integer userId);

    public List<TeamEntity> selectTeamByUserId(@Param("userId") Integer userId);
}