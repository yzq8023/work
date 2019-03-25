package com.github.hollykunge.servicetalk.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.hollykunge.servicetalk.user.entity.ImGroup;
import com.github.hollykunge.servicetalk.user.entity.ImUserFriend;
import com.github.hollykunge.servicetalk.user.mapper.ImUserFriendMapper;
import com.github.hollykunge.servicetalk.user.service.IImUserFriendService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ${描述}
 *
 * @author: holly
 * @since: 2019/2/15
 */
@Service
@Qualifier(value = "imUserFriendService")
public class ImUserFriendServiceImpl extends ServiceImpl<ImUserFriendMapper, ImUserFriend> implements IImUserFriendService {


    /**
     * 根据用户的ID 获取 用户好友(双向用户关系)
     *
     * @param userId 用户ID
     * @return 好友分组的列表
     */
    @Override
    public List<ImGroup> getUserFriends(String userId){
        return this.baseMapper.getUserFriends(userId);
    }
}
