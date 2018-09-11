package com.service.service.extensions.Impl;

import com.service.service.biz.TicketBiz;
import com.service.service.entity.TicketModel;
import com.service.service.extensions.ReceiveHook;
import com.service.service.git.GitblitReceivePack;
import com.service.service.managers.IWorkHub;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @Author: hollykunge
 * @Description:
 * @Date: 创建于 2018/9/11
 * @Modified:
 */
@Component
public class AfterReceive extends ReceiveHook {

    private IWorkHub workHub;
    private TicketBiz ticketBiz;
    @Autowired
    public AfterReceive(IWorkHub workHub,
                        TicketBiz ticketBiz) {
        this.workHub = workHub;
        this.ticketBiz = ticketBiz;
    }

    @Override
    public void onPreReceive(GitblitReceivePack receivePack, Collection<ReceiveCommand> commands) {

    }

    @Override
    public void onPostReceive(GitblitReceivePack receivePack, Collection<ReceiveCommand> commands) {

        TicketModel ticketModel = new TicketModel();
        ticketModel.setTitle("请求合并");
        ticketModel.setBody("来自客户端的合并请求");
        ticketModel.setMergeto("master");
        ticketModel.setTaskName(receivePack.getRepositoryModel().getTaskName());

        ticketBiz.createPullRequest(ticketModel);
    }
}
