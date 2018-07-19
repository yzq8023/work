package com.github.wxiaoqi.security.api.vo.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-06-21 8:12
 */
public class UserInfo implements Serializable{
    private String id;
    private String username;
    private String password;
    private String name;
    private String description;
    private Date updTime;
    private boolean canAdmin;
    private boolean canFork;
    private boolean canCreate;

    public Date getUpdTime() {
        return updTime;
    }
    @JsonDeserialize(using = Custom_Json_Date_Deserializer.class)
    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCanAdmin() {
        return canAdmin;
    }

    public void setCanAdmin(boolean canAdmin) {
        this.canAdmin = canAdmin;
    }

    public boolean isCanFork() {
        return canFork;
    }

    public void setCanFork(boolean canFork) {
        this.canFork = canFork;
    }

    public boolean isCanCreate() {
        return canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }
}
