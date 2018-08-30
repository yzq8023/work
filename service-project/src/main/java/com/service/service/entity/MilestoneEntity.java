package com.service.service.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 
 * 
 * @author
 * @email
 * @date 2018-08-27 10:41:16
 */
@Table(name = "milestone")
public class MilestoneEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //
    @Id
    private Integer id;
	
	    //
    @Column(name = "task_id")
    private Integer taskId;
	
	    //
    @Column(name = "name")
    private String name;
	
	    //
    @Column(name = "content")
    private String content;
	
	    //
    @Column(name = "is_closed")
    private Integer isClosed;
	
	    //
    @Column(name = "num_issues")
    private Integer numIssues;
	
	    //
    @Column(name = "num_closed_issues")
    private Integer numClosedIssues;
	
	    //
    @Column(name = "completeness")
    private Integer completeness;
	
	    //
    @Column(name = "deadline_unix")
    private Long deadlineUnix;
	
	    //
    @Column(name = "closed_date_unix")
    private Long closedDateUnix;
	
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
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	/**
	 * 获取：
	 */
	public Integer getTaskId() {
		return taskId;
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
	public void setNumIssues(Integer numIssues) {
		this.numIssues = numIssues;
	}
	/**
	 * 获取：
	 */
	public Integer getNumIssues() {
		return numIssues;
	}
	/**
	 * 设置：
	 */
	public void setNumClosedIssues(Integer numClosedIssues) {
		this.numClosedIssues = numClosedIssues;
	}
	/**
	 * 获取：
	 */
	public Integer getNumClosedIssues() {
		return numClosedIssues;
	}
	/**
	 * 设置：
	 */
	public void setCompleteness(Integer completeness) {
		this.completeness = completeness;
	}
	/**
	 * 获取：
	 */
	public Integer getCompleteness() {
		return completeness;
	}
	/**
	 * 设置：
	 */
	public void setDeadlineUnix(Long deadlineUnix) {
		this.deadlineUnix = deadlineUnix;
	}
	/**
	 * 获取：
	 */
	public Long getDeadlineUnix() {
		return deadlineUnix;
	}
	/**
	 * 设置：
	 */
	public void setClosedDateUnix(Long closedDateUnix) {
		this.closedDateUnix = closedDateUnix;
	}
	/**
	 * 获取：
	 */
	public Long getClosedDateUnix() {
		return closedDateUnix;
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
