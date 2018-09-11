package com.service.service.controller;

import com.github.wxiaoqi.security.common.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.biz.TicketBiz;
import com.service.service.entity.ProjectEntity;
import com.service.service.entity.TicketModel;
import com.service.service.entity.TicketModel.*;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: hollykunge
 * @Description:
 * @Date: 创建于 2018/9/6
 * @Modified:
 */
@RestController
@RequestMapping("pull")
@Api("合并请求")
public class TicketController extends BaseController<TicketBiz, TicketModel> {

    @Override
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<TicketModel> add(@RequestBody TicketModel entity) {
        baseBiz.createPullRequest(entity);
        return new ObjectRestResponse<TicketModel>();
    }

    @RequestMapping(value = "merge", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<TicketModel> merge(@RequestBody TicketModel entity) {
        baseBiz.createPullRequest(entity);
        return new ObjectRestResponse<TicketModel>();
    }

}
