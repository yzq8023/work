package com.service.service.biz;


import com.ace.cache.annotation.Cache;
import com.service.service.entity.UserModel;
import com.service.service.feign.IUserFeignClient;
import com.service.service.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserModelBiz {


    @Autowired
    private IUserFeignClient userFeignClient;

    @Cache(key = "userModel{1}")
    public UserModel findModel(Integer userId){
        UserModel userModel = UserUtils.transUser(userFeignClient.info(userId));
        return userModel;
    }
}
