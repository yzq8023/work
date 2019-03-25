package com.workhub.z.servicechat.feign;


import com.github.hollykunge.security.api.vo.user.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import com.github.hollykunge.security.auth.configuration.FeignConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ace-admin",configuration = FeignConfiguration.class)
public interface IUserService {
    /**
    *@Description: 根据user身份证查询用户信息
    *@Param: sn
    *@return: UserInfo
    *@Author: 忠
    *@date: 2019/3/22
    */
    @RequestMapping(value = "/api/user/validate", method = RequestMethod.POST)
    public UserInfo queryUserinfoBySn(@RequestParam ("sn")String sn);
}
