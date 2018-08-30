package com.service.service.entity;

import com.service.service.enumeration.impl.CommentType;

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
@Table(name = "comment")
public class CommentEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //
    @Id
    private Integer id;
	
	    //
    @Column(name = "comment_type")
    private CommentType commentType;
	
	    //
    @Column(name = "poster_id")
    private Integer posterId;
	
	    //
    @Column(name = "issue_id")
    private Integer issueId;
	
	    //
    @Column(name = "commit_id")
    private Integer commitId;
	
	    //
    @Column(name = "line")
    private Integer line;
	
	    //
    @Column(name = "content")
    private String content;
	
	    //
    @Column(name = "created_unix")
    private Integer createdUnix;
	
	    //
    @Column(name = "updated_unix")
    private Integer updatedUnix;
	
	    //
    @Column(name = "commit_sha")
    private String commitSha;
	
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
	public void setCommentType(CommentType commentType) {
		this.commentType = commentType;
	}
	/**
	 * 获取：
	 */
	public CommentType getCommentType() {
		return commentType;
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
	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}
	/**
	 * 获取：
	 */
	public Integer getIssueId() {
		return issueId;
	}
	/**
	 * 设置：
	 */
	public void setCommitId(Integer commitId) {
		this.commitId = commitId;
	}
	/**
	 * 获取：
	 */
	public Integer getCommitId() {
		return commitId;
	}
	/**
	 * 设置：
	 */
	public void setLine(Integer line) {
		this.line = line;
	}
	/**
	 * 获取：
	 */
	public Integer getLine() {
		return line;
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
	public void setCommitSha(String commitSha) {
		this.commitSha = commitSha;
	}
	/**
	 * 获取：
	 */
	public String getCommitSha() {
		return commitSha;
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
