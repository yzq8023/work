package com.service.service.controller;


import com.github.wxiaoqi.security.api.vo.user.UserInfo;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.service.service.biz.IssueBiz;
import com.service.service.entity.IssueEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yanzhenqing
 */
@RestController
@RequestMapping("issue")
@Api("问题研讨")
public class IssueController extends BaseController<IssueBiz,IssueEntity> {

    IssueBiz issueBiz;

    @Autowired
    public IssueController(IssueBiz issueBiz) {
        this.issueBiz = issueBiz;
    }

    @RequestMapping(value = "/{iid}/labels", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse updateLabel(@PathVariable Integer iid, @RequestBody String labels) {
        baseBiz.modifyLabelsInIssue(iid,labels);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{iid}/users", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<UserInfo>> updateMilestone(@PathVariable Integer iid,Integer taskId) {
        List<UserInfo> users = baseBiz.getJoinedUsersFromIssue(iid,taskId);
        return new ObjectRestResponse().data(users).rel(true);
    }
}
