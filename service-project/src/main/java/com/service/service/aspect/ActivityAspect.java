package com.service.service.aspect;

import com.github.wxiaoqi.security.common.context.BaseContextHandler;
import com.service.service.ConfigUserService;
import com.service.service.biz.ActivityBiz;
import com.service.service.constant.OpTypeConstant;
import com.service.service.entity.ActivityEntity;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: hollykunge
 * @Description:
 * @Date: 创建于 2018/8/27
 * @Modified:
 */
@Aspect
@Component
public class ActivityAspect {

    ActivityBiz activityBiz;
    ConfigUserService configUserService;

    @Autowired
    public ActivityAspect(ActivityBiz activityBiz, ConfigUserService configUserService) {
        this.activityBiz = activityBiz;
        this.configUserService = configUserService;
    }

    private Logger logger = Logger.getLogger(getClass());

    @Pointcut("execution(public * com.service.service.biz.TaskBiz.createTask(..))")
    public void taskAspect(){}

//    @Pointcut("execution(public * com.service.service.biz.ProjectBiz.*(..))")
//    public void projectAspect(){}
//
//    @Pointcut("execution(public * com.service.service.biz.IssueBiz.*(..))")
//    public void issueAspect(){}
//
//    @Pointcut("execution(public * com.service.service.biz.TeamBiz.*(..))")
//    public void teamAspect(){}

    @AfterReturning(returning = "taskEntity", pointcut = "taskAspect()")
    public void doAfterReturning(TaskEntity taskEntity) throws Throwable {
        //获取访问目标方法
        activityBiz.updateActivity(taskEntity, OpTypeConstant.OP_CREATE);
    }
}
