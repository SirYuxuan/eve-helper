package com.yuxuan66.modules.account.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * EVE角色订单历史(EveAccountOrderHistory)实体类
 *
 * @author Sir丶雨轩
 * @since 2022-05-21 16:40:31
 */
@Setter
@Getter
@TableName("eve_account_order_history")
public class AccountOrderHistory implements Serializable {
    private static final long serialVersionUID = -21693221664878917L;
    /**
     * ESI 订单ID
     */
    private Long id;
    /**
     * 区域ID
     */
    private Integer regionId;
    /**
     * 持续时间
     */
    private Integer duration;
    /**
     * 是否是收购订单
     */
    private Boolean isBuyOrder;
    /**
     * 发布时间
     */
    private Timestamp issued;
    /**
     * 位置ID
     */
    private Long locationId;
    /**
     * 最小数据
     */
    private Integer minVolume;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 范围
     */
    private String range;

    /**
     * 类型ID
     */
    private Integer typeId;
    /**
     * 体积
     */
    private Integer volumeRemain;
    /**
     * 总数量
     */
    private Integer volumeTotal;

    private Long accountId;
    private Long userId;
    /**
     * 角色ID
     */
    private Integer characterId;
    /**
     * 角色名
     */
    private String characterName;
    /**
     * 数据更新时间
     */
    private Date createTime;
    /**
     * 购买订单金额
     */
    private BigDecimal escrow;
    /**
     * 区域名称
     */
    private String regionName;
    /**
     * 是否是军团订单
     */
    private Boolean isCorporation;
    /**
     * 位置名称
     */
    private String locationName;

    /**
     * 当前状态
     */
    private String state;
    /**
     * 物品名称
     */
    private String typeName;


}

