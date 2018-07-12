package com.service.service.vo;

import com.github.wxiaoqi.security.api.vo.user.UserInfo;
import com.service.service.entity.ProjectEntity;
import com.service.service.entity.TaskEntity;

import java.util.List;

/**
 * 团队关联信息实体
 * 描述：P:Project、T:Task、U:User
 *
 * @author dk
 */
public class TeamPTUs {

    List<ProjectEntity> projectEntities;
    List<TaskEntity> taskEntities;
    List<UserInfo> userInfos;

    public TeamPTUs(){

    }

    public TeamPTUs(List<ProjectEntity> projectEntities, List<TaskEntity> taskEntities, List<UserInfo> userInfos){
        this.projectEntities = projectEntities;
        this.taskEntities = taskEntities;
        this.userInfos = userInfos;
    }

    public List<ProjectEntity> getProjectEntities() {
        return projectEntities;
    }

    public void setProjectEntities(List<ProjectEntity> projectEntities) {
        this.projectEntities = projectEntities;
    }

    public List<TaskEntity> getTaskEntities() {
        return taskEntities;
    }

    public void setTaskEntities(List<TaskEntity> taskEntities) {
        this.taskEntities = taskEntities;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }
}
