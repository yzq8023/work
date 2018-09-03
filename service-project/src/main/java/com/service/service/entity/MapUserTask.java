package com.service.service.entity;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Table(name = "map_user_task")
public class MapUserTask {

    @Id
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "permission")
    private String permission;
    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return task_id
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * @param taskId
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * @return user_name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return task_name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }


}