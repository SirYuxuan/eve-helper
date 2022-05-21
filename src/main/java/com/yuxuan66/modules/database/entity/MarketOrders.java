package com.yuxuan66.modules.database.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * EVE市场订单(EveMarketOrders)实体类
 *
 * @author Sir丶雨轩
 * @since 2022-02-11 09:17:15
 */
@Setter
@Getter
@TableName("eve_market_orders")
public class MarketOrders implements Serializable {
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
     * 发布人
     */
    private String issued;
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
     * 星系ID
     */
    private Integer systemId;
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
    /**
     * 数据更新时间
     */
    private Timestamp updateTime;

    @TableField(exist = false)
    private Long orderId;

}

