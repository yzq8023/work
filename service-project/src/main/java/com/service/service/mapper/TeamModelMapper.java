package com.service.service.mapper;

import com.service.service.entity.TeamModel;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TeamModelMapper extends Mapper<TeamModel> {

    public void deleteTeamUsersById(@Param("teamId") Integer teamId);

    public void insertTeamUsersById(@Param("teamId") Integer teamId, @Param("userId") Integer userId);

    public List<TeamModel> selectTeamByUserId(@Param("userId") Integer userId);

    public List<String> getTeamIds();

    public List<String> getTeamPermissions(@Param("teamId") Integer teamId);

    public List<String> getUserIdsByTeamId(@Param("teamId") Integer teamId);

    public void deleteTeamsByUserId(@Param("userId") Integer userId);

    public  void insertTeamsByUserId(@Param("userId") Integer userId, @Param("teamId") Integer teamId);
}