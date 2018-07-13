package com.service.service.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.biz.TaskBiz;
import com.service.service.entity.TaskEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author dk
 */
@RestController
@RequestMapping("task")
@Api("任务管理")
public class TaskController extends BaseController<TaskBiz, TaskEntity> {

    TaskBiz taskBiz;

    @Autowired
    public TaskController(TaskBiz taskBiz) {
        this.taskBiz = taskBiz;
    }


    @RequestMapping(value = "/{tid}/task", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyTaskTeams(@PathVariable Integer tid, String teams){
        baseBiz.modifyTaskTeams(tid, teams);
        return new ObjectRestResponse().rel(true);
    }
    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<TaskEntity> list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<TaskEntity> repositories = taskBiz.getRepositories(query);
        return baseBiz.selectTaskById(query);
    }

    @Override
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<TaskEntity> add(@RequestBody TaskEntity entity) {
        taskBiz.createTask(entity, getCurrentUserName());
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<TaskEntity>();
    }
}















