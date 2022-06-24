package com.yuxuan66.modules.industry.entity.dto;

import com.yuxuan66.modules.database.entity.Type;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Sir丶雨轩
 * @since 6/14/2022
 */
@Setter
@Getter
public class TypeInfo {
    private Type type;
    /**
     * 链式分组
     */
    private String chainGrouping;

    /**
     * 是否可以制造
     */
    private Boolean isMake;
    /**
     * 最大流程数
     */
    private Integer maxLimit;
}
