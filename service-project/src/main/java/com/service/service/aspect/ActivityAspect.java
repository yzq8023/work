package com.service.service.aspect;

import com.github.wxiaoqi.security.common.context.BaseContextHandler;
import com.service.service.ConfigUserService;
import com.service.service.biz.ActivityBiz;
import com.service.service.constant.OpTypeConstant;
import com.service.service.entity.ActivityEntity;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
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

    @Autowired
    public ActivityAspect(ActivityBiz activityBiz) {
        this.activityBiz = activityBiz;
    }

    private Logger logger = Logger.getLogger(getClass());

    @Pointcut("execution(public * com.service.service.controller.TaskController.remove(..))")
    public void removeTaskAspect(){}

    @Pointcut("execution(public * com.service.service.biz.MapUserTaskBiz.updateUsersInTask(..))")
    public void updateUsersInTaskAspect(){}
//    @Pointcut("execution(public * com.service.service.biz.TaskBiz.(..))")
//    public void createTaskAspect(){}
//    @Pointcut("execution(public * com.service.service.biz.ProjectBiz.*(..))")
//    public void projectAspect(){}
//
//    @Pointcut("execution(public * com.service.service.biz.IssueBiz.*(..))")
//    public void issueAspect(){}
//
//    @Pointcut("execution(public * com.service.service.biz.TeamBiz.*(..))")
//    public void teamAspect(){}

    @AfterReturning(returning = "result", pointcut = "removeTaskAspect()")
    public void doAfterTaskReturning(JoinPoint joinPoint, Object result) throws Throwable {
        logger.info(joinPoint.getSignature().getName());
        //获取访问目标方法
        activityBiz.updateActivity(result, OpTypeConstant.OP_CREATE);
    }

    @AfterReturning(returning = "result", pointcut = "updateUsersInTaskAspect()")
    public void doAfterMapReturning(JoinPoint joinPoint, Object result) throws Throwable {
        Object[] args = joinPoint.getArgs();
        logger.info(joinPoint.getSignature().getName());
        //获取访问目标方法
        activityBiz.updateActivity(result, OpTypeConstant.OP_CREATE);
    }
}
