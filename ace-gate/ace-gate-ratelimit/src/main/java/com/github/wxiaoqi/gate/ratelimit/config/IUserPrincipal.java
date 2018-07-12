package com.github.wxiaoqi.gate.ratelimit.config;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 协同设计小组 on 2017/9/23.
 */
public interface IUserPrincipal {
    String getName(HttpServletRequest request);
}
