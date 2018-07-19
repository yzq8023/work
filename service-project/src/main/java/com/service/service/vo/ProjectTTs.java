package com.service.service.vo;

import com.service.service.entity.TaskEntity;
import com.service.service.entity.TeamModel;

import java.util.List;
/**
 * 项目关联信息实体
 * 描述：T:Team、T:Task
 *
 * @author dk
 */
public class ProjectTTs {

    List<TaskEntity> taskEntities;
    List<TeamModel> teamEntities;

    public ProjectTTs() {
    }

    public ProjectTTs(List<TaskEntity> taskEntities, List<TeamModel> teamEntities) {
        this.taskEntities = taskEntities;
        this.teamEntities = teamEntities;
    }

    public List<TaskEntity> getTaskEntities() {
        return taskEntities;
    }

    public void setTaskEntities(List<TaskEntity> taskEntities) {
        this.taskEntities = taskEntities;
    }

    public List<TeamModel> getTeamEntities() {
        return teamEntities;
    }

    public void setTeamEntities(List<TeamModel> teamEntities) {
        this.teamEntities = teamEntities;
    }
}
