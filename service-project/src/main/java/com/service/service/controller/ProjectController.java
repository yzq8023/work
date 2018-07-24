package com.service.service.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.biz.ProjectBiz;
import com.service.service.entity.ProjectEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author dk
 */
@RestController
@RequestMapping("project")
@Api("项目管理")
public class ProjectController extends BaseController<ProjectBiz, ProjectEntity> {


    @RequestMapping(value = "/joined", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<ProjectEntity> joined(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return baseBiz.getJoinedProject(query);
    }
}
