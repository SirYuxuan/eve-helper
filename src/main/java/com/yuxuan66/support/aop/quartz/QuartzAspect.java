package com.yuxuan66.support.aop.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Sir丶雨轩
 * @since 2021/12/7
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class QuartzAspect {

    @Pointcut("target(org.quartz.Job)")
    public void quartz() {
    }

    @Around("quartz()")
    public Object quartzCheck(ProceedingJoinPoint pjp) throws Throwable {
        if(File.separatorChar == '\\'){
            log.info("定时任务触发，Window环境下禁止执行");
            return null;
        }
        return pjp.proceed();
    }
}
