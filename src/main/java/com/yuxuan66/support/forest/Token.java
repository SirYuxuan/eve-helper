package com.yuxuan66.support.forest;

import com.dtflys.forest.annotation.MethodLifeCycle;
import com.dtflys.forest.annotation.RequestAttributes;

import java.lang.annotation.*;

/**
 * @author Sir丶雨轩
 * @since 2022/1/6
 */
@Documented
@MethodLifeCycle(HttpTokenLifeCycle.class)
@RequestAttributes
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
public @interface Token {
}
