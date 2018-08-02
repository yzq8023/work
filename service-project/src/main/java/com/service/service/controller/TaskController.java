package com.service.service.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.biz.MapUserTaskBiz;
import com.service.service.biz.TaskBiz;
import com.service.service.entity.PathModel;
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
    MapUserTaskBiz mapUserTaskBiz;

    @Autowired
    public TaskController(TaskBiz taskBiz,
                          MapUserTaskBiz mapUserTaskBiz) {
        this.taskBiz = taskBiz;
        this.mapUserTaskBiz = mapUserTaskBiz;
    }

    @RequestMapping(value = "/{tid}/teams", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyTeamsInTask(@PathVariable Integer tid, String teams){
        taskBiz.modifyTeamsInTask(tid, teams);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{tid}/users", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyUsersInTask(@PathVariable Integer tid, String userIds){
        mapUserTaskBiz.updateUsersInTask(tid, userIds);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/joined", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<TaskEntity> joined(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return taskBiz.getJoinedTask(query);
    }

    @RequestMapping(value = "/project/joined", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Map<String,Object>> pjoined(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return taskBiz.getJoinedTaskFromProject(query);
    }

    @Override
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<TaskEntity> add(@RequestBody TaskEntity entity) {
        if (taskBiz.createTask(entity, getCurrentUserId())){
            baseBiz.insertSelective(entity);
        }
        return new ObjectRestResponse<TaskEntity>();
    }

    @RequestMapping(value = "/repository", method = RequestMethod.GET)
    @ResponseBody
    public List<PathModel> repository(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return taskBiz.getRepository(query);
    }
}















