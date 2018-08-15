package com.service.service.controller;



import com.service.service.biz.UserModelBiz;
import com.service.service.entity.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("userInfo")
public class UserInfoController {

    @Autowired
    UserModelBiz userModelBiz;


    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @ResponseBody
    public UserModel testUserInfo(Integer userId){

     return userModelBiz.findModel(userId);
    }
}
