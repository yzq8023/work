package com.service.service.vo;

import com.service.service.entity.TaskEntity;
import com.service.service.entity.TeamEntity;

import java.util.List;
/**
 * 项目关联信息实体
 * 描述：T:Team、T:Task
 *
 * @author dk
 */
public class ProjectTTs {

    List<TaskEntity> taskEntities;
    List<TeamEntity> teamEntities;

    public ProjectTTs() {
    }

    public ProjectTTs(List<TaskEntity> taskEntities, List<TeamEntity> teamEntities) {
        this.taskEntities = taskEntities;
        this.teamEntities = teamEntities;
    }

    public List<TaskEntity> getTaskEntities() {
        return taskEntities;
    }

    public void setTaskEntities(List<TaskEntity> taskEntities) {
        this.taskEntities = taskEntities;
    }

    public List<TeamEntity> getTeamEntities() {
        return teamEntities;
    }

    public void setTeamEntities(List<TeamEntity> teamEntities) {
        this.teamEntities = teamEntities;
    }
}
