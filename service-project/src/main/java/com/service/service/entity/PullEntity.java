package com.service.service.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "pull_request")
public class PullEntity {
    @Id
    private Integer id;

    private Integer type;

    private Integer status;

    @Column(name = "issue_id")
    private Long issueId;

    private Long index;

    @Column(name = "head_repo_id")
    private Long headRepoId;

    @Column(name = "base_repo_id")
    private Long baseRepoId;

    @Column(name = "head_user_name")
    private String headUserName;

    @Column(name = "head_branch")
    private String headBranch;

    @Column(name = "base_branch")
    private String baseBranch;

    @Column(name = "merge_base")
    private String mergeBase;

    @Column(name = "has_merged")
    private Boolean hasMerged;

    @Column(name = "merged_commit_id")
    private String mergedCommitId;

    @Column(name = "merger_id")
    private Long mergerId;

    @Column(name = "merged_unix")
    private Long mergedUnix;

    private String title;

    private String context;

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
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return issue_id
     */
    public Long getIssueId() {
        return issueId;
    }

    /**
     * @param issueId
     */
    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    /**
     * @return index
     */
    public Long getIndex() {
        return index;
    }

    /**
     * @param index
     */
    public void setIndex(Long index) {
        this.index = index;
    }

    /**
     * @return head_repo_id
     */
    public Long getHeadRepoId() {
        return headRepoId;
    }

    /**
     * @param headRepoId
     */
    public void setHeadRepoId(Long headRepoId) {
        this.headRepoId = headRepoId;
    }

    /**
     * @return base_repo_id
     */
    public Long getBaseRepoId() {
        return baseRepoId;
    }

    /**
     * @param baseRepoId
     */
    public void setBaseRepoId(Long baseRepoId) {
        this.baseRepoId = baseRepoId;
    }

    /**
     * @return head_user_name
     */
    public String getHeadUserName() {
        return headUserName;
    }

    /**
     * @param headUserName
     */
    public void setHeadUserName(String headUserName) {
        this.headUserName = headUserName;
    }

    /**
     * @return head_branch
     */
    public String getHeadBranch() {
        return headBranch;
    }

    /**
     * @param headBranch
     */
    public void setHeadBranch(String headBranch) {
        this.headBranch = headBranch;
    }

    /**
     * @return base_branch
     */
    public String getBaseBranch() {
        return baseBranch;
    }

    /**
     * @param baseBranch
     */
    public void setBaseBranch(String baseBranch) {
        this.baseBranch = baseBranch;
    }

    /**
     * @return merge_base
     */
    public String getMergeBase() {
        return mergeBase;
    }

    /**
     * @param mergeBase
     */
    public void setMergeBase(String mergeBase) {
        this.mergeBase = mergeBase;
    }

    /**
     * @return has_merged
     */
    public Boolean getHasMerged() {
        return hasMerged;
    }

    /**
     * @param hasMerged
     */
    public void setHasMerged(Boolean hasMerged) {
        this.hasMerged = hasMerged;
    }

    /**
     * @return merged_commit_id
     */
    public String getMergedCommitId() {
        return mergedCommitId;
    }

    /**
     * @param mergedCommitId
     */
    public void setMergedCommitId(String mergedCommitId) {
        this.mergedCommitId = mergedCommitId;
    }

    /**
     * @return merger_id
     */
    public Long getMergerId() {
        return mergerId;
    }

    /**
     * @param mergerId
     */
    public void setMergerId(Long mergerId) {
        this.mergerId = mergerId;
    }

    /**
     * @return merged_unix
     */
    public Long getMergedUnix() {
        return mergedUnix;
    }

    /**
     * @param mergedUnix
     */
    public void setMergedUnix(Long mergedUnix) {
        this.mergedUnix = mergedUnix;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return context
     */
    public String getContext() {
        return context;
    }

    /**
     * @param context
     */
    public void setContext(String context) {
        this.context = context;
    }
}