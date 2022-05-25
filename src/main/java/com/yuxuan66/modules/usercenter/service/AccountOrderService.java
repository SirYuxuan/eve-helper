package com.yuxuan66.modules.usercenter.service;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.common.esi.EsiApi;
import com.yuxuan66.common.esi.http.EsiClient;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.modules.account.entity.Account;
import com.yuxuan66.modules.account.entity.AccountOrder;
import com.yuxuan66.modules.account.mapper.AccountMapper;
import com.yuxuan66.modules.account.mapper.AccountOrderMapper;
import com.yuxuan66.modules.usercenter.entity.query.AccountOrderQuery;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sir丶雨轩
 * @since 2022/5/24
 */
@Service
@RequiredArgsConstructor
public class AccountOrderService {

    @Resource
    private AccountOrderMapper accountOrderMapper;
    @Resource
    private AccountMapper accountMapper;
    private final EsiApi esi;
    private final EsiClient esiClient;
    /**
     * 缓存玩家所查询的区域/建筑订单，1小时失效
     */
    private final TimedCache<Long,JSONArray> marketsStructuresMap = new TimedCache<>(1000 * 60 * 60 );


    /**
     * 查询一个角色当前的订单状态
     *
     * @param accountId 角色
     * @return 标准返回
     */
    public RespEntity marketMargin(Long accountId) {

        List<Map<String, Object>> result = new ArrayList<>();

        result.addAll(accountOrderMapper.getBuyOrderSum(accountId));
        result.addAll(accountOrderMapper.getSellOrderSum(accountId));

        return RespEntity.success(result);

    }


    /**
     * 分页查询角色订单记录
     *
     * @param accountOrderQuery 查询分页条件
     * @return 标准返回
     */
    public PageEntity list(AccountOrderQuery accountOrderQuery) {
        accountOrderQuery.processingSort();
        accountOrderQuery.processingCreateTime("issued");
        accountOrderQuery.processingBlurry("region_name", "location_name", "type_name");
        QueryWrapper<AccountOrder> queryWrapper = accountOrderQuery.getQueryWrapper();
        queryWrapper.eq(accountOrderQuery.getIsBuyOrder() != null, "is_buy_order", accountOrderQuery.getIsBuyOrder());
        queryWrapper.eq(accountOrderQuery.getAccountId() != null, "account_id", accountOrderQuery.getAccountId());
        queryWrapper.eq(accountOrderQuery.getAccountId() == null, "user_id", TokenUtil.getUserId());
        if (accountOrderQuery.getMin() != null) {
            if (accountOrderQuery.getMin()) {
                queryWrapper.eq("min_price", 0).or().isNull("min_price");
                queryWrapper.isNotNull("jita_price");
            } else {
                queryWrapper.ne("min_price", 0);
                queryWrapper.isNotNull("min_price");
            }
        }
        return PageEntity.success(accountOrderMapper.selectPage(accountOrderQuery.getPage(), queryWrapper));
    }

    /**
     * 对当前登录用户的所有角色订单进行价格建议
     *
     * @return 标准返回
     */
    public RespEntity priceProposal(boolean allJita) {


        Map<Long, Account> accountMap = new HashMap<>();

        List<AccountOrder> accountOrders = accountOrderMapper.selectList(new QueryWrapper<AccountOrder>().eq("user_id", TokenUtil.getUserId()));
        for (AccountOrder accountOrder : accountOrders) {
            if (accountOrder.getIsBuyOrder() != null && accountOrder.getIsBuyOrder()) {
                accountOrder.setJitaPrice(BigDecimal.valueOf(esi.getMaxBuyPrice(accountOrder.getTypeId())));
            } else {
                accountOrder.setJitaPrice(BigDecimal.valueOf(esi.getMinSellPrice(accountOrder.getTypeId())));
            }

            // 计算建筑最低价格
            Account account = accountMap.get(accountOrder.getAccountId());
            if (account == null) {
                account = accountMapper.selectById(accountOrder.getAccountId());
                accountMap.put(account.getId(), account);
            }
            JSONArray data = marketsStructuresMap.get(accountOrder.getLocationId());
            if (data == null) {

                if (accountOrder.getLocationId() > Integer.MAX_VALUE) {
                    data = esi.marketsStructures(account, accountOrder.getLocationId(), 1);
                }else if(!allJita){
                    data = esi.marketsRegion(Convert.toInt(accountOrder.getRegionId()),1);
                    data = data.stream().filter(item-> ((JSONObject)item).getInteger("location_id").equals(Convert.toInt(accountOrder.getLocationId()))).collect(Collectors.toCollection(JSONArray::new));
                }

                marketsStructuresMap.put(accountOrder.getLocationId(), data);
            }
            BigDecimal minPrice = accountOrder.getPrice();
            if(data == null){
                if (accountOrder.getIsBuyOrder() != null && accountOrder.getIsBuyOrder()) {
                    BigDecimal jitaMaxBuy = Convert.toBigDecimal(Convert.toStr(esi.getMaxBuyPrice(accountOrder.getTypeId())));
                    if (jitaMaxBuy.compareTo(minPrice) > 0) {
                        minPrice = jitaMaxBuy;
                    }
                } else {
                    BigDecimal jitaMinSell = Convert.toBigDecimal(Convert.toStr(esi.getMinSellPrice(accountOrder.getTypeId())));
                    if (jitaMinSell.compareTo(minPrice) < 0) {
                        minPrice = jitaMinSell;
                    }
                }
            }else{
                for (Object datum : data) {
                    JSONObject obj = (JSONObject) datum;
                    if (obj.getIntValue("type_id") != accountOrder.getTypeId()) {
                        continue;
                    }
                    if (accountOrder.getIsBuyOrder() == null) {
                        accountOrder.setIsBuyOrder(false);
                    }
                    if (accountOrder.getIsBuyOrder() != obj.getBooleanValue("is_buy_order")) {
                        continue;
                    }
                    if (accountOrder.getIsBuyOrder()) {
                        if (obj.getBigDecimal("price").compareTo(minPrice) > 0) {
                            minPrice = obj.getBigDecimal("price");
                        }
                    } else {
                        if (obj.getBigDecimal("price").compareTo(minPrice) < 0) {
                            minPrice = obj.getBigDecimal("price");
                        }
                    }
                }
            }

            if (minPrice.compareTo(accountOrder.getPrice()) != 0) {
                accountOrder.setMinPrice(minPrice);
            } else {
                accountOrder.setMinPrice(BigDecimal.ZERO);
            }
            accountOrderMapper.updateById(accountOrder);

        }

        return RespEntity.success();
    }

    /**
     * 打开游戏窗口
     * @param orderId 订单id
     * @return 标准返回
     */
    public RespEntity openWindow(Long orderId){
        AccountOrder accountOrder = accountOrderMapper.selectById(orderId);
        esiClient.uiOpenWindowMarketDetails(accountMapper.selectById(accountOrder.getAccountId()),accountOrder.getTypeId());
        return RespEntity.success();
    }
}
