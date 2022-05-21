package com.yuxuan66.common.esi.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 授权完成后的信息
 * @author Sir丶雨轩
 * @since 2021/7/27
 */
@Setter
@Getter
public class EsiTokenInfo {

    /**
     * accessToken
     */
    private String accessToken;

    /**
     * refreshToken
     */
    private String refreshToken;

    /**
     * 角色ID
     */
    private Integer characterId;

    /**
     * 角色名字
     */
    private String name;

}