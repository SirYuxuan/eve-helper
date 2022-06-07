package com.yuxuan66.modules.account.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户技能表(AccountSkill)实体类
 *
 * @author Sir丶雨轩
 * @since 2022-06-07 11:16:50
 */
@Getter
@Setter
@TableName("eve_account_skill")
public class AccountSkill implements Serializable {
    private static final long serialVersionUID = -73780574081534432L;
    
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 角色ID
     */
    private Long accountId;
    /**
     * 游戏角色ID
     */
    private Integer characterId;
    /**
     * 游戏角色名
     */
    private String characterName;
    /**
     * 技能完成时间
     */
    private Timestamp finishDate;
    /**
     * 技能完成的等级
     */
    private Integer finishedLevel;
    /**
     * 技能结束技能点
     */
    private Integer levelEndSp;
    /**
     * 技能开始技能点
     */
    private Integer levelStartSp;
    /**
     * 队列节点
     */
    private Integer queuePosition;
    /**
     * 技能ID
     */
    private Integer skillId;
    /**
     * 技能名称
     */
    private String skillName;
    /**
     * 开始时间
     */
    private Timestamp startDate;
    /**
     * 训练开始SP
     */
    private Integer trainingStartSp;
    
    private Timestamp createTime;



}

