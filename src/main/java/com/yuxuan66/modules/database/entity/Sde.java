package com.yuxuan66.modules.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * EVE官网SDE文件(EveSde)实体类
 *
 * @author Sir丶雨轩
 * @since 2021-12-14 14:34:00
 */
@Setter
@Getter
@TableName("eve_sde")
public class Sde implements Serializable {

    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 备份路径
     */
    private String url;
    /**
     * 本地路径
     */
    private String localPath;
    /**
     * 是否使用，当前只能存在一个使用中的版本
     */
    private Boolean isUse;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人ID
     */
    private Long createId;



}

