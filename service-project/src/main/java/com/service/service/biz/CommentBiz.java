package com.service.service.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.service.service.entity.CommentEntity;
import com.service.service.mapper.CommentEntityMapper;
import com.service.service.utils.StringUtils;
import org.springframework.stereotype.Service;



/**
 * 
 *
 * @author
 * @email
 * @date 2018-08-27 10:41:16
 */
@Service
public class CommentBiz extends BaseBiz<CommentEntityMapper,CommentEntity> {


    @Override
    protected String getPageName() {
        return null;
    }
}