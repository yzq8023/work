package com.service.service.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.Constants;
import com.service.service.IStoredSettings;
import com.service.service.Keys;
import com.service.service.entity.*;
import com.service.service.exception.GitBlitException;
import com.service.service.feign.IUserFeignClient;
import com.service.service.managers.IRuntimeManager;
import com.service.service.managers.IWorkHub;
import com.service.service.mapper.TaskEntityMapper;
import com.service.service.utils.JGitUtils;
import com.service.service.utils.StringUtils;
import com.service.service.utils.UserUtils;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.apache.log4j.helpers.LogLog.error;

/**
 * 业务逻辑类
 * 描述：实现在增删改中对事务的管理和回滚，所有需要实现事物的方法均要放到这里
 *
 * @author dk
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskBiz extends BaseBiz<TaskEntityMapper, TaskEntity> {

    private IWorkHub workHub;
    private IStoredSettings settings;
    private IUserFeignClient userFeignClient;
    private Map<String, SubmoduleModel> submodules;

    @Autowired
    public TaskBiz(IWorkHub workHub,
                   IRuntimeManager runtimeManager,
                   IUserFeignClient userFeignClient) {
        this.workHub = workHub;
        this.settings = runtimeManager.getSettings();
        this.userFeignClient = userFeignClient;
    }

    /**
     * 更改任务关联团队
     *
     * @param taskId 任务id
     * @param teams  队伍
     */
    public void modifyTeamsInTask(Integer taskId, String teams) {
        mapper.deleteTaskTeamsById(taskId);
        if (!StringUtils.isEmpty(teams)) {
            String[] team = teams.split(",");
            for (String t : team) {
                mapper.insertTaskTeamsById(taskId, Integer.parseInt(t));
            }
        }
    }

    /**
     * 在当前项目中获取已参与的任务
     *
     * @param query dao接口
     * @return 任务
     */
    public TableResultResponse<Map<String, Object>> getJoinedTaskFromProject(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        String taskName = null;
        Integer taskProcess = null;
        if (query.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                if (entry.getKey().equals("taskName") && entry.getKey() != null && entry.getValue() instanceof String) {
                    taskName = (String) entry.getValue();
                }
                if (entry.getKey().equals("taskProcess") && entry.getKey() != null && entry.getValue() instanceof String) {
                    taskProcess = Integer.parseInt(entry.getValue().toString());
                }
            }
        }
        List<Map<String, Object>> list = mapper.selectTaskByPIdAndUId(query.getCrtUser(), query.getProjectId(), taskName, taskProcess);
        return new TableResultResponse<>(result.getTotal(), list);
    }

    /**
     * 根据用户id查找map_user_task
     * 获取所有用户参与的任务
     */
    public TableResultResponse<TaskEntity> getJoinedTask(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<TaskEntity> list = mapper.selectJoinedTaskById(query.getCrtUser());
        return new TableResultResponse<>(result.getTotal(), list);
    }

    /**
     * 根据用户及名称创建任务
     *
     * @param taskEntity 任务实体
     */
    public TaskEntity createTask(TaskEntity taskEntity, String userId) {
        try {
            // 传递
            taskEntity.setCrtUser(userId);
            taskEntity.setHead(Constants.R_MASTER);
            taskEntity.setMergeTo(Constants.MASTER);
            taskEntity.setAccessRestriction(Constants.AccessRestrictionType.PUSH);
            // 更新
            taskEntity.setTaskName(taskEntity.getTaskProjectName() + "/" + taskEntity.getTaskName());
            taskEntity.addOwner(userId);
            // 初始化
            workHub.updateRepositoryModel(taskEntity.getTaskName(), taskEntity, true);
            // 创建初始提交
//            initialCommit(taskEntity, addReadme, gitignore, useGitFlow);
//            mapper.insertSelective(taskEntity);
            Integer taskId = mapper.insert(taskEntity);

            return taskEntity;
        } catch (GitBlitException e) {
            error(e.getMessage());
        }
        return null;
    }

    //TODO 采用数据库方式读取repository，如果有问题建议使用原始方式

    /**
     * 获取任务仓库内文件路径
     *
     * @param query
     */
    public TableResultResponse<PathModel> getRepository(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<PathModel> paths = new ArrayList<>();
        String projectName = null;
//        if (!getRepositoryModel(query.getTaskName(), query.getCrtUser()).isHasCommits()) {
//            return paths;
//        }

        String root = StringUtils.getFirstPathElement(query.getTaskName());

        if (StringUtils.isEmpty(root)) {
            projectName = settings.getString(Keys.web.repositoryRootGroupName, "main");
        } else {
            projectName = root;
        }

        Repository r = workHub.getRepository(query.getTaskName());
        RevCommit commit = getCommit(r, null);
        paths = JGitUtils.getFilesInPath2(r, null, commit);
        return new TableResultResponse<>(result.getTotal(), paths);
    }

    protected RevCommit getCommit(Repository r, String objectId) {
        RevCommit commit = JGitUtils.getCommit(r, objectId);
        if (commit == null) {
            error("commit出现错误");
        }
        getSubmodules(commit, r);
        return commit;
    }

    protected Map<String, SubmoduleModel> getSubmodules(RevCommit commit, Repository r) {
        if (submodules == null) {
            submodules = new HashMap<String, SubmoduleModel>();
            for (SubmoduleModel model : JGitUtils.getSubmodules(r, commit.getTree())) {
                submodules.put(model.path, model);
            }
        }
        return submodules;
    }


    @Override
    protected String getPageName() {
        return "taskBiz";
    }

    protected TaskEntity getRepositoryModel(String taskName, Integer userId) {
        TaskEntity taskEntity = new TaskEntity();
        if (taskEntity == null) {
            TaskEntity model = workHub.getRepositoryModel(UserUtils.transUser(userFeignClient.info(userId)), taskName);
            if (model == null) {
                if (workHub.hasRepository(taskName, true)) {
                    // 有这个库，但未经授权
                    return null;
                } else {
                    // does not have repository
                    return null;
                }
            }
            taskEntity = model;
        }
        return taskEntity;
    }

    /**
     * 删除任务仓库
     *
     * @param taskEntity
     * @return
     */
    public boolean deleteRepository(TaskEntity taskEntity) {
        return workHub.deleteRepository(taskEntity.getTaskName());
    }

    public boolean isAdmin(Integer crtUser, Integer taskId) {
        int count = mapper.selectTaskByTIdAndCtrUser(crtUser, taskId);
        if (count == 0) {
            return false;
        } else if (count == 1) {
            return true;
        }
        return false;
    }

    public boolean isOwner(Integer crtUser, Integer taskId) {
        int count = mapper.selectMapTaskByTIdAndCtrUser(crtUser, taskId);
        if (count == 0) {
            return false;
        } else if (count == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取可用的分支
     * @param taskName
     * @return
     */
    public List<String> getIntegrationBranch(String taskName){
        List<String> branches = new ArrayList<String>();
        for (String branch : workHub.getRepositoryModel(taskName).getLocalBranches()) {
            // 排除ticket分支
            if (!branch.startsWith(Constants.R_TICKET)) {
                branches.add(Repository.shortenRefName(branch));
            }
        }
        branches.remove(Repository.shortenRefName(workHub.getRepositoryModel(taskName).getHead()));
        branches.add(0, Repository.shortenRefName(workHub.getRepositoryModel(taskName).getHead()));
        return branches;
    }
}

