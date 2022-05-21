package com.yuxuan66.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuxuan66.annotation.Mapper;
import com.yuxuan66.support.basic.model.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.io.Serializable;

/**
 * (SysLog)实体类
 *
 * @author makejava
 * @since 2021-12-07 15:15:48
 */
@Mapper
@Setter
@Getter
@TableName("sys_log")
public class Log extends BasicEntity implements Serializable {

    private Long id;
    /**
     * 日志标题
     */
    private String title;
    /**
     * 请求IP
     */
    private String ip;
    /**
     * 请求地址
     */
    private String city;
    /**
     * 浏览器
     */
    private String browser;
    /**
     * 请求耗时
     */
    private Long time;
    /**
     * 请求参数
     */
    private String param;
    /**
     * 请求返回结果
     */
    private String body;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 创建ID
     */
    private Long createId;
    /**
     * 创建人名称
     */
    private String createBy;




}

