package com.workhub.z.servicechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableFeignClients({"com.github.hollykunge.security.auth.client.feign", "com.service.service.feign"})
public class ServiceChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceChatApplication.class, args);
    }

}

