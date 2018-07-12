package com.service.service.vo;

import com.github.wxiaoqi.security.api.vo.user.UserInfo;
import com.service.service.entity.TeamEntity;

import java.util.List;
/**
 * 任务关联信息实体
 * 描述：T:Team、U:User
 *
 * @author dk
 */
public class TaskTUs {

    List<TeamEntity> teamEntities;
    List<UserInfo> userInfos;

    public TaskTUs(){

    }

    public TaskTUs(List<TeamEntity> teamEntities, List<UserInfo> userInfos){
        this.teamEntities = teamEntities;
        this.userInfos = userInfos;
    }

    public List<TeamEntity> getTeamEntities() {
        return teamEntities;
    }

    public void setTeamEntities(List<TeamEntity> teamEntities) {
        this.teamEntities = teamEntities;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }
}
