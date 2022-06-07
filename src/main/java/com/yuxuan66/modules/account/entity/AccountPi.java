package com.yuxuan66.modules.account.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * EVE角色行星开发(AccountPi)实体类
 *
 * @author Sir丶雨轩
 * @since 2022-06-07 14:54:22
 */
@Getter
@Setter
@TableName("eve_account_pi")
public class AccountPi implements Serializable {
    private static final long serialVersionUID = -67327440007619261L;
    
    private Long id;

    @TableField(exist = false)
    private Integer planetId;
    
    private Long userId;
    @TableField(exist = false)
    private String accountName = "未设置";
    
    private Long accountId;
    
    private Integer characterId;
    
    private String characterName;
    /**
     * 行星类型
     */
    private String planetType;
    /**
     * 产出内容
     */
    private String produce;
    /**
     * 设备数量
     */
    private Integer numPins;
    /**
     * 最后更新时间
     */
    private Timestamp lastUpdate;
    /**
     * 所在星系ID
     */
    private Integer solarSystemId;
    /**
     * 所在星系
     */
    private String solarSystemName;
    /**
     * 指挥中心等级
     */
    private Integer upgradeLevel;
    /**
     * 创建时间
     */
    private Timestamp createTime;



}

