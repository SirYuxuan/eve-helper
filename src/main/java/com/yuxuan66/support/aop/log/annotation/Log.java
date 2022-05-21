package com.yuxuan66.support.aop.log.annotation;

import com.yuxuan66.support.aop.log.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sir丶雨轩
 * @since 2021/12/7
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    LogType type() default LogType.NORMAL;

    String value();
}
