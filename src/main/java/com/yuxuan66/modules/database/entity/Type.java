package com.yuxuan66.modules.database.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * EVE所有的物品ID和名称(EveType)实体类
 *
 * @author Sir丶雨轩
 * @since 2021-12-15 14:49:32
 */
@Setter
@Getter
@TableName("eve_type")
public class Type implements Serializable {

    private Integer id;
    /**
     * 物品名称
     */
    private String name;
    /**
     * 物品名称英文
     */
    private String nameEn;
    /**
     * 物品描述
     */
    private String description;
    /**
     * 物品描述英文
     */
    private String descriptionEn;
    /**
     * 分组ID
     */
    private Integer groupId;
    private String groupName;
    private String groupNameEn;

    private Integer metaGroupId;
    private String metaGroupName;
    private String metaGroupNameEn;

    private Integer marketGroupId;
    private String marketGroupName;
    private String marketGroupNameEn;

    private Integer categoryId;
    private String categoryName;
    private String categoryNameEn;



    @TableField(exist = false)
    private Price price;

    @TableField(exist = false)
    private Boolean isLeaf = true;

    @Setter
    @Getter
    public static class Price{
        private BigDecimal jitaSell;
        private BigDecimal jitaBuy;

        private BigDecimal dodexieSell;
        private BigDecimal dodexieBuy;

        private BigDecimal amarrSell;
        private BigDecimal amarrBuy;

        private BigDecimal hekSell;
        private BigDecimal hekBuy;

        private BigDecimal rensSell;
        private BigDecimal rensBuy;

    }



}

