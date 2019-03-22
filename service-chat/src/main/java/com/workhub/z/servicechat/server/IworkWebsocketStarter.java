package com.workhub.z.servicechat.server;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@Order(1)
public class IworkWebsocketStarter implements ApplicationRunner {
    private WsServerStarter wsServerStarter;
    private ServerGroupContext serverGroupContext;

    /**
     *
     * @author tanyaowu
     */
    public IworkWebsocketStarter() throws IOException {
        wsServerStarter = new WsServerStarter(IworkServerConfig.SERVER_PORT, IworkWsMsgHandler.me);

        serverGroupContext = wsServerStarter.getServerGroupContext();
        serverGroupContext.setName(IworkServerConfig.PROTOCOL_NAME);
        serverGroupContext.setServerAioListener(IworkServerAioListener.me);

        //设置ip监控
        serverGroupContext.setIpStatListener(IworkIpStatListener.me);
        //设置ip统计时间段
        serverGroupContext.ipStats.addDurations(IworkServerConfig.IpStatDuration.IPSTAT_DURATIONS);

        //设置心跳超时时间
        serverGroupContext.setHeartbeatTimeout(IworkServerConfig.HEARTBEAT_TIMEOUT);
    }

    /**
     * @return the serverGroupContext
     */
    public ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }

    public WsServerStarter getWsServerStarter() {
        return wsServerStarter;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        IworkWebsocketStarter appStarter = new IworkWebsocketStarter();
        appStarter.wsServerStarter.start();
        System.out.println("网络初始化成功!!");
    }
}
