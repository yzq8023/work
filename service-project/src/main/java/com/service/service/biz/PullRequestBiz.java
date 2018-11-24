package com.service.service.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.context.BaseContextHandler;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.service.service.entity.PathModel;
import com.service.service.entity.PullRequestEntity;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;
import com.service.service.feign.IUserFeignClient;
import com.service.service.git.ReceiveCommandEvent;
import com.service.service.managers.IWorkHub;
import com.service.service.mapper.PullRequestEntityMapper;
import com.service.service.utils.JGitUtils;
import com.service.service.utils.JGitUtils.*;
import com.service.service.utils.StringUtils;

import com.service.service.utils.UserUtils;
import org.eclipse.jgit.events.RefsChangedEvent;
import org.eclipse.jgit.events.RefsChangedListener;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;

import org.eclipse.jgit.transport.ReceiveCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.service.service.Constants.MergeType.MERGE_ALWAYS;

/**
 * @author hollykunge
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PullRequestBiz extends BaseBiz<PullRequestEntityMapper, PullRequestEntity> implements RefsChangedListener {

    private IWorkHub workHub;
    private IUserFeignClient userFeignClient;

    @Autowired
    public PullRequestBiz(IWorkHub workHub, TaskBiz taskBiz, IUserFeignClient userFeignClient) {
        this.workHub = workHub;
        this.userFeignClient = userFeignClient;
        Repository.getGlobalListenerList().addRefsChangedListener(this);
    }

    static final Logger LOGGER = LoggerFactory.getLogger(JGitUtils.class);

    private String getCurrentUserId() {
        return BaseContextHandler.getUserID();
    }

    /**
     * 创建合并请求
     * 该处可进行拓展，拓展为接收工单
     * @param pullRequestEntity
     */
    public ObjectRestResponse createPullRequest(PullRequestEntity pullRequestEntity) {
        if (StringUtils.isEmpty(pullRequestEntity.getTitle())) {
            return new ObjectRestResponse().rel(false);
        }
        super.insert(pullRequestEntity);
        return new ObjectRestResponse().rel(true);
    }

    /**
     * 执行合并动作
     * 如果合并失败会返回冲突提示
     * @param pRid
     * @param taskName
     */
    public MergeStatus merge(Integer pRid, String taskName) {

        UserModel userModel = UserUtils.transUser(userFeignClient.info(Integer.valueOf(getCurrentUserId())));

        PullRequestEntity pullRequestEntity = super.selectById(pRid);

        PersonIdent committer = new PersonIdent(userModel.getUsername(), StringUtils.isEmpty(userModel.getEmailAddress()) ? (userModel.getUsername() + "@workhub") : userModel.getEmailAddress());

        String message = MessageFormat.format("合并 #{0,number,0} \"{1}\"", pullRequestEntity.getIndex(), pullRequestEntity.getTitle());

        MergeResult mergeResult = JGitUtils.merge(
                workHub.getRepository(taskName),
                pullRequestEntity.getBaseBranch(),
                pullRequestEntity.getHeadBranch(),
                MERGE_ALWAYS,
                committer,
                message);

        if (StringUtils.isEmpty(mergeResult.sha)) {
            LOGGER.error("合并失败 {} 到 {} ({})", new Object[]{pullRequestEntity.getBaseBranch(), pullRequestEntity.getHeadBranch(), mergeResult.status.name()});
            return mergeResult.status;
        }
        return mergeResult.status;
    }

    /**
     * 在push完成之后调用
     * 调用创建合并请求
     * @param event
     */
    @Override
    public void onRefsChanged(RefsChangedEvent event) {
        if (!(event instanceof ReceiveCommandEvent)) {
            return;
        }
        ReceiveCommandEvent branchUpdate = (ReceiveCommandEvent) event;
        TaskEntity repository = branchUpdate.model;
        ReceiveCommand cmd = branchUpdate.cmd;

        UserModel userModel = UserUtils.transUser(userFeignClient.info(Integer.valueOf(getCurrentUserId())));

        try {
            switch (cmd.getType()) {
                case CREATE:
                case UPDATE_NONFASTFORWARD:
                    break;
                case UPDATE:
                    break;
                default:

                    break;
            }
        } catch (Exception e) {

        }
    }
    @Override
    protected String getPageName() {
        return "PullRequestBiz";
    }
}
