package com.service.service.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.service.service.entity.LabelEntity;
import com.service.service.mapper.LabelEntityMapper;
import com.service.service.utils.StringUtils;
import org.springframework.stereotype.Service;



/**
 * 
 *
 * @author
 * @email
 * @date 2018-08-27 10:41:15
 */
@Service
public class LabelBiz extends BaseBiz<LabelEntityMapper,LabelEntity> {


    @Override
    protected String getPageName() {
        return null;
    }
}