package com.service.service.entity;

import com.service.service.Constants;
import com.service.service.biz.TaskBiz;
import com.service.service.utils.ArrayUtils;
import com.service.service.utils.ModelUtils;
import com.service.service.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.*;

@Table(name = "task")
public class TaskEntity implements Serializable, Comparable<TaskEntity> {
    @Id
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 暂不使用
     */
    @Column(name = "task_creator_id")
    private Integer taskCreatorId;

    /**
     * 任务描述
     */
    @Column(name = "task_des")
    private String taskDes;

    /**
     * 任务执行人
     */
    @Column(name = "task_executor_id")
    private Integer taskExecutorId;

    @Column(name = "task_name")
    private String taskName;

    /**
     * 爹任务ID
     */
    @Column(name = "task_parent_id")
    private Integer taskParentId;

    /**
     * 计划结束时间
     */
    @Column(name = "task_plan_end")
    private Date taskPlanEnd;

    /**
     * 任务进度
     */
    @Column(name = "task_process")
    private Integer taskProcess;

    /**
     * 所属项目名称
     */
    @Column(name = "task_project_name")
    private String taskProjectName;

    /**
     * 所属项目ID
     */
    @Column(name = "task_project_id")
    private Integer taskProjectId;

    /**
     * 任务资源ID
     */
    @Column(name = "task_resource_id")
    private Integer taskResourceId;

    /**
     * 任务状态
     */
    @Column(name = "task_state")
    private Byte taskState;

    /**
     * 任务实际结束时间
     */
    @Column(name = "task_time_end")
    private Date taskTimeEnd;

    /**
     * 任务实际开始时间
     */
    @Column(name = "task_time_start")
    private Date taskTimeStart;

    /**
     * 任务类型
     */
    @Column(name = "task_type")
    private Byte taskType;

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
     * 默认分支
     */
    @Column(name = "default_branch")
    private String defaultBranch;

    /**
     * 大小
     */
    private String size;

    /**
     * 关注量
     */
    @Column(name = "num_watches")
    private String numWatches;

    /**
     * 标星量
     */
    @Column(name = "num_stars")
    private Integer numStars;

    /**
     * 分支任务数
     */
    @Column(name = "num_forks")
    private Integer numForks;

    /**
     * 问题量
     */
    @Column(name = "num_issues")
    private Integer numIssues;

    /**
     * 关闭问题量
     */
    @Column(name = "num_closed_issues")
    private Integer numClosedIssues;

    /**
     * 拉取量
     */
    @Column(name = "num_pulls")
    private Integer numPulls;

    /**
     * 关闭拉取量
     */
    @Column(name = "num_closed_pulls")
    private Integer numClosedPulls;

    /**
     * 里程碑数
     */
    @Column(name = "num_milestones")
    private Integer numMilestones;

    /**
     * 关闭里程碑量
     */
    @Column(name = "num_closed_milestones")
    private Integer numClosedMilestones;

    /**
     * 是否允许提问
     */
    @Column(name = "enable_issues")
    private Boolean enableIssues;

    /**
     * 允许公共问题
     */
    @Column(name = "allow_public_issues")
    private Boolean allowPublicIssues;

    /**
     * 重用
     */
    @Column(name = "is_fork")
    private Boolean isFork;

    /**
     * 重用id
     */
    @Column(name = "fork_id")
    private Integer forkId;

    private String head;

    @Column(name = "merge_to")
    private String mergeTo;

    /**
     * 项目路径
     */
    @Column(name = "project_path")
    private String projectPath;

    /**
     * 是否子任务
     */
    @Column(name = "task_is_leaf")
    private byte[] taskIsLeaf;
    /**
     * 访问限制
     VIEW
     PUSH
     CLONE
     NONE
     */
    @Transient
    private Constants.AccessRestrictionType accessRestriction;
    @Transient
    private Constants.AuthorizationControl authorizationControl;
    @Transient
    private List<String> owners;
    @Transient
    private Constants.MergeType mergeType;
    @Transient
    private String originRepository;
    @Transient
    private int maxActivityCommits;
    @Transient
    private boolean isBare;
    @Transient
    private boolean isMirror;
    @Transient
    private boolean isFrozen;
    @Transient
    private boolean allowForks;
    @Transient
    private Set<String> forks;
    @Transient
    private boolean hasCommits;
    @Transient
    private boolean acceptNewPatchsets;
    @Transient
    private boolean acceptNewTickets;
    @Transient
    private boolean requireApproval;
    @Transient
    private boolean useIncrementalPushTags;
    @Transient
    private String incrementalPushTagPrefix;
    @Transient
    private boolean verifyCommitter;
    @Transient
    private List<String> availableRefs;
    @Transient
    private List<String> indexedBranches;
    @Transient
    private String origin;
    @Transient
    private Constants.FederationStrategy federationStrategy;
    @Transient
    private List<String> federationSets;
    @Transient
    private boolean isFederated;
    @Transient
    private boolean skipSizeCalculation;
    @Transient
    private boolean skipSummaryMetrics;
    @Transient
    private String frequency;
    @Transient
    private Constants.CommitMessageRenderer commitMessageRenderer;
    @Transient
    private Date lastChange;
    @Transient
    private List<String> metricAuthorExclusions;
    @Transient
    private boolean isCollectingGarbage;

    public TaskEntity() {
        this(0, "", new Date(0), "", "");
    }

    public TaskEntity(Integer taskId,
                      String taskName,
                      Date updTime,
                      String taskDes,
                      String crtUser) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.updTime = updTime;
        this.accessRestriction = Constants.AccessRestrictionType.NONE;
        this.authorizationControl = Constants.AuthorizationControl.NAMED;
        this.projectPath = StringUtils.getFirstPathElement(taskName);
        this.isBare = true;
        this.crtUser = crtUser;
        this.taskDes = taskDes;
        this.owners = new ArrayList<String>();
        this.mergeType = Constants.MergeType.DEFAULT_MERGE_TYPE;
        addOwner(crtUser);
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
     * 获取暂不使用
     *
     * @return task_creator_id - 暂不使用
     */
    public Integer getTaskCreatorId() {
        return taskCreatorId;
    }

    /**
     * 设置暂不使用
     *
     * @param taskCreatorId 暂不使用
     */
    public void setTaskCreatorId(Integer taskCreatorId) {
        this.taskCreatorId = taskCreatorId;
    }

    /**
     * 获取任务描述
     *
     * @return task_des - 任务描述
     */
    public String getTaskDes() {
        return taskDes;
    }

    /**
     * 设置任务描述
     *
     * @param taskDes 任务描述
     */
    public void setTaskDes(String taskDes) {
        this.taskDes = taskDes;
    }

    /**
     * 获取任务执行人
     *
     * @return task_executor_id - 任务执行人
     */
    public Integer getTaskExecutorId() {
        return taskExecutorId;
    }

    /**
     * 设置任务执行人
     *
     * @param taskExecutorId 任务执行人
     */
    public void setTaskExecutorId(Integer taskExecutorId) {
        this.taskExecutorId = taskExecutorId;
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

    /**
     * 获取爹任务ID
     *
     * @return task_parent_id - 爹任务ID
     */
    public Integer getTaskParentId() {
        return taskParentId;
    }

    /**
     * 设置爹任务ID
     *
     * @param taskParentId 爹任务ID
     */
    public void setTaskParentId(Integer taskParentId) {
        this.taskParentId = taskParentId;
    }

    /**
     * 获取计划结束时间
     *
     * @return task_plan_end - 计划结束时间
     */
    public Date getTaskPlanEnd() {
        return taskPlanEnd;
    }

    /**
     * 设置计划结束时间
     *
     * @param taskPlanEnd 计划结束时间
     */
    public void setTaskPlanEnd(Date taskPlanEnd) {
        this.taskPlanEnd = taskPlanEnd;
    }

    /**
     * 获取任务进度
     *
     * @return task_process - 任务进度
     */
    public Integer getTaskProcess() {
        return taskProcess;
    }

    /**
     * 设置任务进度
     *
     * @param taskProcess 任务进度
     */
    public void setTaskProcess(Integer taskProcess) {
        this.taskProcess = taskProcess;
    }

    /**
     * 获取所属项目名称
     *
     * @return task_project_name - 所属项目名称
     */
    public String getTaskProjectName() {
        return taskProjectName;
    }

    /**
     * 设置所属项目名称
     *
     * @param taskProjectName 所属项目名称
     */
    public void setTaskProjectName(String taskProjectName) {
        this.taskProjectName = taskProjectName;
    }

    /**
     * 获取所属项目ID
     *
     * @return task_project_id - 所属项目ID
     */
    public Integer getTaskProjectId() {
        return taskProjectId;
    }

    /**
     * 设置所属项目ID
     *
     * @param taskProjectId 所属项目ID
     */
    public void setTaskProjectId(Integer taskProjectId) {
        this.taskProjectId = taskProjectId;
    }

    /**
     * 获取任务资源ID
     *
     * @return task_resource_id - 任务资源ID
     */
    public Integer getTaskResourceId() {
        return taskResourceId;
    }

    /**
     * 设置任务资源ID
     *
     * @param taskResourceId 任务资源ID
     */
    public void setTaskResourceId(Integer taskResourceId) {
        this.taskResourceId = taskResourceId;
    }

    /**
     * 获取任务状态
     *
     * @return task_state - 任务状态
     */
    public Byte getTaskState() {
        return taskState;
    }

    /**
     * 设置任务状态
     *
     * @param taskState 任务状态
     */
    public void setTaskState(Byte taskState) {
        this.taskState = taskState;
    }

    /**
     * 获取任务实际结束时间
     *
     * @return task_time_end - 任务实际结束时间
     */
    public Date getTaskTimeEnd() {
        return taskTimeEnd;
    }

    /**
     * 设置任务实际结束时间
     *
     * @param taskTimeEnd 任务实际结束时间
     */
    public void setTaskTimeEnd(Date taskTimeEnd) {
        this.taskTimeEnd = taskTimeEnd;
    }

    /**
     * 获取任务实际开始时间
     *
     * @return task_time_start - 任务实际开始时间
     */
    public Date getTaskTimeStart() {
        return taskTimeStart;
    }

    /**
     * 设置任务实际开始时间
     *
     * @param taskTimeStart 任务实际开始时间
     */
    public void setTaskTimeStart(Date taskTimeStart) {
        this.taskTimeStart = taskTimeStart;
    }

    /**
     * 获取任务类型
     *
     * @return task_type - 任务类型
     */
    public Byte getTaskType() {
        return taskType;
    }

    /**
     * 设置任务类型
     *
     * @param taskType 任务类型
     */
    public void setTaskType(Byte taskType) {
        this.taskType = taskType;
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
     * 获取默认分支
     *
     * @return default_branch - 默认分支
     */
    public String getDefaultBranch() {
        return defaultBranch;
    }

    /**
     * 设置默认分支
     *
     * @param defaultBranch 默认分支
     */
    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    /**
     * 获取大小
     *
     * @return size - 大小
     */
    public String getSize() {
        return size;
    }

    /**
     * 设置大小
     *
     * @param size 大小
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * 获取关注量
     *
     * @return num_watches - 关注量
     */
    public String getNumWatches() {
        return numWatches;
    }

    /**
     * 设置关注量
     *
     * @param numWatches 关注量
     */
    public void setNumWatches(String numWatches) {
        this.numWatches = numWatches;
    }

    /**
     * 获取标星量
     *
     * @return num_stars - 标星量
     */
    public Integer getNumStars() {
        return numStars;
    }

    /**
     * 设置标星量
     *
     * @param numStars 标星量
     */
    public void setNumStars(Integer numStars) {
        this.numStars = numStars;
    }

    /**
     * 获取分支任务数
     *
     * @return num_forks - 分支任务数
     */
    public Integer getNumForks() {
        return numForks;
    }

    /**
     * 设置分支任务数
     *
     * @param numForks 分支任务数
     */
    public void setNumForks(Integer numForks) {
        this.numForks = numForks;
    }

    /**
     * 获取问题量
     *
     * @return num_issues - 问题量
     */
    public Integer getNumIssues() {
        return numIssues;
    }

    /**
     * 设置问题量
     *
     * @param numIssues 问题量
     */
    public void setNumIssues(Integer numIssues) {
        this.numIssues = numIssues;
    }

    /**
     * 获取关闭问题量
     *
     * @return num_closed_issues - 关闭问题量
     */
    public Integer getNumClosedIssues() {
        return numClosedIssues;
    }

    /**
     * 设置关闭问题量
     *
     * @param numClosedIssues 关闭问题量
     */
    public void setNumClosedIssues(Integer numClosedIssues) {
        this.numClosedIssues = numClosedIssues;
    }

    /**
     * 获取拉取量
     *
     * @return num_pulls - 拉取量
     */
    public Integer getNumPulls() {
        return numPulls;
    }

    /**
     * 设置拉取量
     *
     * @param numPulls 拉取量
     */
    public void setNumPulls(Integer numPulls) {
        this.numPulls = numPulls;
    }

    /**
     * 获取关闭拉取量
     *
     * @return num_closed_pulls - 关闭拉取量
     */
    public Integer getNumClosedPulls() {
        return numClosedPulls;
    }

    /**
     * 设置关闭拉取量
     *
     * @param numClosedPulls 关闭拉取量
     */
    public void setNumClosedPulls(Integer numClosedPulls) {
        this.numClosedPulls = numClosedPulls;
    }

    /**
     * 获取里程碑数
     *
     * @return num_milestones - 里程碑数
     */
    public Integer getNumMilestones() {
        return numMilestones;
    }

    /**
     * 设置里程碑数
     *
     * @param numMilestones 里程碑数
     */
    public void setNumMilestones(Integer numMilestones) {
        this.numMilestones = numMilestones;
    }

    /**
     * 获取关闭里程碑量
     *
     * @return num_closed_milestones - 关闭里程碑量
     */
    public Integer getNumClosedMilestones() {
        return numClosedMilestones;
    }

    /**
     * 设置关闭里程碑量
     *
     * @param numClosedMilestones 关闭里程碑量
     */
    public void setNumClosedMilestones(Integer numClosedMilestones) {
        this.numClosedMilestones = numClosedMilestones;
    }

    /**
     * 获取是否允许提问
     *
     * @return enable_issues - 是否允许提问
     */
    public Boolean getEnableIssues() {
        return enableIssues;
    }

    /**
     * 设置是否允许提问
     *
     * @param enableIssues 是否允许提问
     */
    public void setEnableIssues(Boolean enableIssues) {
        this.enableIssues = enableIssues;
    }

    /**
     * 获取允许公共问题
     *
     * @return allow_public_issues - 允许公共问题
     */
    public Boolean getAllowPublicIssues() {
        return allowPublicIssues;
    }

    /**
     * 设置允许公共问题
     *
     * @param allowPublicIssues 允许公共问题
     */
    public void setAllowPublicIssues(Boolean allowPublicIssues) {
        this.allowPublicIssues = allowPublicIssues;
    }

    /**
     * 获取重用
     *
     * @return is_fork - 重用
     */
    public Boolean getIsFork() {
        return isFork;
    }

    /**
     * 设置重用
     *
     * @param isFork 重用
     */
    public void setIsFork(Boolean isFork) {
        this.isFork = isFork;
    }

    /**
     * 获取重用id
     *
     * @return fork_id - 重用id
     */
    public Integer getForkId() {
        return forkId;
    }

    /**
     * 设置重用id
     *
     * @param forkId 重用id
     */
    public void setForkId(Integer forkId) {
        this.forkId = forkId;
    }

    /**
     * @return head
     */
    public String getHead() {
        return head;
    }

    /**
     * @param head
     */
    public void setHead(String head) {
        this.head = head;
    }

    /**
     * @return merge_to
     */
    public String getMergeTo() {
        return mergeTo;
    }

    /**
     * @param mergeTo
     */
    public void setMergeTo(String mergeTo) {
        this.mergeTo = mergeTo;
    }

    /**
     * 获取项目路径
     *
     * @return project_path - 项目路径
     */
    public String getProjectPath() {
        return projectPath;
    }

    /**
     * 设置项目路径
     *
     * @param projectPath 项目路径
     */
    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    /**
     * 获取访问限制
     *
     * @return access_restriction - 访问限制
     */
    public Constants.AccessRestrictionType getAccessRestriction() {
        return accessRestriction;
    }

    /**
     * 设置访问限制
     *
     * @param accessRestriction 访问限制
     */
    public void setAccessRestriction(Constants.AccessRestrictionType accessRestriction) {
        this.accessRestriction = accessRestriction;
    }

    /**
     * @return authorization_control
     */
    public Constants.AuthorizationControl getAuthorizationControl() {
        return authorizationControl;
    }

    /**
     * @param authorizationControl
     */
    public void setAuthorizationControl(Constants.AuthorizationControl authorizationControl) {
        this.authorizationControl = authorizationControl;
    }

    /**
     * 获取是否子任务
     *
     * @return task_is_leaf - 是否子任务
     */
    public byte[] getTaskIsLeaf() {
        return taskIsLeaf;
    }

    /**
     * 设置是否子任务
     *
     * @param taskIsLeaf 是否子任务
     */
    public void setTaskIsLeaf(byte[] taskIsLeaf) {
        this.taskIsLeaf = taskIsLeaf;
    }


    public Boolean getFork() {
        return isFork;
    }

    public void setFork(Boolean fork) {
        isFork = fork;
    }

    public List<String> getOwners() {
        return owners;
    }

    public void setOwners(List<String> owners) {
        this.owners = owners;
    }

    public Constants.MergeType getMergeType() {
        return mergeType;
    }

    public void setMergeType(Constants.MergeType mergeType) {
        this.mergeType = mergeType;
    }

    public String getOriginRepository() {
        return originRepository;
    }

    public void setOriginRepository(String originRepository) {
        this.originRepository = originRepository;
    }

    public int getMaxActivityCommits() {
        return maxActivityCommits;
    }

    public void setMaxActivityCommits(int maxActivityCommits) {
        this.maxActivityCommits = maxActivityCommits;
    }

    public boolean isBare() {
        return isBare;
    }

    public void setBare(boolean bare) {
        isBare = bare;
    }

    public boolean isMirror() {
        return isMirror;
    }

    public void setMirror(boolean mirror) {
        isMirror = mirror;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public boolean isAllowForks() {
        return allowForks;
    }

    public void setAllowForks(boolean allowForks) {
        this.allowForks = allowForks;
    }

    public Set<String> getForks() {
        return forks;
    }

    public void setForks(Set<String> forks) {
        this.forks = forks;
    }

    public boolean isHasCommits() {
        return hasCommits;
    }

    public void setHasCommits(boolean hasCommits) {
        this.hasCommits = hasCommits;
    }

    public boolean isAcceptNewPatchsets() {
        return acceptNewPatchsets;
    }

    public void setAcceptNewPatchsets(boolean acceptNewPatchsets) {
        this.acceptNewPatchsets = acceptNewPatchsets;
    }

    public boolean isAcceptNewTickets() {
        return acceptNewTickets;
    }

    public void setAcceptNewTickets(boolean acceptNewTickets) {
        this.acceptNewTickets = acceptNewTickets;
    }

    public boolean isRequireApproval() {
        return requireApproval;
    }

    public void setRequireApproval(boolean requireApproval) {
        this.requireApproval = requireApproval;
    }

    public boolean isUseIncrementalPushTags() {
        return useIncrementalPushTags;
    }

    public void setUseIncrementalPushTags(boolean useIncrementalPushTags) {
        this.useIncrementalPushTags = useIncrementalPushTags;
    }

    public String getIncrementalPushTagPrefix() {
        return incrementalPushTagPrefix;
    }

    public void setIncrementalPushTagPrefix(String incrementalPushTagPrefix) {
        this.incrementalPushTagPrefix = incrementalPushTagPrefix;
    }

    public boolean isVerifyCommitter() {
        return verifyCommitter;
    }

    public void setVerifyCommitter(boolean verifyCommitter) {
        this.verifyCommitter = verifyCommitter;
    }

    public List<String> getAvailableRefs() {
        return availableRefs;
    }

    public void setAvailableRefs(List<String> availableRefs) {
        this.availableRefs = availableRefs;
    }

    public List<String> getIndexedBranches() {
        return indexedBranches;
    }

    public void setIndexedBranches(List<String> indexedBranches) {
        this.indexedBranches = indexedBranches;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Constants.FederationStrategy getFederationStrategy() {
        return federationStrategy;
    }

    public void setFederationStrategy(Constants.FederationStrategy federationStrategy) {
        this.federationStrategy = federationStrategy;
    }

    public List<String> getFederationSets() {
        return federationSets;
    }

    public void setFederationSets(List<String> federationSets) {
        this.federationSets = federationSets;
    }

    public boolean isFederated() {
        return isFederated;
    }

    public void setFederated(boolean federated) {
        isFederated = federated;
    }

    public boolean isSkipSizeCalculation() {
        return skipSizeCalculation;
    }

    public void setSkipSizeCalculation(boolean skipSizeCalculation) {
        this.skipSizeCalculation = skipSizeCalculation;
    }

    public boolean isSkipSummaryMetrics() {
        return skipSummaryMetrics;
    }

    public void setSkipSummaryMetrics(boolean skipSummaryMetrics) {
        this.skipSummaryMetrics = skipSummaryMetrics;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }



    @Override
    public int hashCode() {
        return taskId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TaskEntity) {
            return taskId.equals(((TaskEntity) o).taskId);
        }
        return false;
    }

    @Override
    public String toString() {
        if (taskName == null) {
            taskName = StringUtils.stripDotGit(String.valueOf(taskId));
        }
        return taskName;
    }

    @Override
    public int compareTo(@NotNull TaskEntity o) {
        return StringUtils.compareRepositoryNames(String.valueOf(taskId), String.valueOf(o.taskId));
    }

    public boolean typeOfFork() {
        return !StringUtils.isEmpty(originRepository);
    }

    public boolean isOwner(String userId) {
        if (StringUtils.isEmpty(userId) || ArrayUtils.isEmpty(owners)) {
            return isUsersPersonalRepository(userId);
        }
        return owners.contains(userId.toLowerCase()) || isUsersPersonalRepository(userId);
    }

    public boolean isPersonalRepository() {
        return !StringUtils.isEmpty(projectPath) && ModelUtils.isPersonalRepository(projectPath);
    }

    public boolean isUsersPersonalRepository(String userId) {
        return !StringUtils.isEmpty(projectPath) && ModelUtils.isUsersPersonalRepository(userId, projectPath);
    }

    public boolean allowAnonymousView() {
        return !accessRestriction.atLeast(Constants.AccessRestrictionType.VIEW);
    }

    public boolean isShowActivity() {
        return maxActivityCommits > -1;
    }

    public TaskEntity cloneAs(String cloneName) {
        TaskEntity clone = new TaskEntity();
        clone.originRepository = taskName;
        clone.taskName = cloneName;
        clone.projectPath = StringUtils.getFirstPathElement(cloneName);
        clone.isBare = true;
        clone.taskDes = taskDes;
        clone.accessRestriction = Constants.AccessRestrictionType.PUSH;
        clone.authorizationControl = Constants.AuthorizationControl.NAMED;
//        clone.showRemoteBranches = false;
        clone.allowForks = false;
        clone.acceptNewPatchsets = false;
        clone.acceptNewTickets = false;
        return clone;
    }

    public void addOwner(String userId) {
        if (!StringUtils.isEmpty(userId)) {
            String name = userId.toLowerCase();
            // a set would be more efficient, but this complicates JSON
            // deserialization so we enforce uniqueness with an arraylist
            if (!owners.contains(name)) {
                owners.add(name);
            }
        }
    }

    public String getRID() {
        return StringUtils.getSHA1(taskName);
    }

    public void removeOwner(String userId) {
        if (!StringUtils.isEmpty(userId)) {
            owners.remove(userId.toLowerCase());
        }
    }

    public void addOwners(Collection<String> userIds) {
        if (!ArrayUtils.isEmpty(userIds)) {
            for (String username : userIds) {
                addOwner(username);
            }
        }
    }

    public void removeOwners(Collection<String> userIds) {
        if (!ArrayUtils.isEmpty(owners)) {
            for (String username : userIds) {
                removeOwner(username);
            }
        }
    }

    public void addFork(String repository) {
        if (forks == null) {
            forks = new TreeSet<String>();
        }
        forks.add(repository);
    }

    public Constants.CommitMessageRenderer getCommitMessageRenderer() {
        return commitMessageRenderer;
    }

    public void setCommitMessageRenderer(Constants.CommitMessageRenderer commitMessageRenderer) {
        this.commitMessageRenderer = commitMessageRenderer;
    }

    public List<String> getMetricAuthorExclusions() {
        return metricAuthorExclusions;
    }

    public void setMetricAuthorExclusions(List<String> metricAuthorExclusions) {
        this.metricAuthorExclusions = metricAuthorExclusions;
    }

    public Date getLastChange() {
        return lastChange;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }

    public boolean isCollectingGarbage() {
        return isCollectingGarbage;
    }

    public void setCollectingGarbage(boolean collectingGarbage) {
        isCollectingGarbage = collectingGarbage;
    }
}