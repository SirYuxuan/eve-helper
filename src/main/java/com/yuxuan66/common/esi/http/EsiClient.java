package com.yuxuan66.common.esi.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestResponse;
import com.yuxuan66.modules.account.entity.Account;
import com.yuxuan66.support.forest.Token;

import java.util.Set;

/**
 * ESI的HTTP请求
 *
 * @author Sir丶雨轩
 * @since 2022/1/6
 */
@BaseRequest(baseURL = "{url}")
public interface EsiClient {

    /**
     * 获取用户钱包余额
     *
     * @param account 用户
     * @return body
     */
    @Get("characters/{account.characterId}/wallet/")
    @Token
    String charactersWallet(@Var("account") Account account);

    /**
     * 获取用户所在位置
     *
     * @param account 用户
     * @return body
     */
    @Get("characters/{account.characterId}/location/")
    @Token
    JSONObject charactersLocation(@Var("account") Account account);

    /**
     * 获取用户驾驶舰船
     *
     * @param account 用户
     * @return body
     */
    @Get("characters/{account.characterId}/ship/")
    @Token
    JSONObject charactersShip(@Var("account") Account account);

    /**
     * 获取一个角色的技能点总数
     *
     * @param account 用户
     * @return body
     */
    @Get("characters/{account.characterId}/skills/")
    @Token
    String charactersSkills(@Var("account") Account account);

    /**
     * 获取一个角色的名称
     *
     * @param id 角色id
     * @return 角色名称
     */
    @Get("characters/{id}/")
    String charactersName(@Var("id") Integer id);

    /**
     * 获取一个npc空间站的信息
     * @param id 空间站id
     * @return 空间站信息
     */
    @Get("universe/stations/{id}/")
    String universeStations(@Var("id") Integer id);

    /**
     * 获取一个区域的名字
     * @param id 区域id
     * @return 区域信息
     */
    @Get("universe/regions/{id}/?language=zh")
    JSONObject universeRegions(@Var("id") Integer id);

    /**
     * 获取一个玩家建筑的信息
     * @param id 玩家建筑id
     * @return 玩家建筑信息
     */
    @Get("universe/structures/{id}/")
    @Token
    String universeStructures(@Var("account") Account account,@Var("id") long id);
    /**
     * 获取一个角色的名称
     *
     * @param id 角色id
     * @return 角色名称
     */
    @Get("universe/systems/{id}/")
    String universeSystems(@Var("id") Integer id);

    /**
     * 获取一个角色军团历史
     *
     * @param id 角色id
     * @return 军团历史
     */
    @Get("characters/{id}/corporationhistory/?datasource=tranquility")
    String charactersCorporationHistory(@Var("id") Integer id);

    /**
     * 获取用户钱包流水
     *
     * @param account 用户
     * @param page        页码
     * @return 钱包流水
     */
    @Get("characters/{account.characterId}/wallet/journal?page={page}")
    @Token
    ForestResponse<JSONArray> charactersWalletJournal(@Var("account") Account account, @Var("page") int page);

    /**
     * 获取用户市场交易
     *
     * @param account 用户
     * @return 市场交易
     */
    @Get("characters/{account.characterId}/wallet/transactions")
    @Token
    JSONArray charactersWalletTransactions(@Var("account") Account account);


    /**
     * 查询一个用户的订单历史
     *
     * @param account 用户
     * @param page        订单历史
     * @return 订单历史
     */
    @Get("characters/{account.characterId}/orders/history?page={page}")
    @Token
    ForestResponse<JSONArray> charactersOrdersHistory(@Var("account") Account account, @Var("page") int page);

    /**
     * 查询一个用户的订单
     *
     * @param account 用户
     * @return 订单
     */
    @Get("characters/{account.characterId}/orders/")
    @Token
    JSONArray charactersOrders(@Var("account") Account account);

    /**
     * 获取军团建筑列表
     * @param account 总监或有权限的账号
     * @param page 页码
     * @return 标准返回
     */
    @Get("corporations/{account.corpId}/structures/?datasource=tranquility&language=zh&page={page}")
    @Token
    ForestResponse<JSONArray> corporationsStructures(@Var("account") Account account, @Var("page") int page);

    /**
     * 获取军团月矿牵引情况
     * @param account 总监或有权限的账号
     * @param page 页码
     * @return 标准返回
     */
    @Get("corporation/{account.corpId}/mining/extractions/?datasource=tranquility&page={page}")
    @Token
    ForestResponse<JSONArray> corporationsExtractions(@Var("account") Account account, @Var("page") int page);

    /**
     * 获取用户军团的合同信息
     *
     * @param account 用户
     * @return 合同列表
     */
    @Get("corporations/{account.corpId}/contracts?page={page}")
    @Token
    ForestResponse<JSONArray> corporationsContracts(@Var("account") Account account, @Var("page") int page);

    /**
     * 获取用户军团信息
     *
     * @param account 用户
     * @return 军团成员
     */
    @Get("corporations/{account.corpId}/members")
    @Token
    JSONArray corporationsMembers(@Var("account") Account account);

 /**
     * 获取用户头衔
     *
     * @param account 总监
     * @return 头衔数据
     */
    @Get("corporations/{account.corpId}/titles/?datasource=tranquility")
    @Token
    JSONArray corporationsTitles(@Var("account") Account account);

    /**
     * 获取军团成员的头衔
     * @param account 总监权限
     * @return 成员头衔
     */
    @Get("corporations/{account.corpId}/members/titles/?datasource=tranquility")
    @Token
    JSONArray corporationsMembersTitles(@Var("account") Account account);

    /**
     * 获取用户技能队列
     *
     * @param account 用户
     * @return 技能队列
     */
    @Get("characters/{account.characterId}/skillqueue")
    @Token
    JSONArray charactersSkillQueue(@Var("account") Account account);

    /**
     * 获取用户舰队
     *
     * @param account 用户
     * @return 舰队
     */
    @Get("characters/{account.characterId}/fleet")
    @Token
    JSONObject charactersFleet(@Var("account") Account account);



    /**
     * 获取指定id列表的中文
     *
     * @param ids id列表
     * @return 名称列表
     */
    @Post("universe/names")
    JSONArray universeNames(@JSONBody Set<Integer> ids);

    /**
     * 获取指定名称列表的id
     *
     * @param names 名称列表
     * @return id列表
     */
    @Post("universe/ids")
    JSONObject universeIds(@JSONBody Set<String> names);


    /**
     * 获取军团钱包流水
     *
     * @param account 军团总监
     * @param corpId      军团id
     * @param division    部门
     * @param page        页码
     * @return 钱包流水
     */
    @Get("corporations/{corpId}/wallets/{division}/journal?page={page}")
    @Token
    ForestResponse<JSONArray> corporationsWalletsJournal(@Var("account") Account account, @Var("corpId") Integer corpId, @Var("division") Integer division, @Var("page") int page);

    /**
     * 获取个人的采矿明细
     *
     * @param account 角色
     * @param page        页码
     * @return 采矿明细
     */
    @Get("characters/{account.characterId}/mining/?page={page}")
    @Token
    ForestResponse<JSONArray> charactersMining(@Var("account") Account account, @Var("page") int page);

    /**
     * 查询玩家建筑订单
     * @param account 角色
     * @param id 建筑id
     * @param page 页码
     * @return 数据
     */
    @Get("markets/structures/{id}/?page={page}")
    @Token
    ForestResponse<JSONArray> marketsStructures(@Var("account") Account account,@Var("id") Long id, @Var("page") int page);

    /**
     * 查询NPC建筑订单
     * @param id 建筑id
     * @param page 页码
     * @return 数据
     */
    @Get("markets/{id}/orders/?page={page}&order_type=all")
    ForestResponse<JSONArray> marketsRegion(@Var("id") Integer id, @Var("page") int page);

    /**
     * 获取物品的基准价格
     * @return 基准价格
     */
    @Get("markets/prices")
    JSONArray marketsPrices();






}
