package com.yuxuan66.modules.account.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * EVE-授权角色(EveAccount)实体类
 *
 * @author Sir丶雨轩
 * @since 2022-05-21 14:39:48
 */
@Setter
@Getter
@TableName("eve_account")
public class Account implements Serializable {
    private static final long serialVersionUID = 310997689833459177L;
    
    private Long id;
    /**
     * 角色名
     */
    private String characterName;
    /**
     * 角色ID
     */
    private Integer characterId;
    private String accountName;
    /**
     * 当前剩余ISK
     */
    private Long isk;
    /**
     * 技能点数
     */
    private Integer skillNum;
    private String skillName;
    private String skillEndTime;

    @TableField(exist = false)
    private long scientificResearch;
    @TableField(exist = false)
    private long make;
    @TableField(exist = false)
    private long reaction;

    private Integer scientificLine;
    private Integer makeLine;
    private Integer reactionLine;

    private Boolean isMain;

    /**
     * 舰船名称
     */
    private String shipName;
    /**
     * 当前驾驶舰船
     */
    private String shipTypeName;
    private Integer shipTypeId;
    /**
     * 当前所在位置
     */
    private String solarSystemName;
    private String localName;
    /**
     * 服务器1=欧服,2=国服
     */
    private Integer server;
    /**
     * 系统用户ID
     */
    private Long userId;
    /**
     * 军团ID
     */
    private Long corpId;
    /**
     * 军团名称
     */
    private String corpName;
    /**
     * 联盟ID
     */
    private Long allianceId;
    /**
     * 联盟名称
     */
    private String allianceName;
    
    private String accessToken;
    
    private String refreshToken;
    
    /**
     * 创建时间
     */
    private Timestamp createTime;

    @TableField(exist = false)
    private List<AccountSkill> accountSkillList;


}

