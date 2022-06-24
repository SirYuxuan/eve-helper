package com.yuxuan66.modules.market.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.cache.modules.EveCache;
import com.yuxuan66.common.esi.EsiApi;
import com.yuxuan66.common.esi.http.EsiClient;
import com.yuxuan66.modules.account.entity.Account;
import com.yuxuan66.modules.account.mapper.AccountMapper;
import com.yuxuan66.modules.database.entity.Type;
import com.yuxuan66.modules.market.entity.Market;
import com.yuxuan66.modules.market.entity.MarketStructures;
import com.yuxuan66.modules.market.entity.query.MarketQuery;
import com.yuxuan66.modules.market.mapper.MarketMapper;
import com.yuxuan66.support.basic.model.PageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 处理市场相关业务
 *
 * @author Sir丶雨轩
 * @since 6/23/2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketService {

    private final EsiApi esi;
    private final EsiClient esiClient;
    private final EveCache eveCache;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private MarketMapper marketMapper;

    /**
     * 分页查询市场数据
     *
     * @param marketQuery 查询条件
     * @return 市场数据
     */
    public PageEntity list(MarketQuery marketQuery) {
        marketQuery.processingSort();
        marketQuery.processingBlurry("type_name");
        QueryWrapper<Market> queryWrapper = marketQuery.getQueryWrapper();

        if (marketQuery.getMeta() != null) {
            if (marketQuery.getMeta().size() == 1 && "全部数据".equals(marketQuery.getMeta().get(0))) {

            } else {

                queryWrapper.and(marketQueryWrapper -> {
                    for (String s : marketQuery.getMeta()) {
                        marketQueryWrapper.eq("meta", s);
                        marketQueryWrapper.or();
                    }
                });

            }
        }

        if (marketQuery.getMarketGroupId() != null) {
            queryWrapper.in("market_group_id", eveCache.getMarketGroupIdByPidCache(marketQuery.getMarketGroupId()));
        }
        return PageEntity.success(marketMapper.selectPage(marketQuery.getPage(), queryWrapper));
    }

    //@PostConstruct
    public void save4HMarket() {
        Account account = accountMapper.selectById(59);
        esi.refreshToken(account);

        JSONArray array = esi.marketsStructures(account, 1035466617946L, 1);

        Map<Integer, Market> marketMap = new HashMap<>();

        Map<Integer, Type> typeMap = eveCache.getTypeMap();

        TimeInterval interval = DateUtil.timer();

        BigDecimal oneHundred = new BigDecimal("100");
        for (int i = 0; i < array.size(); i++) {

            try {
                MarketStructures marketStructures = array.getJSONObject(i).toJavaObject(MarketStructures.class);
                if (marketMap.containsKey(marketStructures.getTypeId())) {
                    Market market = marketMap.get(marketStructures.getTypeId());
                    if (marketStructures.isBuyOrder()) {
                        // 处理购买订单
                        if (marketStructures.getPrice() > market.getBuyPrice()) {
                            market.setBuyPrice(marketStructures.getPrice());
                        }
                        market.setBuyNum(market.getBuyNum() + marketStructures.getVolumeRemain());
                    } else {
                        // 处理出售订单
                        if (market.getSellPrice() == 0 || marketStructures.getPrice() < market.getSellPrice()) {
                            market.setSellPrice(marketStructures.getPrice());
                        }
                        market.setSellNum(market.getSellNum() + marketStructures.getVolumeRemain());
                    }
                    if (market.getJitaSellPrice() == 0) {
                        market.setJita(oneHundred);
                    } else {
                        market.setJita(new BigDecimal(Convert.toStr(Convert.toDouble(market.getSellPrice()) / market.getJitaSellPrice() * 100)));
                    }
                    marketMap.put(marketStructures.getTypeId(), market);
                } else {
                    Market market = new Market();
                    market.setTypeId(marketStructures.getTypeId());
                    if (typeMap.containsKey(market.getTypeId())) {
                        Type type = typeMap.get(market.getTypeId());
                        market.setTypeName(type.getName());
                        market.setMarketGroupId(type.getMarketGroupId());
                        market.setMeta(type.getMetaGroupName());
                    }
                    if (marketStructures.isBuyOrder()) {
                        market.setBuyPrice(marketStructures.getPrice());
                        market.setBuyNum(marketStructures.getVolumeRemain());
                    } else {
                        market.setSellPrice(marketStructures.getPrice());
                        market.setSellNum(marketStructures.getVolumeRemain());
                    }
                    market.setJitaSellPrice(esi.getMinSellPrice(market.getTypeId()));
                    market.setJitaBuyPrice(esi.getMaxBuyPrice(market.getTypeId()));

                    // 查询区域内 本月交易量
                    JSONArray history = esiClient.marketsHistory(market.getTypeId());
                    for (int j = history.size() - 1; j >= 0; j--) {
                        JSONObject data = history.getJSONObject(j);
                        long time = DateUtil.parse(data.getString("date")).setTimeZone(TimeZone.getDefault()).getTime();
                        long begin = DateUtil.beginOfMonth(new Date()).getTime();
                        if (time > begin) {
                            // 本月数据
                            // 本月总成交量
                            market.setOrder(market.getOrder() + data.getInteger("volume"));
                            if (data.getLongValue("highest") > market.getHighest()) {
                                market.setHighest(data.getLongValue("highest"));
                            }
                            if (market.getLowest() == 0 || data.getLongValue("lowest") < market.getLowest()) {
                                market.setLowest(data.getLongValue("lowest"));
                            }
                        }
                    }
                    if (market.getJitaSellPrice() == 0) {
                        market.setJita(oneHundred);
                    } else {
                        market.setJita(new BigDecimal(Convert.toStr(Convert.toDouble(market.getSellPrice()) / market.getJitaSellPrice() * 100)));
                    }
                    marketMap.put(marketStructures.getTypeId(), market);
                }

                long time = interval.interval() / (i + 1);

                log.info("处理完成第：" + i + "条数据,完成百分比：" + String.format("%.2f", (i / array.size() * 100.0)) + "%,当前耗时：" + interval.intervalPretty() + ",预计剩余时间：" + DateUtil.formatBetween(time * (array.size() - i)));
            } catch (Exception e) {

            }
        }
        List<Market> marketList = new ArrayList<>();
        marketMap.forEach((k, v) -> marketList.add(v));
        marketMapper.delete(null);
        marketMapper.batchInsert(marketList);

    }

}
