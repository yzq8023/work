/*
package com.service.service.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.service.service.Constants;
import com.service.service.entity.PullRequestEntity;
import com.service.service.managers.IWorkHub;
import com.service.service.utils.JGitUtils;
import com.service.service.vo.DataFlow;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class GraphBiz {

    private IWorkHub workHub;

    @Autowired
    public GraphBiz(IWorkHub workHub) {
        this.workHub = workHub;
    }

    */
/**
     * 为任务仓库提供数据流视图
     * @param query
     * @return
     *//*

    public TableResultResponse<DataFlow> dataFlow(Query query){
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        Repository r = workHub.getRepository(query.getTaskName());
        List<RevCommit> commits = JGitUtils.getRevLog(r, workHub.getRepositoryModel(query.getTaskName()).getHead(), 0, query.getLimit());

        DataFlow dataFlow = new DataFlow();
        for (RevCommit commit:commits){
            dataFlow.setAction(commit.getAuthorIdent().);
            dataFlow.setActionTime(commit.getCommitTime());
            dataFlow.setBranch(commit);
            dataFlow.setMessage();
            dataFlow.setTaskId();
            dataFlow.setUserId();
            dataFlow.setUserName(commit.getCommitterIdent().getName());
            dataFlow.setParent();
            dataFlow.setSha();
        }
        return new TableResultResponse<>(result.getTotal(), paths);
    }

}
*/
