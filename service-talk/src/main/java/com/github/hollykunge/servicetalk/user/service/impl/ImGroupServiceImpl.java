package com.github.hollykunge.servicetalk.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.hollykunge.servicetalk.user.entity.ImGroup;
import com.github.hollykunge.servicetalk.user.mapper.ImGroupMapper;
import com.github.hollykunge.servicetalk.user.service.IImGroupService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ${描述}
 *
 * @author: holly
 * @since: 2019/2/15
 */
@Service
@Qualifier("imGroupService")
public class ImGroupServiceImpl extends ServiceImpl<ImGroupMapper, ImGroup> implements IImGroupService {
}
