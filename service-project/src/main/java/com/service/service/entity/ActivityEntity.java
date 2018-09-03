package com.service.service.entity;

import javax.persistence.*;

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