/*
package com.service.service.controller;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.service.service.biz.GraphBiz;
import com.service.service.entity.PathModel;
import com.service.service.vo.DataFlow;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

*/
/**
 * @author dk
 *//*

@RestController
@RequestMapping("graph")
@Api("任务管理")
public class GraphController {

    GraphBiz graphBiz;

    @Autowired
    public GraphController(GraphBiz graphBiz) {
        this.graphBiz = graphBiz;
    }

    @RequestMapping(value = "/flow", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<DataFlow> flow(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return new ObjectRestResponse().rel(true);
    }
}
*/
