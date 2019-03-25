package com.github.hollykunge.servicetalk.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.hollykunge.servicetalk.user.entity.ImChatGroupUser;
import com.github.hollykunge.servicetalk.user.mapper.ImChatGroupUserMapper;
import com.github.hollykunge.servicetalk.user.service.IImChatGroupUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ${描述}
 *
 * @author: holly
 * @since: 2019/2/15
 */
@Service
@Qualifier("imChatGroupUserService")
public class ImChatGroupUserServiceImpl extends ServiceImpl<ImChatGroupUserMapper, ImChatGroupUser> implements IImChatGroupUserService {
}
