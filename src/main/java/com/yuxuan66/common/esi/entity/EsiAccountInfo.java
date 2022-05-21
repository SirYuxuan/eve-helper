package com.yuxuan66.common.esi.entity;


import lombok.Getter;
import lombok.Setter;

/**
 * Esi 一个角色详细信息
 * @author Sir丶雨轩
 * @since 2021/7/27
 */
@Setter
@Getter
public class EsiAccountInfo {

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


}