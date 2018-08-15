package com.service.service.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.biz.ActivityBiz;
import com.service.service.entity.Activity;
import com.service.service.exception.GitBlitException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: hollykunge
 * @Description:
 * @Date: 创建于 2018/8/13
 * @Modified:
 */
@RestController
@RequestMapping("act")
@Api("活动")
public class ActivityController {

    ActivityBiz activityBiz;
    @Autowired
    public ActivityController(ActivityBiz activityBiz) {
        this.activityBiz = activityBiz;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Activity> view(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return activityBiz.getActivity(query);
    }
}
