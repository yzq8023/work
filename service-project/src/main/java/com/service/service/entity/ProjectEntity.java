package com.service.service.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "project")
public class ProjectEntity {
    @Id
    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "project_creator_id")
    private Integer projectCreatorId;

    @Column(name = "project_des")
    private String projectDes;

    @Column(name = "project_group_id")
    private Integer projectGroupId;

    @Column(name = "project_label")
    private String projectLabel;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_phase")
    private Short projectPhase;

    @Column(name = "project_plan_end")
    private Date projectPlanEnd;

    @Column(name = "project_process")
    private Integer projectProcess;

    @Column(name = "project_resource_id")
    private Integer projectResourceId;

    @Column(name = "project_state")
    private Boolean projectState;

    @Column(name = "project_time_end")
    private Date projectTimeEnd;

    @Column(name = "project_time_start")
    private Date projectTimeStart;

    @Column(name = "project_type")
    private Byte projectType;

    @Column(name = "project_user_id")
    private Integer projectUserId;

    @Column(name = "default_branch")
    private String defaultBranch;

    @Column(name = "project_size")
    private Long size;

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

    /**
     * @return project_id
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * @param projectId
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * @return project_creator_id
     */
    public Integer getProjectCreatorId() {
        return projectCreatorId;
    }

    /**
     * @param projectCreatorId
     */
    public void setProjectCreatorId(Integer projectCreatorId) {
        this.projectCreatorId = projectCreatorId;
    }

    /**
     * @return project_des
     */
    public String getProjectDes() {
        return projectDes;
    }

    /**
     * @param projectDes
     */
    public void setProjectDes(String projectDes) {
        this.projectDes = projectDes;
    }

    /**
     * @return project_group_id
     */
    public Integer getProjectGroupId() {
        return projectGroupId;
    }

    /**
     * @param projectGroupId
     */
    public void setProjectGroupId(Integer projectGroupId) {
        this.projectGroupId = projectGroupId;
    }

    /**
     * @return project_label
     */
    public String getProjectLabel() {
        return projectLabel;
    }

    /**
     * @param projectLabel
     */
    public void setProjectLabel(String projectLabel) {
        this.projectLabel = projectLabel;
    }

    /**
     * @return project_name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return project_phase
     */
    public Short getProjectPhase() {
        return projectPhase;
    }

    /**
     * @param projectPhase
     */
    public void setProjectPhase(Short projectPhase) {
        this.projectPhase = projectPhase;
    }

    /**
     * @return project_plan_end
     */
    public Date getProjectPlanEnd() {
        return projectPlanEnd;
    }

    /**
     * @param projectPlanEnd
     */
    public void setProjectPlanEnd(Date projectPlanEnd) {
        this.projectPlanEnd = projectPlanEnd;
    }

    /**
     * @return project_process
     */
    public Integer getProjectProcess() {
        return projectProcess;
    }

    /**
     * @param projectProcess
     */
    public void setProjectProcess(Integer projectProcess) {
        this.projectProcess = projectProcess;
    }

    /**
     * @return project_resource_id
     */
    public Integer getProjectResourceId() {
        return projectResourceId;
    }

    /**
     * @param projectResourceId
     */
    public void setProjectResourceId(Integer projectResourceId) {
        this.projectResourceId = projectResourceId;
    }

    /**
     * @return project_state
     */
    public Boolean getProjectState() {
        return projectState;
    }

    /**
     * @param projectState
     */
    public void setProjectState(Boolean projectState) {
        this.projectState = projectState;
    }

    /**
     * @return project_time_end
     */
    public Date getProjectTimeEnd() {
        return projectTimeEnd;
    }

    /**
     * @param projectTimeEnd
     */
    public void setProjectTimeEnd(Date projectTimeEnd) {
        this.projectTimeEnd = projectTimeEnd;
    }

    /**
     * @return project_time_start
     */
    public Date getProjectTimeStart() {
        return projectTimeStart;
    }

    /**
     * @param projectTimeStart
     */
    public void setProjectTimeStart(Date projectTimeStart) {
        this.projectTimeStart = projectTimeStart;
    }

    /**
     * @return project_type
     */
    public Byte getProjectType() {
        return projectType;
    }

    /**
     * @param projectType
     */
    public void setProjectType(Byte projectType) {
        this.projectType = projectType;
    }

    /**
     * @return project_user_id
     */
    public Integer getProjectUserId() {
        return projectUserId;
    }

    /**
     * @param projectUserId
     */
    public void setProjectUserId(Integer projectUserId) {
        this.projectUserId = projectUserId;
    }

    /**
     * @return default_branch
     */
    public String getDefaultBranch() {
        return defaultBranch;
    }

    /**
     * @param defaultBranch
     */
    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    /**
     * @return size
     */
    public Long getSize() {
        return size;
    }

    /**
     * @param size
     */
    public void setSize(Long size) {
        this.size = size;
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
}