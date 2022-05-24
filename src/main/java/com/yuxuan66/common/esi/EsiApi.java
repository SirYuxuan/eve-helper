package com.yuxuan66.common.esi;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dtflys.forest.http.ForestResponse;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.cache.RedisUtil;
import com.yuxuan66.common.esi.entity.EsiAccountInfo;
import com.yuxuan66.common.esi.entity.EsiTokenInfo;
import com.yuxuan66.common.esi.http.EsiClient;
import com.yuxuan66.config.SystemConfig;
import com.yuxuan66.modules.account.entity.Account;
import com.yuxuan66.modules.account.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * EVE，ESI操作
 *
 * @author Sir丶雨轩
 * @since 2021/10/26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EsiApi {

    private final EsiClient esiClient;
    private final SystemConfig config;
    private final RedisUtil redis;
    @Resource
    private AccountMapper accountMapper;

    /**
     * 通过授权后的Code换取Info信息
     *
     * @param code 授权后的Code
     * @return token信息
     */
    public EsiTokenInfo codeToInfo(String code) {

        Map<String, Object> param = new HashMap<>() {{
            put("grant_type", "authorization_code");
            put("code", code);
        }};

        HttpRequest request = HttpUtil.createPost("https://login.eveonline.com/v2/oauth/token");
        request.basicAuth(config.getVal(CacheKey.Config.EVE.EVE_ESI_CLIENT_ID), config.getVal(CacheKey.Config.EVE.EVE_ESI_SECRET_KEY));

        request.form(param);

        String requestBody = request.execute().body();

        if (!JSONUtil.isJson(requestBody)) {
            return null;
        }

        JSONObject token = JSONObject.parseObject(requestBody);

        String accessToken = token.getString("access_token");
        String[] accessTokenArr = accessToken.split("\\.");
        if (accessTokenArr.length < 2) {
            return null;
        }
        String tokenBase64 = accessTokenArr[1];

        JSONObject userInfo = JSONObject.parseObject(Base64.decodeStr(tokenBase64));

        String characterId = userInfo.getString("sub").split(":")[2];
        EsiTokenInfo tokenInfo = new EsiTokenInfo();
        tokenInfo.setAccessToken(token.getString("access_token"));
        tokenInfo.setRefreshToken(token.getString("refresh_token"));
        tokenInfo.setName(userInfo.getString("name"));
        tokenInfo.setCharacterId(Convert.toInt(characterId));

        return tokenInfo;

    }

    /**
     * 刷新指定角色的Token
     *
     * @param account 角色
     */
    public boolean refreshToken(Account account) {

        if (account.getAccessToken() == null) {
            return false;
        }

        // token 过期时间
        long exp = JWT.decode(account.getAccessToken()).getClaims().get("exp").asLong();

        if (exp < System.currentTimeMillis() / 1000) {
            HttpRequest request = HttpUtil.createPost("https://login.eveonline.com/v2/oauth/token");
            request.form("grant_type", "refresh_token");
            request.form("refresh_token", account.getRefreshToken());
            request.header("Authorization", "Basic " + Base64.encode(config.getVal(CacheKey.Config.EVE.EVE_ESI_CLIENT_ID) + ":" + config.getVal(CacheKey.Config.EVE.EVE_ESI_SECRET_KEY)));
            JSONObject tokenInfo = JSONObject.parseObject(request.execute().body());
            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("access_token", tokenInfo.getString("access_token"));
            updateWrapper.set("refresh_token", tokenInfo.getString("refresh_token"));
            updateWrapper.eq("id", account.getId());
            account.setRefreshToken(tokenInfo.getString("refresh_token"));
            account.setAccessToken(tokenInfo.getString("access_token"));
            accountMapper.update(new Account(), updateWrapper);
            return StrUtil.isNotBlank(tokenInfo.getString("access_token"));
        }
        return true;
    }


    /**
     * 获取一个角色的isk余额
     *
     * @param account 角色
     * @return 余额
     */
    public Long getIskBalance(Account account) {
        return Convert.toLong(esiClient.charactersWallet(account), 0L);
    }

    /**
     * 获取一个角色的技能点总数
     *
     * @param account 角色
     * @return 技能点总数
     */
    public Integer getTotalSp(Account account) {
        JSONObject info = JSONObject.parseObject(esiClient.charactersSkills(account));
        return info.getIntValue("total_sp");
    }

    /**
     * 获取一个角色的已学习技能
     *
     * @param account 角色
     * @return 已学习技能
     */
    public JSONArray getSkill(Account account) {
        JSONObject info = JSONObject.parseObject(esiClient.charactersSkills(account));
        return info.getJSONArray("skills");
    }

    /**
     * 获取指定id列表的中文
     *
     * @param ids id列表
     * @return 名称列表
     */
    public JSONArray universeNames(List<Integer> ids) {
        if (ids.size() > 1000) {
            int totalPage = PageUtil.totalPage(ids.size(), 1000);
            JSONArray result = new JSONArray();
            for (int i = 0; i < totalPage; i++) {
                List<Integer> tempIds = ListUtil.page(i, 1000, ids);
                result.addAll(esiClient.universeNames(new HashSet<>(tempIds)));
            }
            return result;
        } else {
            return esiClient.universeNames(new HashSet<>(ids));
        }

    }

    /**
     * 获取指定名称列表的id
     *
     * @param names 名称列表
     * @return id列表
     */
    public JSONObject universeIds(List<String> names) {
        return esiClient.universeIds(new HashSet<>(names));
    }

    /**
     * 获取用户技能队列
     *
     * @param userAccount 用户
     * @return 技能队列
     */
    public JSONArray charactersSkillQueue(Account userAccount) {
        return esiClient.charactersSkillQueue(userAccount);
    }

    /**
     * 获取一个位置名称，自动区分玩家或npc建筑
     *
     * @param locationId 位置id
     * @return 名称
     */
    public String getLocationName(Account userAccount, long locationId) {
        if (redis.hasKey(CacheKey.CACHE_EVE_CHARACTERS_NAME_ID + locationId) && redis.get(CacheKey.CACHE_EVE_CHARACTERS_NAME_ID + locationId) != null) {
            return Convert.toStr(redis.get(CacheKey.CACHE_EVE_CHARACTERS_NAME_ID + locationId), "未知");
        }
        if (locationId > Integer.MAX_VALUE) {
            // 玩家建筑
            return universeStructures(userAccount, locationId);
        } else {
            // NPC建筑
            return universeStations(Convert.toInt(locationId));
        }

    }

    /**
     * 获取一个角色id的名称
     *
     * @param id 角色id
     * @return 名称
     */
    public String charactersName(Integer id) {
        if (redis.hasKey(CacheKey.CACHE_EVE_CHARACTERS_NAME_ID + id) && redis.get(CacheKey.CACHE_EVE_CHARACTERS_NAME_ID + id) != null) {
            return Convert.toStr(redis.get(CacheKey.CACHE_EVE_CHARACTERS_NAME_ID + id), "未知");
        }
        String name = JSONObject.parseObject(esiClient.charactersName(id)).getString("name");
        redis.set(CacheKey.CACHE_EVE_CHARACTERS_NAME_ID + id, name);
        return name;
    }

    /**
     * 获取一个npc空间站的信息
     *
     * @param id 空间站id
     * @return 空间站信息
     */
    public String universeStations(Integer id) {
        if (redis.hasKey(CacheKey.CACHE_EVE_STATION_NAME_ID + id) && redis.get(CacheKey.CACHE_EVE_STATION_NAME_ID + id) != null) {
            return Convert.toStr(redis.get(CacheKey.CACHE_EVE_STATION_NAME_ID + id), "未知");
        }
        String name = JSONObject.parseObject(esiClient.universeStations(id)).getString("name");
        redis.set(CacheKey.CACHE_EVE_STATION_NAME_ID + id, name);
        return name;
    }

    /**
     * 获取一个区域的名称
     *
     * @param id 区域id
     * @return 区域名称
     */
    public String universeRegions(Integer id) {
        if (redis.hasKey(CacheKey.CACHE_EVE_REGION_NAME_ID + id) && redis.get(CacheKey.CACHE_EVE_REGION_NAME_ID + id) != null) {
            return Convert.toStr(redis.get(CacheKey.CACHE_EVE_REGION_NAME_ID + id), "未知");
        }
        String name = esiClient.universeRegions(id).getString("name");
        redis.set(CacheKey.CACHE_EVE_REGION_NAME_ID + id, name);
        return name;
    }


    /**
     * 获取一个玩家建筑的信息
     *
     * @param id 玩家建筑id
     * @return 玩家建筑信息
     */
    public String universeStructures(Account userAccount, long id) {
        if (redis.hasKey(CacheKey.CACHE_EVE_STRUCTURES_NAME_ID + id) && redis.get(CacheKey.CACHE_EVE_STRUCTURES_NAME_ID + id) != null) {
            return Convert.toStr(redis.get(CacheKey.CACHE_EVE_STRUCTURES_NAME_ID + id), "未知");
        }
        try {
            String name = JSONObject.parseObject(esiClient.universeStructures(userAccount, id)).getString("name");
            redis.set(CacheKey.CACHE_EVE_STRUCTURES_NAME_ID + id, name);
            return name;
        } catch (Exception e) {
            log.warn("建筑名称无法读取，原因:", e);
            redis.set(CacheKey.CACHE_EVE_STRUCTURES_NAME_ID + id, "无法读取");
            return "无法读取";
        }

    }

    /**
     * 获取一个星系id的名称
     *
     * @param id 星系id
     * @return 名称
     */
    public String universeSystems(Integer id) {
        if (redis.hasKey(CacheKey.CACHE_EVE_SYSTEM_NAME_ID + id) && redis.get(CacheKey.CACHE_EVE_SYSTEM_NAME_ID + id) != null) {
            return Convert.toStr(redis.get(CacheKey.CACHE_EVE_SYSTEM_NAME_ID + id), "未知");
        }
        String name = JSONObject.parseObject(esiClient.universeSystems(id)).getString("name");
        redis.set(CacheKey.CACHE_EVE_SYSTEM_NAME_ID + id, name);
        return name;
    }


    /**
     * 获取一个角色的详细信息
     *
     * @param account 角色
     * @return 角色详细信息
     */
    public EsiAccountInfo getAccountInfo(Account account) {
        HttpRequest request = HttpUtil.createGet("https://esi.evetech.net/latest/characters/" + account.getCharacterId());
        request.header("Authorization", "Bearer " + account.getAccessToken());
        HttpResponse response = request.execute();
        for (int i = 0; i < 5; i++) {
            if (response.isOk()) {
                break;
            }
            response = request.execute();
        }
        JSONObject info = JSONObject.parseObject(response.body());

        EsiAccountInfo accountInfo = new EsiAccountInfo();

        accountInfo.setAllianceId(info.getLong("alliance_id"));
        accountInfo.setCorpId(info.getLong("corporation_id"));

        request = HttpUtil.createGet("https://esi.evetech.net/latest/corporations/" + accountInfo.getCorpId());
        request.header("Authorization", "Bearer " + account.getAccessToken());
        info = JSONObject.parseObject(request.execute().body());
        accountInfo.setCorpName(StrUtil.isBlank(info.getString("name")) ? "暂无军团" : info.getString("name"));

        request = HttpUtil.createGet("https://esi.evetech.net/latest/alliances/" + accountInfo.getAllianceId());
        request.header("Authorization", "Bearer " + account.getAccessToken());
        info = JSONObject.parseObject(request.execute().body());
        accountInfo.setAllianceName(StrUtil.isBlank(info.getString("name")) ? "暂无联盟" : info.getString("name"));

        return accountInfo;
    }

    /**
     * 查询军团钱包流水
     *
     * @param account 总监账号
     * @param page    页面
     * @return 钱包流水
     */
    public JSONArray corporationsWalletsJournal(Account account, int division, int page) {
        ForestResponse<JSONArray> response = esiClient.corporationsWalletsJournal(account, Convert.toInt(config.getVal(CacheKey.Config.EVE.EVE_MAIN_CORP)), division, page);
        JSONArray result = new JSONArray();
        result.addAll(response.getResult());
        if (Convert.toInt(response.getHeader("x-pages").getValue()) > page) {
            result.addAll(corporationsWalletsJournal(account, division, page + 1));
        }
        return result;
    }

    /**
     * 获取个人的采矿明细
     *
     * @param account 角色
     * @param page    页码
     * @return 采矿明细
     */
    public JSONArray charactersMining(Account account, int page) {
        ForestResponse<JSONArray> response = esiClient.charactersMining(account, page);
        JSONArray result = new JSONArray();
        result.addAll(response.getResult());
        if (Convert.toInt(response.getHeader("x-pages").getValue()) > page) {
            result.addAll(charactersMining(account, page + 1));
        }
        return result;
    }

    /**
     * 查询玩家建筑订单
     *
     * @param account 角色
     * @param id      建筑id
     * @param page    页码
     * @return 数据
     */
    public JSONArray marketsStructures(Account account, Long id, int page) {
        ForestResponse<JSONArray> response = esiClient.marketsStructures(account, id, page);
        JSONArray result = new JSONArray();
        result.addAll(response.getResult());
        if (Convert.toInt(response.getHeader("x-pages").getValue()) > page) {
            result.addAll(marketsStructures(account, id, page + 1));
        }
        return result;
    }

    /**
     * 查询NPC建筑订单
     *
     * @param id   建筑id
     * @param page 页码
     * @return 数据
     */
    public JSONArray marketsRegion(Integer id, int page) {
        ForestResponse<JSONArray> response = esiClient.marketsRegion(id, page);
        JSONArray result = new JSONArray();
        result.addAll(response.getResult());
        if (Convert.toInt(response.getHeader("x-pages").getValue()) > page) {
            result.addAll(marketsRegion(id, page + 1));
        }
        return result;
    }

    /**
     * 获取用户军团的合同信息
     *
     * @param account 用户
     * @return 合同列表
     */
    public JSONArray corporationsContracts(Account account, int page) {
        ForestResponse<JSONArray> response = esiClient.corporationsContracts(account, page);
        JSONArray result = new JSONArray();
        result.addAll(response.getResult());
        if (Convert.toInt(response.getHeader("x-pages").getValue()) > page) {
            result.addAll(corporationsContracts(account, page + 1));
        }
        return result;
    }

    /**
     * 获取用户钱包流水
     *
     * @param account 用户
     * @param page    页码
     * @return 钱包流水
     */
    public JSONArray charactersWalletJournal(Account account, int page) {
        ForestResponse<JSONArray> response = esiClient.charactersWalletJournal(account, page);
        JSONArray result = new JSONArray();
        result.addAll(response.getResult());
        if (Convert.toInt(response.getHeader("x-pages").getValue()) > page) {
            result.addAll(charactersWalletJournal(account, page + 1));
        }
        return result;
    }

    /**
     * 获取用户市场交易
     *
     * @param account 用户
     * @return 市场交易
     */
    public JSONArray charactersWalletTransactions(Account account) {
        return esiClient.charactersWalletTransactions(account);
    }


    /**
     * 查询一个用户的订单历史
     *
     * @param account 用户
     * @param page    订单历史
     * @return 订单历史
     */
    public JSONArray charactersOrdersHistory(Account account, int page) {
        ForestResponse<JSONArray> response = esiClient.charactersOrdersHistory(account, page);
        JSONArray result = new JSONArray();
        result.addAll(response.getResult());
        if (Convert.toInt(response.getHeader("x-pages").getValue()) > page) {
            result.addAll(charactersOrdersHistory(account, page + 1));
        }
        return result;
    }

    /**
     * 获取军团建筑列表
     *
     * @param account 总监或有权限的账号
     * @param page    页码
     * @return 标准返回
     */
    public JSONArray corporationsStructures(Account account, int page) {
        ForestResponse<JSONArray> response = esiClient.corporationsStructures(account, page);
        JSONArray result = new JSONArray();
        result.addAll(response.getResult());
        if (Convert.toInt(response.getHeader("x-pages").getValue()) > page) {
            result.addAll(corporationsStructures(account, page + 1));
        }
        return result;
    }

    /**
     * 获取军团月矿牵引情况
     *
     * @param account 总监或有权限的账号
     * @param page    页码
     * @return 标准返回
     */
    public JSONArray corporationsExtractions(Account account, int page) {
        ForestResponse<JSONArray> response = esiClient.corporationsExtractions(account, page);
        JSONArray result = new JSONArray();
        result.addAll(response.getResult());
        if (Convert.toInt(response.getHeader("x-pages").getValue()) > page) {
            result.addAll(corporationsExtractions(account, page + 1));
        }
        return result;
    }

    /**
     * 获取角色加入指定团的时间
     *
     * @param userAccount 角色
     * @param corpId      军团ID
     * @return
     */
    public Timestamp getJoinTime(Account userAccount, Integer corpId) {

        String ret = esiClient.charactersCorporationHistory(userAccount.getCharacterId());
        if (!JSONUtil.isJsonArray(ret)) {
            return null;
        }

        JSONArray history = JSONArray.parseArray(ret);

        // 判断最新记录是否是需要校验的军团
        if (history.getJSONObject(0).getIntValue("corporation_id") == corpId) {
            return history.getJSONObject(0).getTimestamp("start_date");
        }
        return null;
    }

    /**
     * 获取个人物资id列表
     *
     * @param account 用户
     * @param page    页面
     * @return 个人物资
     */
    public JSONArray assets(Account account, int page) {

        if (StrUtil.isBlank(account.getAccessToken())) {
            return new JSONArray();
        }
        refreshToken(account);

        HttpRequest request = HttpUtil.createGet("https://esi.evetech.net/latest/characters/" + account.getCharacterId() + "/assets/?datasource=tranquility&page=" + page);
        request.header("Authorization", "Bearer " + account.getAccessToken());
        HttpResponse response = request.execute();
        String jsonStr = response.body();

        if (StrUtil.isBlank(jsonStr) || !JSONUtil.isJsonArray(jsonStr)) {
            return new JSONArray();
        }

        JSONArray result = JSONObject.parseArray(jsonStr);
        if (Convert.toInt(response.header("x-pages")) > page) {
            result.addAll(assets(account, page + 1));
        }

        return result;
    }

    /**
     * 根据资产id获取资产的名字
     *
     * @param ids id列表
     * @return 名称列表
     */
    public JSONArray assetsNames(Account account, List<Long> ids) {
        refreshToken(account);
        HttpRequest request = HttpUtil.createPost("https://esi.evetech.net/latest/characters/" + account.getCharacterId() + "/assets/names/?datasource=tranquility");
        request.header("Authorization", "Bearer " + account.getAccessToken());
        request.body(JSONObject.toJSONString(new HashSet<>(ids)));
        return JSONArray.parseArray(request.execute().body());
    }

    public JSONArray assetsLocations(Account account, List<Long> ids) {
        refreshToken(account);
        HttpRequest request = HttpUtil.createPost("https://esi.evetech.net/latest/characters/" + account.getCharacterId() + "/assets/locations/?datasource=tranquility");
        request.header("Authorization", "Bearer " + account.getAccessToken());
        request.body(JSONObject.toJSONString(new HashSet<>(ids)));
        return JSONArray.parseArray(request.execute().body());
    }

    /**
     * TODO 这个需要自己维护
     * 获取吉他最低销售价
     *
     * @param id 物品id
     * @return 价格
     */
    public long getMinSellPrice(Integer id) {
        try {
            return JSONObject.parseObject(HttpUtil.get("https://www.ceve-market.org/tqapi/market/region/10000002/system/30000142/type/" + id + ".json")).getJSONObject("sell").getLongValue("min");
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * TODO 这个需要自己维护
     * 获取吉他最高收购
     *
     * @param id 物品id
     * @return 价格
     */
    public long getMaxBuyPrice(Integer id) {
        return JSONObject.parseObject(HttpUtil.get("https://www.ceve-market.org/tqapi/market/region/10000002/system/30000142/type/" + id + ".json")).getJSONObject("buy").getLongValue("max");
    }
}
