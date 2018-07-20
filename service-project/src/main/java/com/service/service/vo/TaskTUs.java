package com.service.service.vo;

import com.github.wxiaoqi.security.api.vo.user.UserInfo;
import com.service.service.entity.TeamModel;

import java.util.List;
/**
 * 任务关联信息实体
 * 描述：T:Team、U:User
 *
 * @author dk
 */
public class TaskTUs {

    List<TeamModel> teamEntities;
    List<UserInfo> userInfos;

    public TaskTUs(){

    }

    public TaskTUs(List<TeamModel> teamEntities, List<UserInfo> userInfos){
        this.teamEntities = teamEntities;
        this.userInfos = userInfos;
    }

    public List<TeamModel> getTeamEntities() {
        return teamEntities;
    }

    public void setTeamEntities(List<TeamModel> teamEntities) {
        this.teamEntities = teamEntities;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }
}
