package com.yuxuan66.modules.industry.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.cache.modules.EveCache;
import com.yuxuan66.modules.database.entity.BluePrint;
import com.yuxuan66.modules.database.entity.BluePrintProducts;
import com.yuxuan66.modules.database.entity.MarketGroup;
import com.yuxuan66.modules.database.entity.Type;
import com.yuxuan66.modules.database.mapper.MarketGroupMapper;
import com.yuxuan66.modules.database.mapper.TypeMapper;
import com.yuxuan66.modules.industry.entity.dto.TypeInfo;
import com.yuxuan66.modules.industry.entity.query.MarketGroupQuery;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Sir丶雨轩
 * @since 6/14/2022
 */
@Service
@RequiredArgsConstructor
public class AnalysisService {

    @Resource
    private MarketGroupMapper marketGroupMapper;
    @Resource
    private TypeMapper typeMapper;
    private final EveCache cache;

    /**
     * 查询市场分组
     *
     * @param marketGroupQuery 查询条件
     * @return 市场分组
     */
    public RespEntity queryMarketGroup(MarketGroupQuery marketGroupQuery) {

        if (marketGroupQuery.getHasType()) {
            // 这里要查询具体的商品
            return RespEntity.success(typeMapper.selectList(new QueryWrapper<Type>().eq("market_group_id", marketGroupQuery.getPid())));
        }

        marketGroupQuery.processingBlurry("name", "name_en");
        QueryWrapper<MarketGroup> queryWrapper = marketGroupQuery.getQueryWrapper();

        if (marketGroupQuery.getPid() != null && marketGroupQuery.getPid() == 0) {
            queryWrapper.isNull("pid");
        } else {
            queryWrapper.eq(marketGroupQuery.getPid() != null, "pid", marketGroupQuery.getPid());
        }

        List<MarketGroup> groupList = marketGroupMapper.selectList(queryWrapper);
        for (MarketGroup marketGroup : groupList) {
            marketGroup.setIsLeaf(false);
        }

        return RespEntity.success(groupList);
    }


    /**
     * 获取一个物品的详细信息
     *
     * @param id 物品id
     * @return 标准返回
     */
    public RespEntity getTypeInfo(Integer id) {
        Type type = typeMapper.selectById(id);
        TypeInfo typeInfo = new TypeInfo();
        typeInfo.setType(type);

        // 循环获取市场分组
        List<MarketGroup> marketGroupList = cache.getMarketGroupList();
        List<String> groupNameList = new ArrayList<>();
        Optional<MarketGroup> marketGroupOptional = marketGroupList.stream().filter(item -> item.getId().equals(type.getMarketGroupId())).findFirst();
        while (marketGroupOptional.isPresent()) {
            groupNameList.add(marketGroupOptional.get().getName());
            Optional<MarketGroup> finalMarketGroupOptional = marketGroupOptional;
            marketGroupOptional = marketGroupList.stream().filter(item -> item.getId().equals(finalMarketGroupOptional.get().getPid())).findFirst();
        }
        Collections.reverse(groupNameList);
        typeInfo.setChainGrouping(String.join("-", groupNameList));
        BluePrint bluePrint = getBluePrint(type);
        typeInfo. setIsMake(bluePrint != null);
        if (bluePrint != null) {
            typeInfo.setMaxLimit(bluePrint.getMaxLimit());
        }


        return RespEntity.success(typeInfo);
    }


    /**
     * 判断某样物品是否可以制造
     *
     * @param type 物品
     * @return 是否可以制造
     */
    private BluePrint getBluePrint(Type type) {
        // 1. 自身可以在蓝图列表里面找到
        Optional<BluePrint> optionalType = cache.getBluePrintList().stream().filter(item -> item.getId().equals(type.getId())).findFirst();
        if (optionalType.isPresent()) {
            return optionalType.get();
        }
        Optional<BluePrintProducts> optionalBluePrintProducts = cache.getBluePrintProductsList().stream().filter(item -> item.getProductsId().equals(type.getId())).findFirst();
        if (optionalBluePrintProducts.isPresent()) {
            int bluePrintId = optionalBluePrintProducts.get().getBluePrintId();
            optionalType = cache.getBluePrintList().stream().filter(item -> item.getId().equals(bluePrintId)).findFirst();
            return optionalType.orElse(null);
        }
        return null;
    }
}
