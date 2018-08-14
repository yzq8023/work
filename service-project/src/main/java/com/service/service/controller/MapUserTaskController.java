package com.service.service.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.service.service.biz.MapUserTaskBiz;
import com.service.service.entity.MapUserTask;
import com.service.service.exception.GitBlitException;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * @author hollykunge
 * @date 2018/7/18
 * @des
 */
@RestController
@RequestMapping("map")
@Api("任务用户关系管理")
public class MapUserTaskController extends BaseController<MapUserTaskBiz, MapUserTask> {

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse modify(@PathVariable  int id, String userIds) throws GitBlitException {
        baseBiz.updateUsersInTask(id, userIds);
        return new ObjectRestResponse().rel(true);
    }
}
