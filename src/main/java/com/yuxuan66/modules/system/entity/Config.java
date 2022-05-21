package com.yuxuan66.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuxuan66.annotation.Mapper;
import com.yuxuan66.support.basic.model.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.io.Serializable;

/**
 * (SysConfig)实体类
 *
 * @author makejava
 * @since 2021-12-08 09:44:09
 */

@Mapper
@Setter
@Getter
@TableName("sys_config")
public class Config extends BasicEntity implements Serializable {

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

