package com.github.hollykunge.security.admin.vo;

import com.github.hollykunge.security.admin.entity.User;

import java.util.List;

public class OrgUsers {
    List<User> members ;
    List<User> leaders;

    public OrgUsers() {
    }

    public OrgUsers(List<User> members, List<User> leaders) {
        this.members = members;
        this.leaders = leaders;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<User> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<User> leaders) {
        this.leaders = leaders;
    }
}
