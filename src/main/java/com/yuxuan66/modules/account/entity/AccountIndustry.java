package com.yuxuan66.modules.account.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * EVE角色工业表(AccountIndustry)实体类
 *
 * @author Sir丶雨轩
 * @since 2022-06-07 17:08:07
 */
@Getter
@Setter
@TableName("eve_account_industry")
public class AccountIndustry implements Serializable {
    private static final long serialVersionUID = -74727207941567423L;
    
    private Long id;
    
    private Long userId;
    @TableField(exist = false)
    private Integer installerId;
    
    private Long accountId;
    
    private Integer characterId;
    
    private String characterName;
    /**
     * 完成时间
     */
    private Timestamp endDate;
    /**
     * 工业成本
     */
    private BigDecimal cost;
    
    private Integer licensedRuns;
    /**
     * 成功率
     */
    private String probability;
    /**
     * 产出物品ID
     */
    private Integer productTypeId;
    /**
     * 产出物品名称
     */
    private String productTypeName;
    /**
     * 蓝图ID
     */
    private Integer blueprintTypeId;
    /**
     * 蓝图名称
     */
    private String blueprintTypeName;
    /**
     * 耗时
     */
    private Integer duration;
    /**
     * 类型
     */
    private Integer activityId;
    /**
     * 执行流程
     */
    private Integer runs;
    /**
     * 开始时间
     */
    private Timestamp startDate;
    /**
     * 状态
     */
    private String status;
    
    private Timestamp createTime;



}

