package com.service.service.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.service.service.biz.PullRequestBiz;
import com.service.service.entity.PullRequestEntity;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: hollykunge
 * @Description:
 * @Date: 创建于 2018/9/6
 * @Modified:
 */
@RestController
@RequestMapping("pull")
@Api("合并请求")
public class PullRequestController extends BaseController<PullRequestBiz, PullRequestEntity> {

    @Override
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<PullRequestEntity> add(@RequestBody PullRequestEntity entity) {
        baseBiz.createPullRequest(entity);
        return new ObjectRestResponse<PullRequestEntity>();
    }

    @RequestMapping(value = "merge", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse merge(@PathVariable Integer id, @PathVariable String name) {
        baseBiz.merge(id, name);
        return new ObjectRestResponse().rel(true);
    }

}
