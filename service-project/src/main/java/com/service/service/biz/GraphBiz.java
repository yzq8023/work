package com.service.service.biz;

import com.service.service.entity.PullRequestEntity;
import com.service.service.utils.JGitUtils;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class GraphBiz {

    public List<> flow(){
        List<RevCommit> commits = JGitUtils.getRevLog(r, objectId, 0, limit);

        for (RevCommit commit:commits){
            commit.getCommitTime();
            commit.getCommitterIdent();
            commit.getFullMessage();
            commit.
        }
    }

}
