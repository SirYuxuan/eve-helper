package com.yuxuan66.cache.modules;

import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.cache.RedisUtil;
import com.yuxuan66.modules.database.entity.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sir丶雨轩
 * @since 2021/12/15
 */
@Component
public class EveCache {

    private final RedisUtil redis;

    private List<Type> typeList;

    public EveCache(RedisUtil redis) {
        this.redis = redis;
        this.typeList = getType();
    }


    /**
     * 重新设置eve type对照表缓存
     *
     * @param typeList type对照表
     */
    public void resetType(List<Type> typeList) {
        redis.del(CacheKey.EVE_SDE_TYPE_ID);
        redis.lSet(CacheKey.EVE_SDE_TYPE_ID, typeList, Type.class);
    }

    /**
     * 获取eve type列表
     *
     * @return 物品列表
     */
    public List<Type> getType() {
        if (redis.hasKey(CacheKey.EVE_SDE_TYPE_ID)) {
            long size = redis.lGetListSize(CacheKey.EVE_SDE_TYPE_ID);
            return redis.lGet(CacheKey.EVE_SDE_TYPE_ID, 0, size, Type.class);
        }
        return new ArrayList<>();
    }


    /**
     * 获取eve type的map
     *
     * @return 物品数据
     */
    public Map<Integer, Type> getTypeMap() {
        Map<Integer, Type> result = new HashMap<>();
        for (Type type : this.typeList) {
            result.put(type.getId(), type);
        }
        return result;
    }



    /**
     * 重新设置eve 分类缓存
     *
     * @param categoryList 分类
     */
    public void resetCategory(List<Category> categoryList) {
        redis.del(CacheKey.EVE_SDE_CATEGORY_ID);
        redis.lSet(CacheKey.EVE_SDE_CATEGORY_ID, categoryList, Category.class);
    }

    /**
     * 重新设置eve分组缓存
     *
     * @param categoryList 分类
     */
    public void resetGroup(List<Group> categoryList) {
        redis.del(CacheKey.EVE_SDE_GROUP_ID);
        redis.lSet(CacheKey.EVE_SDE_GROUP_ID, categoryList, Group.class);
    }

    /**
     * 重新设置eve元分组缓存
     *
     * @param categoryList 分类
     */
    public void resetMetaGroup(List<MetaGroup> categoryList) {
        redis.del(CacheKey.EVE_SDE_META_GROUP_ID);
        redis.lSet(CacheKey.EVE_SDE_META_GROUP_ID, categoryList, MetaGroup.class);
    }

    /**
     * 重新设置eve市场分组缓存
     *
     * @param categoryList 分类
     */
    public void resetMarketGroup(List<MarketGroup> categoryList) {
        redis.del(CacheKey.EVE_SDE_MARKET_GROUP_ID);
        redis.lSet(CacheKey.EVE_SDE_MARKET_GROUP_ID, categoryList, MarketGroup.class);
    }

    /**
     * 递归获取所有子菜单
     *
     * @param marketGroupList 菜单列表
     * @param marketGroupSet  菜单集合
     */
    private void getChildMarketGroup(List<MarketGroup> marketGroupList, Set<MarketGroup> marketGroupSet) {
        for (MarketGroup marketGroup : marketGroupList) {
            marketGroupSet.add(marketGroup);
            if (marketGroup.getHasType()) {
                continue;
            }
            List<MarketGroup> marketGroups = getMarketGroup().stream().filter(item -> item.getPid() != null && marketGroup.getId().equals(item.getPid())).collect(Collectors.toList());
            if (marketGroups.size() != 0) {
                getChildMarketGroup(marketGroups, marketGroupSet);
            }
        }
    }


    /**
     * 获取自身及子集的id
     *
     * @param id 目录id
     * @return id列表
     */
    private Set<Integer> getMarketGroupIdByPid(int id) {
        MarketGroup marketGroup = getMarketGroup().stream().filter(item -> item.getId().equals(id)).findFirst().get();
        if (marketGroup.getHasType()) {
            Set<Integer> result = new HashSet<>();
            result.add(id);
            return result;
        }
        Set<MarketGroup> marketGroupSet = new HashSet<>();
        List<MarketGroup> menuList = getMarketGroup().stream().filter(item -> item.getPid() != null && item.getPid() == id).collect(Collectors.toList());
        marketGroupSet.add(marketGroup);
        getChildMarketGroup(menuList, marketGroupSet);
        return marketGroupSet.stream().map(MarketGroup::getId).collect(Collectors.toSet());
    }

    /**
     * 获取eve市场分组缓存
     *
     * @return eve市场分组缓存
     */
    public List<MarketGroup> getMarketGroup() {
        if (redis.hasKey(CacheKey.EVE_SDE_MARKET_GROUP_ID)) {
            long size = redis.lGetListSize(CacheKey.EVE_SDE_MARKET_GROUP_ID);
            return redis.lGet(CacheKey.EVE_SDE_MARKET_GROUP_ID, 0, size, MarketGroup.class);
        }
        return new ArrayList<>();
    }

    /**
     * 重新设置eve 蓝图数据
     *
     * @param bluePrintList 蓝图数据
     */
    public void resetBluePrint(List<BluePrint> bluePrintList) {
        redis.del(CacheKey.EVE_SDE_BLUE_PRINT);
        redis.lSet(CacheKey.EVE_SDE_BLUE_PRINT, bluePrintList, BluePrint.class);
    }




}
