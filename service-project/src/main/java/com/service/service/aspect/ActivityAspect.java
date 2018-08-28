package com.service.service.aspect;

import com.service.service.biz.ActivityBiz;
import com.service.service.entity.ActivityEntity;
import com.service.service.entity.TaskEntity;
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
    ActivityEntity activityEntity;

    @Autowired
    public ActivityAspect(ActivityBiz activityBiz, ActivityEntity activityEntity) {
        this.activityBiz = activityBiz;
        this.activityEntity = activityEntity;
    }

    private Logger logger = Logger.getLogger(getClass());

    @Pointcut("execution(public * com.service.service.biz.TaskBiz.*(..))")
    public void taskAspect(){}

    @Pointcut("execution(public * com.service.service.biz.ProjectBiz.*(..))")
    public void projectAspect(){}

    @Pointcut("execution(public * com.service.service.biz.IssueBiz.*(..))")
    public void issueAspect(){}

    @Pointcut("execution(public * com.service.service.biz.TeamBiz.*(..))")
    public void teamAspect(){}

    @AfterReturning(returning = "taskEntity", pointcut = "taskAspect()")
    public void doAfterReturning(TaskEntity taskEntity) throws Throwable {
        // 处理完请求，返回内容
        logger.info("RESPONSE : " + taskEntity);

        activityEntity.setActUserId();
        activityEntity.setActUserName();
        activityEntity.setContent();
        activityEntity.setIsPrivate();
        activityEntity.setOpType();
        activityEntity.setRefName();
        activityEntity.setRepoId();
        activityEntity.setRepoUserName();
        activityEntity.setRepoName();
        activityEntity.setUserId();

        activityBiz.insertSelective(activityEntity);
    }
}
