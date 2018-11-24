package com.github.hollykunge.security.admin.biz;

import com.github.hollykunge.security.admin.entity.GateLog;
import com.github.hollykunge.security.admin.mapper.GateLogMapper;
import com.github.hollykunge.security.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-07-01 14:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GateLogBiz extends BaseBiz<GateLogMapper,GateLog> {

    @Override
    public void insert(GateLog entity) {
        mapper.insert(entity);
    }

    @Override
    public void insertSelective(GateLog entity) {
        mapper.insertSelective(entity);
    }

    @Override
    protected String getPageName() {
        return null;
    }
}
