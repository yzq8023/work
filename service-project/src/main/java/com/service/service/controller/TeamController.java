package com.service.service.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.biz.TeamBiz;
import com.service.service.entity.TeamEntity;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author dk
 */
@RestController
@RequestMapping("team")
@Api("团队管理")
public class TeamController extends BaseController<TeamBiz, TeamEntity> {
    @RequestMapping(value = "/{id}/team", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifiyTeamUsers(@PathVariable Integer id, String members){
        baseBiz.modifiyTeamUsers(id, members);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/page/jointeam", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<TeamEntity> page(@RequestParam Map<String, Object> params){

        Query query = new Query(params);
        return baseBiz.selectTeamByUserId(query);
    }
 }
