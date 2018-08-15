package com.service.service.utils;

import com.github.wxiaoqi.security.api.vo.user.UserInfo;
import com.service.service.entity.UserModel;
import org.springframework.beans.BeanUtils;

/**
 * @author hollykunge
 * @date
 * @des
 */
public class UserUtils {
    public static UserModel transUser(UserInfo userInfo){
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userInfo,userModel);
        return userModel;
    }
}
