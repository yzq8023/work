package com.service.service.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "activity")
public class ActivityEntity {
    @Id
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "op_type")
    private Integer opType;

    @Column(name = "act_user_id")
    private Integer actUserId;

    @Column(name = "act_user_name")
    private String actUserName;

    @Column(name = "repo_id")
    private Integer repoId;

    @Column(name = "repo_user_name")
    private String repoUserName;

    @Column(name = "repo_name")
    private String repoName;

    @Column(name = "ref_name")
    private String refName;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @Column(name = "created_unix")
    private Integer createdUnix;

    @Column(name = "crt_name")
    private String crtName;

    @Column(name = "crt_user")
    private String crtUser;

    @Column(name = "crt_host")
    private String crtHost;

    @Column(name = "crt_time")
    private Date crtTime;

    @Column(name = "upd_time")
    private Date updTime;

    @Column(name = "upd_user")
    private String updUser;

    @Column(name = "upd_name")
    private String updName;

    @Column(name = "upd_host")
    private String updHost;

    private String content;

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
     * @return op_type
     */
    public Integer getOpType() {
        return opType;
    }

    /**
     * @param opType
     */
    public void setOpType(Integer opType) {
        this.opType = opType;
    }

    /**
     * @return act_user_id
     */
    public Integer getActUserId() {
        return actUserId;
    }

    /**
     * @param actUserId
     */
    public void setActUserId(Integer actUserId) {
        this.actUserId = actUserId;
    }

    /**
     * @return act_user_name
     */
    public String getActUserName() {
        return actUserName;
    }

    /**
     * @param actUserName
     */
    public void setActUserName(String actUserName) {
        this.actUserName = actUserName;
    }

    /**
     * @return repo_id
     */
    public Integer getRepoId() {
        return repoId;
    }

    /**
     * @param repoId
     */
    public void setRepoId(Integer repoId) {
        this.repoId = repoId;
    }

    /**
     * @return repo_user_name
     */
    public String getRepoUserName() {
        return repoUserName;
    }

    /**
     * @param repoUserName
     */
    public void setRepoUserName(String repoUserName) {
        this.repoUserName = repoUserName;
    }

    /**
     * @return repo_name
     */
    public String getRepoName() {
        return repoName;
    }

    /**
     * @param repoName
     */
    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    /**
     * @return ref_name
     */
    public String getRefName() {
        return refName;
    }

    /**
     * @param refName
     */
    public void setRefName(String refName) {
        this.refName = refName;
    }

    /**
     * @return is_private
     */
    public Boolean getIsPrivate() {
        return isPrivate;
    }

    /**
     * @param isPrivate
     */
    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     * @return created_unix
     */
    public Integer getCreatedUnix() {
        return createdUnix;
    }

    /**
     * @param createdUnix
     */
    public void setCreatedUnix(Integer createdUnix) {
        this.createdUnix = createdUnix;
    }

    /**
     * @return crt_name
     */
    public String getCrtName() {
        return crtName;
    }

    /**
     * @param crtName
     */
    public void setCrtName(String crtName) {
        this.crtName = crtName;
    }

    /**
     * @return crt_user
     */
    public String getCrtUser() {
        return crtUser;
    }

    /**
     * @param crtUser
     */
    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    /**
     * @return crt_host
     */
    public String getCrtHost() {
        return crtHost;
    }

    /**
     * @param crtHost
     */
    public void setCrtHost(String crtHost) {
        this.crtHost = crtHost;
    }

    /**
     * @return crt_time
     */
    public Date getCrtTime() {
        return crtTime;
    }

    /**
     * @param crtTime
     */
    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    /**
     * @return upd_time
     */
    public Date getUpdTime() {
        return updTime;
    }

    /**
     * @param updTime
     */
    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    /**
     * @return upd_user
     */
    public String getUpdUser() {
        return updUser;
    }

    /**
     * @param updUser
     */
    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    /**
     * @return upd_name
     */
    public String getUpdName() {
        return updName;
    }

    /**
     * @param updName
     */
    public void setUpdName(String updName) {
        this.updName = updName;
    }

    /**
     * @return upd_host
     */
    public String getUpdHost() {
        return updHost;
    }

    /**
     * @param updHost
     */
    public void setUpdHost(String updHost) {
        this.updHost = updHost;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
}