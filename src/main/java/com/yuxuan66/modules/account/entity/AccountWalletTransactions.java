package com.yuxuan66.modules.account.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * EVE角色市场交易(EveAccountWalletTransactions)实体类
 *
 * @author makejava
 * @since 2022-05-21 17:09:30
 */
@Setter
@Getter
@TableName("eve_account_wallet_transactions")
public class AccountWalletTransactions implements Serializable {
    private static final long serialVersionUID = -41806156176184825L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountWalletTransactions that = (AccountWalletTransactions) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * ESI 订单ID
     */
    private Long id;
    
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
     * 客户ID
     */
    private Integer clientId;
    /**
     * 客户名称
     */
    private String clientName;
    private String clientType;
    /**
     * 订单时间
     */
    private Timestamp date;
    /**
     * 是否是收购订单
     */
    private Integer isBuy;
    /**
     * 私人订单？
     */
    private Integer isPersonal;
    /**
     * 关联订单
     */
    private Long journalRefId;
    /**
     * 位置ID
     */
    private Long locationId;
    /**
     * 位置名称
     */
    private String locationName;
    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 类型ID
     */
    private Integer typeId;
    /**
     * 物品名称
     */
    private String typeName;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 数据更新时间
     */
    private Timestamp createTime;



}

