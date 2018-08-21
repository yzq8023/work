package com.service.service.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 
 * 
 * @author
 * @date 2018-08-20 16:05:04
 */
@Table(name = "issue")
public class IssueEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //
    @Id
    private Integer id;
	
	    //
    @Column(name = "repo_id")
    private Integer repoId;
	
	    //
    @Column(name = "issue_index")
    private Integer issue_index;
	
	    //
    @Column(name = "poster_id")
    private Integer posterId;
	
	    //
    @Column(name = "name")
    private String name;
	
	    //
    @Column(name = "content")
    private String content;
	
	    //
    @Column(name = "milestone_id")
    private Integer milestoneId;
	
	    //
    @Column(name = "priority")
    private Integer priority;
	
	    //
    @Column(name = "assignee_id")
    private Integer assigneeId;
	
	    //
    @Column(name = "is_closed")
    private Integer isClosed;
	
	    //
    @Column(name = "is_pull")
    private Integer isPull;
	
	    //
    @Column(name = "num_comments")
    private Integer numComments;
	
	    //
    @Column(name = "deadline_unix")
    private Integer deadlineUnix;
	
	    //
    @Column(name = "created_unix")
    private Integer createdUnix;
	
	    //
    @Column(name = "updated_unix")
    private Integer updatedUnix;
	
	    //
    @Column(name = "crt_name")
    private String crtName;
	
	    //
    @Column(name = "crt_user")
    private String crtUser;
	
	    //
    @Column(name = "crt_host")
    private String crtHost;
	
	    //
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //
    @Column(name = "upd_time")
    private Date updTime;
	
	    //
    @Column(name = "upd_user")
    private String updUser;
	
	    //
    @Column(name = "upd_name")
    private String updName;
	
	    //
    @Column(name = "upd_host")
    private String updHost;
	

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setRepoId(Integer repoId) {
		this.repoId = repoId;
	}
	/**
	 * 获取：
	 */
	public Integer getRepoId() {
		return repoId;
	}
	/**
	 * 设置：
	 */
	public void setIndex(Integer index) {
		this.issue_index = index;
	}
	/**
	 * 获取：
	 */
	public Integer getIndex() {
		return issue_index;
	}
	/**
	 * 设置：
	 */
	public void setPosterId(Integer posterId) {
		this.posterId = posterId;
	}
	/**
	 * 获取：
	 */
	public Integer getPosterId() {
		return posterId;
	}
	/**
	 * 设置：
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：
	 */
	public void setMilestoneId(Integer milestoneId) {
		this.milestoneId = milestoneId;
	}
	/**
	 * 获取：
	 */
	public Integer getMilestoneId() {
		return milestoneId;
	}
	/**
	 * 设置：
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	/**
	 * 获取：
	 */
	public Integer getPriority() {
		return priority;
	}
	/**
	 * 设置：
	 */
	public void setAssigneeId(Integer assigneeId) {
		this.assigneeId = assigneeId;
	}
	/**
	 * 获取：
	 */
	public Integer getAssigneeId() {
		return assigneeId;
	}
	/**
	 * 设置：
	 */
	public void setIsClosed(Integer isClosed) {
		this.isClosed = isClosed;
	}
	/**
	 * 获取：
	 */
	public Integer getIsClosed() {
		return isClosed;
	}
	/**
	 * 设置：
	 */
	public void setIsPull(Integer isPull) {
		this.isPull = isPull;
	}
	/**
	 * 获取：
	 */
	public Integer getIsPull() {
		return isPull;
	}
	/**
	 * 设置：
	 */
	public void setNumComments(Integer numComments) {
		this.numComments = numComments;
	}
	/**
	 * 获取：
	 */
	public Integer getNumComments() {
		return numComments;
	}
	/**
	 * 设置：
	 */
	public void setDeadlineUnix(Integer deadlineUnix) {
		this.deadlineUnix = deadlineUnix;
	}
	/**
	 * 获取：
	 */
	public Integer getDeadlineUnix() {
		return deadlineUnix;
	}
	/**
	 * 设置：
	 */
	public void setCreatedUnix(Integer createdUnix) {
		this.createdUnix = createdUnix;
	}
	/**
	 * 获取：
	 */
	public Integer getCreatedUnix() {
		return createdUnix;
	}
	/**
	 * 设置：
	 */
	public void setUpdatedUnix(Integer updatedUnix) {
		this.updatedUnix = updatedUnix;
	}
	/**
	 * 获取：
	 */
	public Integer getUpdatedUnix() {
		return updatedUnix;
	}
	/**
	 * 设置：
	 */
	public void setCrtName(String crtName) {
		this.crtName = crtName;
	}
	/**
	 * 获取：
	 */
	public String getCrtName() {
		return crtName;
	}
	/**
	 * 设置：
	 */
	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}
	/**
	 * 获取：
	 */
	public String getCrtUser() {
		return crtUser;
	}
	/**
	 * 设置：
	 */
	public void setCrtHost(String crtHost) {
		this.crtHost = crtHost;
	}
	/**
	 * 获取：
	 */
	public String getCrtHost() {
		return crtHost;
	}
	/**
	 * 设置：
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}
	/**
	 * 获取：
	 */
	public Date getCrtTime() {
		return crtTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdTime() {
		return updTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}
	/**
	 * 获取：
	 */
	public String getUpdUser() {
		return updUser;
	}
	/**
	 * 设置：
	 */
	public void setUpdName(String updName) {
		this.updName = updName;
	}
	/**
	 * 获取：
	 */
	public String getUpdName() {
		return updName;
	}
	/**
	 * 设置：
	 */
	public void setUpdHost(String updHost) {
		this.updHost = updHost;
	}
	/**
	 * 获取：
	 */
	public String getUpdHost() {
		return updHost;
	}
}
