package com.service.service.controller;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
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

    MapUserTaskBiz mapUserTaskBiz;
    TaskBiz taskBiz;

    @Autowired
    public TaskController(MapUserTaskBiz mapUserTaskBiz, TaskBiz taskBiz) {
        this.mapUserTaskBiz = mapUserTaskBiz;
        this.taskBiz = taskBiz;
    }

    @RequestMapping(value = "/{tid}/teams", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyTeamsInTask(@PathVariable Integer tid, String teams) {
        baseBiz.modifyTeamsInTask(tid, teams);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/joined", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<TaskEntity> joined(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return baseBiz.getJoinedTask(query);
    }

    @RequestMapping(value = "/project/joined", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Map<String, Object>> pjoined(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return baseBiz.getJoinedTaskFromProject(query);
    }

    @Override
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<TaskEntity> add(@RequestBody TaskEntity entity) {
        baseBiz.createTask(entity, getCurrentUserId());
        return new ObjectRestResponse<TaskEntity>();
    }

    @RequestMapping(value = "/repository", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<PathModel> repository(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return taskBiz.getRepository(query);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectRestResponse<TaskEntity> remove(@PathVariable Integer id) {
        if (baseBiz.deleteRepository(baseBiz.selectById(id))) {
            baseBiz.deleteById(id);
        }
        return new ObjectRestResponse<TaskEntity>();
    }

    @RequestMapping(value = "{taskId}/branches", method = RequestMethod.GET)
    @ResponseBody
    public List<String> branches(@PathVariable Integer taskId) {
        TaskEntity taskEntity = baseBiz.selectById(taskId);
        return taskBiz.getIntegrationBranch(taskEntity.getTaskName());
    }
}















