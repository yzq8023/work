package com.service.service.vo;

import com.service.service.entity.TaskEntity;
import com.service.service.entity.TeamEntity;

import java.util.List;

/**
 * 用户关联信息实体
 * 描述：P:Project、T:Task、T:Team
 *
 * @author dk
 */
public class UserTTPs {

    List<TeamEntity> teamEntities;
    List<TaskEntity> userInfos;

    public UserTTPs(){
    }

    public UserTTPs(List<TeamEntity> teamEntities, List<TaskEntity> userInfos){
        this.teamEntities = teamEntities;
        this.userInfos = userInfos;
    }

    public List<TeamEntity> getTeamEntities() {
        return teamEntities;
    }

    public void setTeamEntities(List<TeamEntity> teamEntities) {
        this.teamEntities = teamEntities;
    }

    public List<TaskEntity> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<TaskEntity> userInfos) {
        this.userInfos = userInfos;
    }
}
