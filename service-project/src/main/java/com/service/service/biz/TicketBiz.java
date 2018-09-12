package com.service.service.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.context.BaseContextHandler;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.TicketModel;
import com.service.service.feign.IUserFeignClient;
import com.service.service.managers.IWorkHub;
import com.service.service.mapper.TicketModelMapper;
import com.service.service.tickets.TicketNotifier;
import com.service.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: hollykunge
 * @Description:
 * @Date: 创建于 2018/9/11
 * @Modified:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TicketBiz extends BaseBiz<TicketModelMapper, TicketModel> {

    private IWorkHub workHub;
    private TicketModel ticket;
    private TaskBiz taskBiz;
    private IUserFeignClient userFeignClient;

    @Autowired
    public TicketBiz(IWorkHub workHub, TaskBiz taskBiz, IUserFeignClient userFeignClient) {
        this.workHub = workHub;
        this.taskBiz = taskBiz;
        this.userFeignClient = userFeignClient;
    }

    private String getCurrentUserId() {
        return BaseContextHandler.getUserID();
    }

    /**
     * 创建pullRequest
     *
     * @param ticketModel
     */
    public void createPullRequest(TicketModel ticketModel) {
        if (StringUtils.isEmpty(ticketModel.getTitle())) {
            return;
        }
        TicketModel.Change change = new TicketModel.Change(getCurrentUserId());
        change.setField(TicketModel.Field.title, ticketModel.getTitle());
        change.setField(TicketModel.Field.body, ticketModel.getBody());
        String mergeTo = ticketModel.getMergeTo();
        if (!StringUtils.isEmpty(mergeTo)) {
            change.setField(TicketModel.Field.mergeTo, mergeTo);
        }
        TaskEntity taskEntity = taskBiz.selectById(ticketModel.getTaskName());
//        UserModel userModel = UserUtils.transUser(userFeignClient.info(Integer.valueOf(getCurrentUserId())));
        TicketModel ticket = workHub.getTicketService().createTicket(workHub.getRepositoryModel(taskEntity.getTaskName()), 0L, change);

        if (ticket != null) {
            TicketNotifier notifier = workHub.getTicketService().createNotifier();
            //绑定邮件
            notifier.sendMailing(ticket);
//            createMerge(userModel, taskEntity);
        } else {
            // TODO error
        }
    }

    /**
     * 创建合并请求
     *
     * @param userModel
     * @param taskEntity
     */
//    public void createMerge(UserModel userModel, TaskEntity taskEntity) {
//        Patchset patchset = ticket.getCurrentPatchset();
//        if (patchset == null) {
//            // no patchset to merge
//            return;
//        }
//        boolean allowMerge;
//        if (taskEntity.isRequireApproval()) {
//            // repository requires approval
//            allowMerge = ticket.isOpen() && ticket.isApproved(patchset);
//        } else {
//            // vetoes are binding
//            allowMerge = ticket.isOpen() && !ticket.isVetoed(patchset);
//        }
//
//        MergeStatus mergeStatus = JGitUtils.canMerge(workHub.getRepository(taskEntity.getTaskName()), patchset.tip, ticket.getMergeTo(), taskEntity.getMergeType());
//        if (allowMerge) {
//            if (JGitUtils.MergeStatus.MERGEABLE == mergeStatus) {
//                //补丁集可以被干净地合并到集成分支或者已经合并了
//                if (userModel.canPush(taskEntity)) {
//
//                    String taskName = taskBiz.selectById(taskEntity.getTaskId()).getTaskName();
//                    //可合并
//                    final TicketModel refreshedTicket = workHub.getTicketService().getTicket(workHub.getRepositoryModel(taskName), ticket.getNumber());
//                    PatchsetReceivePack rp = new PatchsetReceivePack(
//                            workHub,
//                            workHub.getRepository(taskName),
//                            taskEntity,
//                            userModel
//                            );
//                    MergeStatus result = rp.merge(refreshedTicket);
//                }
//            }
//        }
//    }

    @Override
    protected String getPageName() {
        return "TicketBiz";
    }
}
