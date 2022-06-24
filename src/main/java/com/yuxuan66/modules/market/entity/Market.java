package com.yuxuan66.modules.market.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Sir丶雨轩
 * @since 6/23/2022
 */
@Setter
@Getter
@TableName("market")
public class Market {

    // 物品ID
    private int typeId;
    // 物品名错
    private String typeName;

    private int marketGroupId;

    // 本地市场出售价格
    private long sellPrice;
    // 本地市场收购价格
    private long buyPrice;

    // 本地市场在售数量
    private long sellNum;
    // 本地市场收购数量
    private long buyNum;

    // 本月完成交易数量
    @TableField("`order`")
    private long order;

    // 本地市场高位
    private long highest;
    // 本地市场低位
    private long lowest;

    private BigDecimal jita;

    private String meta;

    // 吉他售价
    private long jitaSellPrice;
    // 吉他收购
    private long jitaBuyPrice;


}
