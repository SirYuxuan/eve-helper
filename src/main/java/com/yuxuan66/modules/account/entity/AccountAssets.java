package com.yuxuan66.modules.account.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * EVE角色资产表(EveAccountAssets)实体类
 *
 * @author makejava
 * @since 2022-05-25 13:46:09
 */
@Setter
@Getter
@TableName("eve_account_assets")
public class AccountAssets implements Serializable {
    private static final long serialVersionUID = 492467838781677182L;
    
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
     * 是否是蓝图副本
     */
    private Boolean isBlueprintCopy;
    /**
     * 是否是单独
     */
    private Boolean isSingleton;
    /**
     * 物品ID
     */
    private Long itemId;
    /**
     * 物品名称
     */
    private String itemName;
    /**
     * 位置标记
     */
    private String locationFlag;
    /**
     * 位置ID
     */
    private Long locationId;
    /**
     * 位置名称
     */
    private String locationName;
    /**
     * 位置类型
     */
    private String locationType;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 物品ID
     */
    private Integer typeId;
    /**
     * 物品名称
     */
    private String typeName;
    /**
     * 创建时间
     */
    private Timestamp createTime;

    private Integer count;



}

