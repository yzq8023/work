package com.service.service.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.service.service.entity.IssueEntity;
import com.service.service.mapper.IssueEntityMapper;
import org.springframework.stereotype.Service;



/**
 * 
 *
 * @author
 * @date 2018-08-20 16:05:04
 */
@Service
public class IssueBiz extends BaseBiz<IssueEntityMapper,IssueEntity> {
    @Override
    protected String getPageName() {
        return null;
    }
}