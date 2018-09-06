package com.service.service.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author: hollykunge
 * @Description:
 * @Date: 创建于 2018/9/6
 * @Modified:
 */
@Aspect
@Component
public class PushAspect {
    public PushAspect() {
    }
    private Logger logger = Logger.getLogger(getClass());

    @Pointcut("execution(public * com.service.service.git.GitblitReceivePack.onPostReceive(..))")
    public void onPostReceiveAspect(){}

    @AfterReturning(returning = "result", pointcut = "onPostReceiveAspect()")
    public void doAfterReturning(JoinPoint joinPoint, Object result){
        logger.info(joinPoint.getSignature().getName());

    }

}
