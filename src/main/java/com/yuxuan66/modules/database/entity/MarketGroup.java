package com.yuxuan66.modules.database.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * EVE市场分组表(EveMarketGroup)实体类
 *
 * @author Sir丶雨轩
 * @since 2021-12-15 16:24:06
 */
@Setter
@Getter
@TableName("eve_market_group")
public class MarketGroup implements Serializable {
    /**
     * 市场分组ID
     */
    private Integer id;
    /**
     * 市场分组父ID
     */
    private Integer pid;
    /**
     * 名称
     */
    private String name;
    /**
     * 名称英文
     */
    private String nameEn;
    /**
     * 图标ID
     */
    private Integer iconId;
    /**
     * 描述
     */
    private String description;
    /**
     * 描述英文
     */
    private String descriptionEn;
    /**
     * 是否是类型
     */
    private Boolean hasType;
    /**
     * 图标文件名
     */
    private String iconFile;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    @TableField(exist = false)
    private Boolean isLeaf;

    @TableField(exist = false)
    private Boolean hasChildren;


}

