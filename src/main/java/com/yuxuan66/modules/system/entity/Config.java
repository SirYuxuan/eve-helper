package com.yuxuan66.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuxuan66.support.basic.model.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * (SysConfig)实体类
 *
 * @author Sir丶雨轩
 * @since 2021-12-08 09:44:09
 */

@Setter
@Getter
@TableName("sys_config")
public class Config extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 8642887041220512276L;
    private Long id;
    /**
     * 配置名称
     */
    @TableField("`name`")
    private String name;
    /**
     * 配置值
     */
    @TableField("`value`")
    private String value;
    /**
     * 更新时间
     */
    private Timestamp updateTime;
    /**
     * 更新人
     */
    private Long updateId;
    /**
     * 更新时间
     */
    private String updateBy;



}

