package com.service.service.config;

import com.github.hollykunge.security.auth.client.feign.ServiceAuthFeign;
import com.github.hollykunge.security.auth.client.interceptor.ServiceAuthRestInterceptor;
import com.github.hollykunge.security.auth.client.interceptor.UserAuthRestInterceptor;
import com.github.hollykunge.security.common.handler.GlobalExceptionHandler;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dk
 */
@Configuration("projectWebConfig")
@Primary
public class WebConfiguration implements WebMvcConfigurer {
    @Bean
    GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getServiceAuthRestInterceptor()).
                addPathPatterns(getIncludePathPatterns()).addPathPatterns("/api/user/validate");
        registry.addInterceptor(getUserAuthRestInterceptor()).
                addPathPatterns(getIncludePathPatterns());
    }

    @Bean
    ServiceAuthRestInterceptor getServiceAuthRestInterceptor() {
        return new ServiceAuthRestInterceptor();
    }

    @Bean
    UserAuthRestInterceptor getUserAuthRestInterceptor() {
        return new UserAuthRestInterceptor();
    }

    /**
     * 需要用户和服务认证判断的路径
     * @return
     */
    private ArrayList<String> getIncludePathPatterns() {
        ArrayList<String> list = new ArrayList<>();
        String[] urls = {
                "/project/**",
                "/team/**",
                "/task/**",
                "/issue/**",
                "/comment/**"
        };
        Collections.addAll(list, urls);
        return list;
    }
}
