package com.service.service;

import com.ace.cache.EnableAceCache;
import com.github.hollykunge.security.auth.client.EnableAceAuthClient;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author dk
 */
@EnableEurekaClient
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableTransactionManagement
@SpringBootApplication
@EnableFeignClients({"com.github.hollykunge.security.auth.client.feign", "com.service.service.feign"})
@EnableScheduling
@EnableAceCache
@EnableAceAuthClient
@MapperScan("com.service.service.mapper")
@EnableSwagger2Doc
@ServletComponentScan
@EnableAspectJAutoProxy
@EnableAsync
public class ServiceProjectApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ServiceProjectApplication.class).web(true).run(args);
	}
}
