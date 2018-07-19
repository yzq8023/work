package com.service.service.feign;

import com.github.wxiaoqi.security.api.vo.user.UserInfo;
import com.service.service.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "ace-admin",configuration = FeignConfiguration.class)
public interface IUserFeignClient {
    @RequestMapping(value = "/api/user/info", method = RequestMethod.POST)
    public UserInfo info(@RequestParam("userId") Integer userId);

    @RequestMapping(value = "/api/user/all", method = RequestMethod.POST)
    public List<UserInfo> all();
}
