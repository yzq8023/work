package com.github.hollykunge.security.auth.client.configuration;

import com.github.hollykunge.security.auth.client.config.ServiceAuthConfig;
import com.github.hollykunge.security.auth.client.config.UserAuthConfig;
import com.github.hollykunge.security.auth.client.feign.ServiceAuthFeign;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by 协同设计小组 on 2017/9/15.
 */
@Configuration
@ComponentScan({"com.github.hollykunge.security.auth.client","com.github.hollykunge.security.auth.common"})
@RemoteApplicationEventScan(basePackages = "com.github.hollykunge.security.auth.common.event")
public class AutoConfiguration {
    @Bean
    ServiceAuthConfig getServiceAuthConfig(){
        return new ServiceAuthConfig();
    }

    @Bean
    UserAuthConfig getUserAuthConfig(){
        return new UserAuthConfig();
    }

}
