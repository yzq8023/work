package com.service.service.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.service.service.biz.GraphBiz;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author dk
 */
@RestController
@RequestMapping("graph")
@Api("任务管理")
public class GraphController {

    GraphBiz graphBiz;

    @Autowired
    public GraphController(GraphBiz graphBiz) {
        this.graphBiz = graphBiz;
    }

    @RequestMapping(value = "/{id}/flow", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse flow(@PathVariable Integer id) {

        return new ObjectRestResponse().rel(true);
    }
}
