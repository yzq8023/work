package com.github.hollykunge.servicetalk.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.hollykunge.servicetalk.user.entity.ImGroup;
import com.github.hollykunge.servicetalk.user.entity.ImUserFriend;

import java.util.List;

public interface ImUserFriendMapper extends BaseMapper<ImUserFriend> {
    /**
     * 根据用户的ID 获取 用户好友(双向用户关系)
     * @param userId 用户ID
     * @return 好友分组的列表
     */
    List<ImGroup> getUserFriends(String userId);
}
