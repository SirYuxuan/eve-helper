package com.yuxuan66.modules.account.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuxuan66.cache.modules.EveCache;
import com.yuxuan66.common.esi.EsiApi;
import com.yuxuan66.common.esi.entity.EsiAccountInfo;
import com.yuxuan66.common.esi.http.EsiClient;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.modules.account.entity.*;
import com.yuxuan66.modules.account.mapper.*;
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
    private final Map<String, String> starMap = new HashMap<>();

    {
        starMap.put("temperate", "温和");
        starMap.put("barren", "贫瘠");
        starMap.put("oceanic", "海洋");
        starMap.put("ice", "冰体");
        starMap.put("gas", "气体");
        starMap.put("lava", "熔岩");
        starMap.put("storm", "风暴");
        starMap.put("plasma", "等离子");
    }

    @Resource
    private AccountMapper accountMapper;
    @Resource
    private AccountAssetsMapper accountAssetsMapper;
    @Resource
    private AccountOrderHistoryMapper accountOrderHistoryMapper;
    @Resource
    private AccountOrderMapper accountOrderMapper;
    @Resource
    private AccountWalletTransactionsMapper accountWalletTransactionsMapper;
    @Resource
    private AccountSkillMapper accountSkillMapper;
    @Resource
    private AccountPiMapper accountPiMapper;
    @Resource
    private AccountIndustryMapper accountIndustryMapper;

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

    @Async("threadPoolTaskExecutor")
    public Future<Boolean> refreshSkill(Account account) {

        JSONArray skillArray = esi.charactersSkillQueue(account);

        Map<Integer, Type> typeMap = cache.getTypeMap();

        List<AccountSkill> accountSkillList = new ArrayList<>();

        if (!skillArray.isEmpty()) {
            for (Object o : skillArray) {
                AccountSkill accountSkill = ((JSONObject) o).toJavaObject(AccountSkill.class);
                accountSkill.setUserId(account.getUserId());
                accountSkill.setAccountId(account.getId());
                accountSkill.setCharacterId(account.getCharacterId());
                accountSkill.setCharacterName(account.getCharacterName());
                accountSkill.setSkillName(typeMap.get(accountSkill.getSkillId()).getName());
                accountSkillList.add(accountSkill);
            }
            Integer skillId = skillArray.getJSONObject(0).getIntValue("skill_id");
            Type type = typeMap.get(skillId);
            String endTime = skillArray.getJSONObject(skillArray.size() - 1).getString("finish_date");
            if (StrUtil.isNotBlank(endTime)) {
                account.setSkillEndTime(DateUtil.parse(endTime).toStringDefaultTimeZone());
                account.setSkillName(type.getName() + " " + skillArray.getJSONObject(0).getIntValue("finished_level"));
                accountMapper.updateById(account);
            }

            accountSkillMapper.delete(new QueryWrapper<AccountSkill>().eq("account_id", account.getId()));
            accountSkillMapper.batchInsert(accountSkillList);
        } else {
            account.setSkillName("");
            account.setSkillEndTime("");
            accountMapper.updateById(account);
        }
        return new AsyncResult<>(true);
    }

    /**
     * 异步刷新一个用户的技能队列
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

        Map<Integer, Type> typeMap = cache.getTypeMap();

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
            if (type != null) {
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
     *
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

        if (walletTransactionsArr.isEmpty()) {
            return new AsyncResult<>(true);
        }

        List<AccountWalletTransactions> saveOrderHistory = new ArrayList<>();

        Map<Integer, Type> typeMap = cache.getTypeMap();

        List<Integer> ids = walletTransactionsArr.stream().map(item -> ((JSONObject) item).getIntValue("client_id")).collect(Collectors.toList());
        JSONArray nameArrays = esi.universeNames(ids);
        Map<Integer, Map<String, String>> idNames = new HashMap<>();
        for (Object nameArray : nameArrays) {
            JSONObject json = (JSONObject) nameArray;
            Map<String, String> temp = new HashMap<>();
            temp.put("name", json.getString("name"));
            temp.put("type", json.getString("category"));
            idNames.put(json.getIntValue("id"), temp);
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
            if (type != null) {
                accountWalletTransactions.setTypeName(type.getName());
            }
            if (idNames.containsKey(accountWalletTransactions.getClientId())) {
                Map<String, String> nameMapping = idNames.get(accountWalletTransactions.getClientId());
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
     *
     * @param account 用户
     * @return 结果
     */
    @Async("threadPoolTaskExecutor")
    public Future<Boolean> refreshAccountOrder(Account account) {

        JSONArray walletOrderArr = esiClient.charactersOrders(account);

        if (walletOrderArr.isEmpty()) {
            return new AsyncResult<>(true);
        }

        List<AccountOrder> saveOrderHistory = new ArrayList<>();

        Map<Integer, Type> typeMap = cache.getTypeMap();


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
            if (type != null) {
                accountOrder.setTypeName(type.getName());
            }
            accountOrder.setRegionName(esi.universeRegions(accountOrder.getRegionId()));

            saveOrderHistory.add(accountOrder);
        }

        accountOrderMapper.delete(new QueryWrapper<AccountOrder>().eq("account_id", account.getId()));
        if (!saveOrderHistory.isEmpty()) {
            accountOrderMapper.batchInsert(saveOrderHistory);
        }


        return new AsyncResult<>(true);
    }


    public void refreshIndustry() {
        Account account = accountMapper.selectById(94);

        Map<Integer, Account> accountMap = new HashMap<>();
        accountMapper.selectList(new QueryWrapper<Account>().eq("user_id", account.getUserId())).forEach(item -> accountMap.put(item.getCharacterId(), item));
        JSONArray industryArr = esi.corporationsIndustryJobs(account, 1);

        List<AccountIndustry> accountIndustryList = new ArrayList<>();
        Map<Integer, Type> typeMap = cache.getTypeMap();

        for (Object o : industryArr) {
            AccountIndustry accountIndustry = ((JSONObject) o).toJavaObject(AccountIndustry.class);
            Account dataAccount = accountMap.get(accountIndustry.getInstallerId());
            if (dataAccount != null) {
                accountIndustry.setAccountId(dataAccount.getId());
                accountIndustry.setUserId(dataAccount.getUserId());
                accountIndustry.setCharacterId(dataAccount.getCharacterId());
                accountIndustry.setCharacterName(dataAccount.getCharacterName());
                Type type = typeMap.get(accountIndustry.getBlueprintTypeId());
                if (type != null) {
                    accountIndustry.setBlueprintTypeName(type.getName());
                }
                type = typeMap.get(accountIndustry.getProductTypeId());
                if (type != null) {
                    accountIndustry.setProductTypeName(type.getName());
                }

                accountIndustryList.add(accountIndustry);
            }
        }
        if (!accountIndustryList.isEmpty()) {
            accountIndustryMapper.batchInsert(accountIndustryList);
        }
    }

    @Async("threadPoolTaskExecutor")
    public Future<Boolean> refreshPI(Account account) {
        JSONArray piArr = esiClient.charactersPlanets(account);
        List<Type> typeList = cache.getTypeList();
        List<AccountPi> accountPiList = new ArrayList<>();
        for (Object o : piArr) {
            AccountPi pi = ((JSONObject) o).toJavaObject(AccountPi.class);
            pi.setAccountId(account.getId());
            pi.setUserId(account.getUserId());
            pi.setCharacterId(account.getCharacterId());
            pi.setCharacterName(account.getCharacterName());
            JSONObject details = esiClient.charactersPlanets(account, pi.getPlanetId());
            Set<Integer> schematicIdSet = new HashSet<>();
            pi.setSolarSystemName(esi.universeSystems(pi.getSolarSystemId()));
            pi.setPlanetType(starMap.get(pi.getPlanetType()));
            if (details.containsKey("pins")) {
                for (Object pins : details.getJSONArray("pins")) {
                    JSONObject pin = (JSONObject) pins;
                    if (pin.containsKey("schematic_id")) {
                        schematicIdSet.add(pin.getIntValue("schematic_id"));
                    }
                }
                List<String> nameList = new ArrayList<>();
                if (!schematicIdSet.isEmpty()) {
                    for (Integer id : schematicIdSet) {
                        Optional<Type> optionalType = typeList.stream().filter(item -> item.getNameEn().equals(esi.universesSchematics(id))).findFirst();
                        optionalType.ifPresent(type -> nameList.add(type.getName()));
                    }
                }
                pi.setProduce(String.join(",", nameList));
                accountPiList.add(pi);
            }
        }
        accountPiMapper.delete(new QueryWrapper<AccountPi>().eq("account_id", account.getId()));
        for (AccountPi accountPi : accountPiList) {
            accountPiMapper.insert(accountPi);
        }

        return new AsyncResult<>(true);
    }

    /**
     * 异步刷新一个用户的资产
     *
     * @param account 用户
     * @return 结果
     */
    @Async("threadPoolTaskExecutor")
    public Future<Boolean> refreshAccountAssets(Account account) {

        JSONArray assetsArr = esi.assets(account, 1);
        JSONArray itemArr = esi.assetsNames(account, assetsArr.stream().map(item -> ((JSONObject) item).getLongValue("item_id")).distinct().collect(Collectors.toList()));
        Map<Long, String> itemNameMapping = new HashMap<>();

        for (Object o : itemArr) {
            JSONObject obj = (JSONObject) o;
            itemNameMapping.put(obj.getLongValue("item_id"), obj.getString("name"));
        }
        List<AccountAssets> accountAssetsList = new ArrayList<>();
        Map<Integer, Type> typeMap = cache.getTypeMap();
        for (Object o : assetsArr) {
            JSONObject obj = (JSONObject) o;
            AccountAssets accountAssets = obj.toJavaObject(AccountAssets.class);
            accountAssets.setAccountId(account.getId());
            accountAssets.setUserId(account.getUserId());
            accountAssets.setCharacterId(account.getCharacterId());
            accountAssets.setCharacterName(account.getCharacterName());
            Type type = typeMap.get(accountAssets.getTypeId());
            if (type != null) {
                accountAssets.setTypeName(type.getName());
            }
            accountAssets.setItemName(itemNameMapping.get(accountAssets.getItemId()));

            accountAssetsList.add(accountAssets);
        }
        for (AccountAssets accountAssets : accountAssetsList) {
            // 处理位置名称
            Optional<AccountAssets> accountAssetsOptional = accountAssetsList.stream().filter(item -> item.getItemId().equals(accountAssets.getLocationId())).findFirst();
            if (accountAssetsOptional.isPresent()) {
                accountAssets.setLocationName(accountAssetsOptional.get().getItemName());
            } else {
                accountAssets.setLocationName(esi.getLocationName(account, accountAssets.getLocationId()));
            }
            accountAssets.setCount(Convert.toInt(accountAssetsList.stream().filter(item -> item.getLocationId().equals(accountAssets.getItemId())).count()));
        }
        accountAssetsMapper.delete(new QueryWrapper<AccountAssets>().eq("account_id", account.getId()));
        if (!accountAssetsList.isEmpty()) {
            accountAssetsMapper.batchInsert(accountAssetsList);
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
        basicQuery.processingSort();
        QueryWrapper<Account> queryWrapper = basicQuery.getQueryWrapper();
        queryWrapper.eq("user_id", TokenUtil.getUserId());
        List<AccountSkill> accountSkillList = accountSkillMapper.selectList(new QueryWrapper<AccountSkill>().eq("user_id", TokenUtil.getUserId()));
        List<AccountIndustry> accountIndustryList = accountIndustryMapper.selectList(new QueryWrapper<AccountIndustry>().eq("user_id",TokenUtil.getUserId()));

        Page<Account> accountPage = accountMapper.selectPage(basicQuery.getPage(), queryWrapper);


        for (Account record : accountPage.getRecords()) {
            record.setAccountSkillList(accountSkillList.stream().filter(item -> item.getAccountId().equals(record.getId())).collect(Collectors.toList()));
            List<AccountIndustry> checkOneList = accountIndustryList.stream().filter(item->item.getAccountId().equals(record.getId())).collect(Collectors.toList());
            record.setReaction(checkOneList.stream().filter(item->item.getActivityId() == 9).count());
            record.setMake(checkOneList.stream().filter(item->item.getActivityId() == 1).count());
            record.setScientificResearch(checkOneList.size() - record.getMake() - record.getReaction());

        }
        return PageEntity.success(accountPage);
    }

    /**
     * 查询当前登录的角色列表
     *
     * @return 角色列表
     */
    public RespEntity listLoginAccount() {
        return RespEntity.success(getLoginAccount());
    }

    /**
     * 刷新当前登录用户所有的订单
     *
     * @return 标准返回
     */
    public RespEntity refreshMarketTransactions() {

        List<Account> accountList = getLoginAccount();
        for (Account account : accountList) {
            this.refreshWalletTransactions(account);
        }

        return RespEntity.success();
    }

    /**
     * 刷新当前登录用户所有的订单
     *
     * @return 标准返回
     */
    public RespEntity refreshAccountOrder() {
        this.refreshIndustry();

        List<Account> accountList = getLoginAccount();
        for (Account account : accountList) {
            this.refreshAccountOrder(account);
        }

        return RespEntity.success();
    }

    /**
     * 刷新当前登录用户所有的资产
     *
     * @return 标准返回
     */
    public RespEntity refreshAccountAssets() {

        List<Account> accountList = getLoginAccount();
        for (Account account : accountList) {
            this.refreshAccountAssets(account);
        }

        return RespEntity.success();
    }

    /**
     * 设置用户为主角色
     * @param accountId 角色id
     * @return 标准返回
     */
    public RespEntity setMainAccount(Long accountId){
        Account account = accountMapper.selectById(accountId);
        UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id",account.getUserId());
        updateWrapper.set("is_main",false);
        accountMapper.update(new Account(),updateWrapper);
        account.setIsMain(true);
        accountMapper.updateById(account);
        return RespEntity.success();
    }


    /**
     * 删除一个授权用户
     *
     * @param accountId 用户id
     * @return 标准返回
     */
    public RespEntity delAccount(Long accountId) {
        accountMapper.deleteById(accountId);
        return RespEntity.success();
    }

    /**
     * 修改一个角色的账号名
     *
     * @param accountId   账号id
     * @param accountName 账号名
     * @return 标准返回
     */
    public RespEntity setAccountName(Long accountId, String accountName) {
        Account account = accountMapper.selectById(accountId);
        account.setAccountName(accountName);
        accountMapper.updateById(account);
        return RespEntity.success();
    }

    /**
     * 获取当前登录人员的角色列表
     *
     * @return 角色列表
     */
    public List<Account> getLoginAccount() {
        return accountMapper.selectList(new QueryWrapper<Account>().eq("user_id", TokenUtil.getUserId()));
    }
}
