package com.service.service.async;

import com.service.service.biz.PullBiz;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.TicketModel;
import com.service.service.managers.IWorkHub;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryState;
import org.eclipse.jgit.transport.ReceivePack;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PushAsync {

    private IWorkHub workHub;

    private PullBiz pullBiz;

    public PushAsync(IWorkHub workHub,PullBiz pullBiz ) {
        this.workHub = workHub;
        this.pullBiz = pullBiz;
    }

    @Async
    public void createPRFromPush(TaskEntity taskEntity) throws InterruptedException{
        TicketModel ticketModel = new TicketModel();

        ticketModel.setTaskName(taskEntity.getTaskName());
        ticketModel.setTitle("请求合并");
        ticketModel.setBody("来自客户端的合并请求");
        ticketModel.setMergeto("master");
        pullBiz.createPullRequest(ticketModel);
    }
}
