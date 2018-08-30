package com.service.service.controller;


import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.service.service.biz.IssueBiz;
import com.service.service.entity.IssueEntity;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("issue")
@Api("问题研讨")
public class IssueController extends BaseController<IssueBiz,IssueEntity> {
    @RequestMapping(value = "/{iid}/labels", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse updateLabel(@PathVariable Integer iid, @RequestBody String labels) {
        baseBiz.modifyLabelsInIssue(iid,labels);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/milestone", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse updateMilestone(@RequestBody IssueEntity entity) {
        baseBiz.updateSelectiveById(entity);
        return new ObjectRestResponse().rel(true);
    }
}
