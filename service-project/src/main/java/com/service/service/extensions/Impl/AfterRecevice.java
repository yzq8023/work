package com.service.service.extensions.Impl;

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
public class AfterRecevice extends ReceiveHook {

    private IWorkHub workHub;
    @Autowired
    public AfterRecevice(IWorkHub workHub) {
        this.workHub = workHub;
    }

    @Override
    public void onPreReceive(GitblitReceivePack receivePack, Collection<ReceiveCommand> commands) {

    }

    @Override
    public void onPostReceive(GitblitReceivePack receivePack, Collection<ReceiveCommand> commands) {
        TicketModel.Change change = new TicketModel.Change("1");
        change.setField(TicketModel.Field.title, "请求合并");
        change.setField(TicketModel.Field.body, "来自客户端的合并请求");
        change.setField(TicketModel.Field.mergeTo, "master");

        workHub.getTicketService().createTicket(workHub.getRepositoryModel(repository.getTaskName()), 0L, change);
    }
}
