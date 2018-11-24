package com.github.hollykunge.security.admin.biz;

import org.springframework.stereotype.Service;

import com.github.hollykunge.security.admin.entity.GroupType;
import com.github.hollykunge.security.admin.mapper.GroupTypeMapper;
import com.github.hollykunge.security.common.biz.BaseBiz;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-06-12 8:48
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupTypeBiz extends BaseBiz<GroupTypeMapper,GroupType> {
    @Override
    protected String getPageName() {
        return null;
    }
}
