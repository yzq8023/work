package com.github.hollykunge.security.admin.rpc;

import com.ace.cache.annotation.Cache;
import com.github.hollykunge.security.admin.biz.UserBiz;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.rpc.service.PermissionService;
import com.github.hollykunge.security.api.vo.authority.PermissionInfo;
import com.github.hollykunge.security.api.vo.user.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-06-21 8:15
 */
@RestController
@RequestMapping("api")
public class UserRest {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserBiz userBiz;

    @Cache(key="permission")
    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    public @ResponseBody
    List<PermissionInfo> getAllPermission(){
        return permissionService.getAllPermission();
    }

    @Cache(key="permission:u{1}")
    @RequestMapping(value = "/user/un/{username}/permissions", method = RequestMethod.GET)
    public @ResponseBody List<PermissionInfo> getPermissionByUsername(@PathVariable("username") String username){
        return permissionService.getPermissionByUsername(username);
    }

    @RequestMapping(value = "/user/validate", method = RequestMethod.POST)
    public @ResponseBody UserInfo validate(String username,String password){
        return permissionService.validate(username,password);
    }

    @RequestMapping(value = "/user/info", method = RequestMethod.POST)
    public @ResponseBody UserInfo info(Integer userId){
        User user = userBiz.getUserByUserId(userId);
        UserInfo info = new UserInfo();

        BeanUtils.copyProperties(user, info);
        info.setId(user.getId().toString());
        return info;
    }

    @RequestMapping(value = "/user/all", method = RequestMethod.POST)
    public @ResponseBody List<UserInfo> all(){
        List<User> users = userBiz.getUsers();
        List<UserInfo> infos = new ArrayList<UserInfo>();
        BeanUtils.copyProperties(users, infos);
        return infos;
    }
}
