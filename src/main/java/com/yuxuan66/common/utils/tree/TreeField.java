package com.yuxuan66.common.utils.tree;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sir丶雨轩
 * @since 2021/6/23
 */
/**
 * 表示树的节点
 *
 * @author Sir丶雨轩
 * @date 2020/9/21
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeField {

    /**
     * 树中名称
     *
     * @return 默认取字段名称
     */
    String value() default "";

    /**
     * 模仿某个字段
     * @return
     */
    String like() default "";

    /**
     * 仅第一级显示
     * @return
     */
    boolean onlyFirst() default false;

    /**
     * 二级
     * @return
     */
    String secondLevel() default "";

    /**
     * 二级名称
     * @return
     */
    String secondLevelName() default "";

}
