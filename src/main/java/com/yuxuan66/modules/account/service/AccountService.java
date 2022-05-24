package com.yuxuan66.modules.account.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.UnicodeUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.cache.modules.EveCache;
import com.yuxuan66.common.esi.EsiApi;
import com.yuxuan66.common.esi.entity.EsiAccountInfo;
import com.yuxuan66.common.esi.http.EsiClient;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.modules.account.entity.Account;
import com.yuxuan66.modules.account.entity.AccountOrder;
import com.yuxuan66.modules.account.entity.AccountOrderHistory;
import com.yuxuan66.modules.account.entity.AccountWalletTransactions;
import com.yuxuan66.modules.account.mapper.AccountMapper;
import com.yuxuan66.modules.account.mapper.AccountOrderHistoryMapper;
import com.yuxuan66.modules.account.mapper.AccountOrderMapper;
import com.yuxuan66.modules.account.mapper.AccountWalletTransactionsMapper;
import com.yuxuan66.modules.database.entity.Type;
import com.yuxuan66.support.basic.BasicQuery;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author Sir丶雨轩
 * @since 2022/5/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final EsiApi esi;
    private final EsiClient esiClient;
    private final EveCache cache;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private AccountOrderHistoryMapper accountOrderHistoryMapper;
    @Resource
    private AccountOrderMapper accountOrderMapper;
    @Resource
    private AccountWalletTransactionsMapper accountWalletTransactionsMapper;

    /**
     * 后台异步刷新用户基础数据
     *
     * @param account 用户
     */
    @Async("threadPoolTaskExecutor")
    public void refreshBaseInfo(Account account) {

        account.setIsk(esi.getIskBalance(account));
        account.setSkillNum(esi.getTotalSp(account));

        EsiAccountInfo accountInfo = esi.getAccountInfo(account);
        account.setCorpId(accountInfo.getCorpId());
        account.setCorpName(accountInfo.getCorpName());
        account.setAllianceId(accountInfo.getAllianceId());
        account.setAllianceName(accountInfo.getAllianceName());

        // 获取角色当前状态
        JSONObject charactersLocation = esiClient.charactersLocation(account);
        // 用户所在星系
        account.setSolarSystemName(esi.universeSystems(charactersLocation.getIntValue("solar_system_id")));
        account.setLocalName("太空中");
        // 用户所在空间站
        int stationId = charactersLocation.getIntValue("station_id");
        if (stationId > 0) {
            account.setLocalName(esi.universeStations(stationId));
        }
        // 用户所在玩家建筑
        long structureId = charactersLocation.getLongValue("structure_id");
        if (structureId > 0) {
            account.setLocalName(esi.universeStructures(account, structureId));
        }

        JSONObject charactersShip = esiClient.charactersShip(account);

        Map<Integer, Type> typeMap = cache.getTypeMap();

        account.setShipName(UnicodeUtil.toString(charactersShip.getString("ship_name").replace("u'", "").replace("'", "")));
        account.setShipTypeId(charactersShip.getIntValue("ship_type_id"));
        if (typeMap.containsKey(account.getShipTypeId())) {
            account.setShipTypeName(typeMap.get(account.getShipTypeId()).getName());
        }
        accountMapper.updateById(account);


    }

    /**
     * 异步刷新一个用户的历史订单
     *
     * @return 结果
     */
    @Async("threadPoolTaskExecutor")
    public Future<Boolean> refreshOrderHistory(Account account) {

        JSONArray orderArr = esi.charactersOrdersHistory(account, 1);

        Long lastId = accountOrderHistoryMapper.getLast(account.getId());
        if (lastId != null) {
            orderArr = orderArr.stream().filter(item -> ((JSONObject) item).getLongValue("id") > lastId).collect(Collectors.toCollection(JSONArray::new));
        }

        List<AccountOrderHistory> saveOrderHistory = new ArrayList<>();

        Map<Integer,Type> typeMap = cache.getTypeMap();

        for (Object o : orderArr) {
            JSONObject obj = (JSONObject) o;
            AccountOrderHistory accountOrderHistory = obj.toJavaObject(AccountOrderHistory.class);
            accountOrderHistory.setAccountId(account.getId());
            accountOrderHistory.setUserId(account.getUserId());
            accountOrderHistory.setCharacterId(account.getCharacterId());
            accountOrderHistory.setCharacterName(account.getCharacterName());
            accountOrderHistory.setId(obj.getLongValue("order_id"));
            accountOrderHistory.setIssued(DateUtil.parse(obj.getString("issued")).setTimeZone(TimeZone.getDefault()).toTimestamp());
            accountOrderHistory.setRegionName(esi.universeRegions(accountOrderHistory.getRegionId()));
            accountOrderHistory.setLocationName(esi.getLocationName(account, accountOrderHistory.getLocationId()));

            Type type = typeMap.get(accountOrderHistory.getTypeId());
            if(type != null){
                accountOrderHistory.setTypeName(type.getName());
            }



            saveOrderHistory.add(accountOrderHistory);
        }

        if (!saveOrderHistory.isEmpty()) {
            accountOrderHistoryMapper.batchInsert(saveOrderHistory);
        }


        return new AsyncResult<>(true);
    }

    /**
     * 异步刷新一个用户的市场交易记录
     * @param account 用户
     * @return 结果
     */
    @Async("threadPoolTaskExecutor")
    public Future<Boolean> refreshWalletTransactions(Account account) {

        JSONArray walletTransactionsArr = esi.charactersWalletTransactions(account);

        Long lastId = accountWalletTransactionsMapper.getLast(account.getId());

        if (lastId != null) {
            walletTransactionsArr = walletTransactionsArr.stream().filter(item -> ((JSONObject) item).getLongValue("transaction_id") > lastId).collect(Collectors.toCollection(JSONArray::new));
        }

        if(walletTransactionsArr.isEmpty()){
            return new AsyncResult<>(true);
        }

        List<AccountWalletTransactions> saveOrderHistory = new ArrayList<>();

        Map<Integer,Type> typeMap = cache.getTypeMap();

        List<Integer> ids = walletTransactionsArr.stream().map(item->((JSONObject)item).getIntValue("client_id")).collect(Collectors.toList());
        JSONArray nameArrays = esi.universeNames(ids);
        Map<Integer,Map<String,String>> idNames = new HashMap<>();
        for (Object nameArray : nameArrays) {
            JSONObject json = (JSONObject) nameArray;
            Map<String,String> temp = new HashMap<>();
            temp.put("name",json.getString("name"));
            temp.put("type",json.getString("category"));
            idNames.put(json.getIntValue("id"),temp);
        }
        for (Object o : walletTransactionsArr) {
            JSONObject obj = (JSONObject) o;
            AccountWalletTransactions accountWalletTransactions = obj.toJavaObject(AccountWalletTransactions.class);
            accountWalletTransactions.setAccountId(account.getId());
            accountWalletTransactions.setUserId(account.getUserId());
            accountWalletTransactions.setCharacterId(account.getCharacterId());
            accountWalletTransactions.setCharacterName(account.getCharacterName());
            accountWalletTransactions.setDate(DateUtil.parse(obj.getString("date")).setTimeZone(TimeZone.getDefault()).toTimestamp());
            accountWalletTransactions.setId(obj.getLongValue("transaction_id"));
            accountWalletTransactions.setLocationName(esi.getLocationName(account, accountWalletTransactions.getLocationId()));
            Type type = typeMap.get(accountWalletTransactions.getTypeId());
            if(type != null){
                accountWalletTransactions.setTypeName(type.getName());
            }
            if(idNames.containsKey(accountWalletTransactions.getClientId())){
                Map<String,String> nameMapping = idNames.get(accountWalletTransactions.getClientId());
                accountWalletTransactions.setClientName(nameMapping.get("name"));
                accountWalletTransactions.setClientType(nameMapping.get("type"));
            }




            saveOrderHistory.add(accountWalletTransactions);
        }

        if (!saveOrderHistory.isEmpty()) {
            accountWalletTransactionsMapper.batchInsert(new ArrayList<>(new HashSet<>(saveOrderHistory)));
        }


        return new AsyncResult<>(true);
    }

    /**
     * 异步刷新一个用户的市场订单
     * @param account 用户
     * @return 结果
     */
    @Async("threadPoolTaskExecutor")
    public Future<Boolean> refreshAccountOrder(Account account) {

        JSONArray walletOrderArr = esiClient.charactersOrders(account);

        if(walletOrderArr.isEmpty()){
            return new AsyncResult<>(true);
        }

        List<AccountOrder> saveOrderHistory = new ArrayList<>();

        Map<Integer,Type> typeMap = cache.getTypeMap();


        for (Object o : walletOrderArr) {
            JSONObject obj = (JSONObject) o;
            AccountOrder accountOrder = obj.toJavaObject(AccountOrder.class);
            accountOrder.setAccountId(account.getId());
            accountOrder.setUserId(account.getUserId());
            accountOrder.setCharacterId(account.getCharacterId());
            accountOrder.setCharacterName(account.getCharacterName());
            accountOrder.setIssued(DateUtil.parse(obj.getString("issued")).setTimeZone(TimeZone.getDefault()).toTimestamp());
            accountOrder.setId(obj.getLongValue("order_id"));
            accountOrder.setLocationName(esi.getLocationName(account, accountOrder.getLocationId()));
            Type type = typeMap.get(accountOrder.getTypeId());
            if(type != null){
                accountOrder.setTypeName(type.getName());
            }
            accountOrder.setRegionName(esi.universeRegions(accountOrder.getRegionId()));

            saveOrderHistory.add(accountOrder);
        }

        accountOrderMapper.delete(new QueryWrapper<AccountOrder>().eq("account_id",account.getId()));
        if (!saveOrderHistory.isEmpty()) {
            accountOrderMapper.batchInsert(saveOrderHistory);
        }


        return new AsyncResult<>(true);
    }

    /**
     * 分页查询当前登录用户的角色列表
     *
     * @param basicQuery 分页条件
     * @return 用户角色列表
     */
    public PageEntity list(BasicQuery<Account> basicQuery) {
        QueryWrapper<Account> queryWrapper = basicQuery.getQueryWrapper();
        queryWrapper.eq("user_id", TokenUtil.getUserId());
        return PageEntity.success(accountMapper.selectPage(basicQuery.getPage(), queryWrapper));
    }

    /**
     * 查询当前登录的角色列表
     * @return 角色列表
     */
    public RespEntity listLoginAccount(){
        return RespEntity.success(getLoginAccount());
    }

    /**
     * 刷新当前登录用户所有的订单
     * @return 标准返回
     */
    public RespEntity refreshMarketTransactions(){

        List<Account> accountList = getLoginAccount();
        for (Account account : accountList) {
            this.refreshWalletTransactions(account);
        }

        return RespEntity.success();
    }

    /**
     * 刷新当前登录用户所有的订单
     * @return 标准返回
     */
    public RespEntity refreshAccountOrder(){

        List<Account> accountList = getLoginAccount();
        for (Account account : accountList) {
            this.refreshAccountOrder(account);
        }

        return RespEntity.success();
    }


    /**
     * 获取当前登录人员的角色列表
     * @return 角色列表
     */
    public List<Account> getLoginAccount(){
        return accountMapper.selectList(new QueryWrapper<Account>().eq("user_id",TokenUtil.getUserId()));
    }
}
