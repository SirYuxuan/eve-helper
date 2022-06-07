package com.yuxuan66.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * EVE市场交易订单表(EveOrder)实体类
 *
 * @author makejava
 * @since 2022-05-27 14:12:31
 */
@Setter
@Getter
@TableName("eve_order")
public class Order implements Serializable {
    private static final long serialVersionUID = -70615308100731773L;
    
    private Long id;
    
    private Long userId;
    
    private Long accountId;
    /**
     * 角色ID
     */
    private Integer characterId;
    /**
     * 角色名
     */
    private String characterName;
    /**
     * 是否是收购订单
     */
    private Integer isBuyOrder;
    /**
     * 收购位置
     */
    private String location;
    /**
     * 收购价格
     */
    private String price;
    /**
     * 具体内容
     */
    private String content;
    /**
     * 是否置顶
     */
    private Boolean top;
    
    private Timestamp createTime;



}

