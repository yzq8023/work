package com.github.hollykunge.servicetalk.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.hollykunge.servicetalk.user.entity.ImChatGroup;
import com.github.hollykunge.servicetalk.user.mapper.ImChatGroupMapper;
import com.github.hollykunge.servicetalk.user.service.IImChatGroupService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ${描述}
 *
 * @author: holly
 * @since: 2019/2/15
 */
@Service
@Qualifier("imChatGroupServiceImpl")
public class ImChatGroupServiceImpl extends ServiceImpl<ImChatGroupMapper, ImChatGroup> implements IImChatGroupService {
}
