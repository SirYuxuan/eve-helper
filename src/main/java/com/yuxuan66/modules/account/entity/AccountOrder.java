package com.yuxuan66.modules.account.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * EVE用户订单(EveAccountOrder)实体类
 *
 * @author makejava
 * @since 2022-05-24 11:12:09
 */
@Setter
@Getter
@TableName("eve_account_order")
public class AccountOrder implements Serializable {
    private static final long serialVersionUID = -78069634071247554L;
    
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
     * 剩余时间
     */
    private Integer duration;
    /**
     * 购买金额
     */
    private BigDecimal escrow;
    /**
     * 是否是购买订单
     */
    private Boolean isBuyOrder;
    /**
     * 是否是军团订单
     */
    private Integer isCorporation;
    private Integer typeId;
    private String typeName;
    /**
     * 发布时间
     */
    private Timestamp issued;
    /**
     * 位置ID
     */
    private Long locationId;
    /**
     * 位置名称
     */
    private String locationName;
    /**
     * 最小数量
     */
    private Integer minVolume;
    /**
     * 价格
     */
    private BigDecimal price;
    private BigDecimal minPrice;
    private BigDecimal jitaPrice;
    /**
     * 范围
     */
    @TableField("`range`")
    private String range;
    /**
     * 区域ID
     */
    private Integer regionId;
    /**
     * 区域名称
     */
    private String regionName;
    /**
     * 剩余数量
     */
    private Integer volumeRemain;
    /**
     * 合计数量
     */
    private Integer volumeTotal;
    /**
     * 创建时间
     */
    private Timestamp createTime;



}

