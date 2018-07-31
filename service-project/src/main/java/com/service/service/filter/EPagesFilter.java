package com.service.service.filter;

import com.service.service.managers.IAuthenticationManager;
import com.service.service.managers.IRepositoryManager;
import com.service.service.managers.IRuntimeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.annotation.WebFilter;

import static com.service.service.Constants.PAGES;

/**
 * @auther: dk
 * @date: 2018/7/6
 * @des:
 */
//@WebFilter(PAGES)
public class EPagesFilter extends DRawFilter {
    @Autowired
    public EPagesFilter(IRuntimeManager runtimeManager,
                        IAuthenticationManager authenticationManager,
                        IRepositoryManager repositoryManager) {
        super(runtimeManager, authenticationManager, repositoryManager);
    }
}
