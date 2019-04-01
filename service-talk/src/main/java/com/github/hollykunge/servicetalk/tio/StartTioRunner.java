package com.github.hollykunge.servicetalk.tio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 启动类
 *
 * @author: holly
 * @since: 2019/2/15
 */
@Component
public class StartTioRunner implements CommandLineRunner {

    @Resource
    private TioWsMsgHandler tioWsMsgHandler;

    private TioWebsocketStarter appStarter;
    @Override
    public void run(String... args) throws Exception {
        this.appStarter = new TioWebsocketStarter(TioServerConfig.SERVER_PORT, tioWsMsgHandler);
        appStarter.getWsServerStarter().start();
    }
}
