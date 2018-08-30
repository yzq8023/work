package com.service.service.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.service.service.entity.MilestoneEntity;
import com.service.service.mapper.MilestoneEntityMapper;
import org.springframework.stereotype.Service;



/**
 * 
 *
 * @author
 * @email
 * @date 2018-08-27 10:41:16
 */
@Service
public class MilestoneBiz extends BaseBiz<MilestoneEntityMapper,MilestoneEntity> {
    @Override
    protected String getPageName() {
        return null;
    }
}