package com.github.hollykunge.security.auth.biz;

import com.github.hollykunge.security.auth.entity.ClientService;
import com.github.hollykunge.security.auth.mapper.ClientServiceMapper;
import com.github.hollykunge.security.common.biz.BaseBiz;
import org.springframework.stereotype.Service;

/**
 * @author ace
 * @create 2017/12/30.
 */
@Service
public class ClientServiceBiz extends BaseBiz<ClientServiceMapper,ClientService> {
    @Override
    protected String getPageName() {
        return null;
    }
}
