package com.service.service.controller;

import com.github.wxiaoqi.security.common.rest.BaseController;
import com.service.service.biz.PullRequestBiz;
import com.service.service.entity.PullRequestEntity;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dk
 */
@RestController
@RequestMapping("pull")
@Api("合并请求")
public class PullRequestController extends BaseController<PullRequestBiz, PullRequestEntity> {

}
