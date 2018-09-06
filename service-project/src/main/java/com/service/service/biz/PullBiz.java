package com.service.service.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.service.service.entity.PullEntity;
import com.service.service.mapper.PullEntityMapper;

/**
 * @Author: hollykunge
 * @Description:
 * @Date: 创建于 2018/9/6
 * @Modified:
 */
public class PullBiz extends BaseBiz<PullEntityMapper, PullEntity> {
    @Override
    protected String getPageName() {
        return null;
    }
}
