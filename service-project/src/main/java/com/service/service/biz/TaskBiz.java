package com.service.service.biz;

import com.ace.cache.annotation.CacheClear;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.api.vo.user.UserInfo;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import com.google.common.base.Optional;
import com.service.service.Constants;
import com.service.service.IStoredSettings;
import com.service.service.Keys;
import com.service.service.entity.*;
import com.service.service.exception.GitBlitException;
import com.service.service.feign.IUserFeignClient;
import com.service.service.managers.IRepositoryManager;
import com.service.service.managers.IRuntimeManager;
import com.service.service.managers.IUserManager;
import com.service.service.managers.IWorkHub;
import com.service.service.mapper.TaskEntityMapper;
import com.service.service.utils.JGitUtils;
import com.service.service.utils.ModelUtils;
import com.service.service.utils.StringUtils;
import com.service.service.utils.UserUtils;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheBuilder;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;

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

    private IUserManager userManager;
    private IWorkHub workHub;
    private IStoredSettings settings;
    private IUserFeignClient userFeignClient;
    private IRepositoryManager repositoryManager;
    List<TaskEntity> repositoryModels = new ArrayList<TaskEntity>();
    protected String projectName;
    private Map<String, SubmoduleModel> submodules;

    private TaskEntity taskEntity;
    @Autowired
    public TaskBiz(IUserManager userManager,
                   IWorkHub workHub,
                   IRuntimeManager runtimeManager,
                   IUserFeignClient userFeignClient,
                   IRepositoryManager repositoryManager) {
        this.userManager = userManager;
        this.workHub = workHub;
        this.settings = runtimeManager.getSettings();
        this.userFeignClient = userFeignClient;
        this.repositoryManager = repositoryManager;
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
    public TableResultResponse<Map<String,Object>> getJoinedTaskFromProject(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<Map<String,Object>> list = mapper.selectTaskByPIdAndUId(query.getCrtUser(), query.getProjectId());
        return  new TableResultResponse<>(result.getTotal(), list);
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
    public boolean createTask(TaskEntity taskEntity, String userId) {
        try {
            //传递
            taskEntity.setCrtUser(userId);
            taskEntity.setHead(Constants.R_MASTER);
            taskEntity.setMergeTo(Constants.MASTER);
            //更新
            taskEntity.setTaskName(taskEntity.getTaskProjectName() + "/" + taskEntity.getTaskName());
            //初始化
            workHub.updateRepositoryModel(taskEntity.getTaskName(), taskEntity, true);
            // 创建初始提交
//            initialCommit(taskEntity, addReadme, gitignore, useGitFlow);
            return true;
        } catch (GitBlitException e) {
            error(e.getMessage());
            return false;
        }
    }

    //TODO 采用数据库方式读取repository，如果有问题建议使用原始方式
    /**
     * 获取任务仓库内文件路径
     *
     * @param query
     */
    public List<PathModel> getRepository(Query query){
        Integer taskId = query.getTaskId();
        TaskEntity taskEntity = this.selectById(taskId);
        String root = StringUtils.getFirstPathElement(taskEntity.getTaskName());
        if (StringUtils.isEmpty(root)) {
            projectName = settings.getString(Keys.web.repositoryRootGroupName, "main");
        } else {
            projectName = root;
        }

        if (StringUtils.isEmpty(taskEntity.getTaskName())) {
//            error(MessageFormat.format(("未指定任务"), getPageName()), true);
        }

//        if (!getRepositoryModel(taskEntity.getTaskName(), query.getCrtUser()).isHasCommits()) {
//            //TODO 重定向到概览页面
//        }

        Repository r = workHub.getRepository(taskEntity.getTaskName());
        RevCommit commit = getCommit(r, null);
        List<PathModel> paths = JGitUtils.getFilesInPath2(r, null, commit);
        return paths;
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

//    protected TaskEntity getRepositoryModel(String taskName, Integer userId) {
//        if (taskEntity == null) {
//            TaskEntity model = workHub.getRepositoryModel(UserUtils.transUser(userFeignClient.info(userId)), taskName);
//            if (model == null) {
//                if (workHub.hasRepository(taskName, true)) {
//                    // 有这个库，但未经授权
//                    authenticationError(getString("gb.unauthorizedAccessForRepository") + " " + repositoryName);
//                } else {
//                    // does not have repository
//                    error(getString("gb.canNotLoadRepository") + " " + repositoryName, true);
//                }
//                return null;
//            }
//            m = model;
//        }
//        return m;
//    }
}

