package com.yuxuan66.common.utils.tree;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sir丶雨轩
 * @since 2021/6/23
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeParent {

    /**
     * 默认的id
     * @return id
     */
    long defaultId() default 0L;
}

