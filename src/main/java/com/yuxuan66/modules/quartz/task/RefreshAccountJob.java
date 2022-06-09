package com.yuxuan66.modules.quartz.task;

import cn.hutool.core.util.StrUtil;
import com.yuxuan66.common.esi.EsiApi;
import com.yuxuan66.modules.account.entity.Account;
import com.yuxuan66.modules.account.mapper.AccountMapper;
import com.yuxuan66.modules.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时刷新用户的基本信息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshAccountJob implements Job {
    @Resource
    private AccountMapper accountMapper;
    private final AccountService accountService;
    private final EsiApi esi;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Account> accountList = accountMapper.selectList(null);

        for (Account account : accountList) {

            esi.refreshToken(account);
            if(StrUtil.isBlank(account.getAccessToken())){
                continue;
            }
            log.info("开始刷新[" + account.getCharacterName()+"]的数据");


            // 刷新角色基础数据
            try {
                accountService.refreshBaseInfo(account);
            } catch (Exception e) {
                log.warn("角色[" + account.getCharacterName() + "]基础数据刷新失败", e);
            }
            // 刷新角色资产
            try {
                accountService.refreshAccountAssets(account);
            } catch (Exception e) {
                log.warn("角色[" + account.getCharacterName() + "]资产刷新失败", e);
            }
            // 刷新角色订单
            try {
                accountService.refreshAccountOrder(account);
            } catch (Exception e) {
                log.warn("角色[" + account.getCharacterName() + "]订单刷新失败", e);
            }
            // 刷新角色订单历史
            try {
                accountService.refreshOrderHistory(account);
            } catch (Exception e) {
                log.warn("角色[" + account.getCharacterName() + "]订单历史刷新失败", e);
            }
            // 刷新角色钱包流水
            try {
                accountService.refreshWalletTransactions(account);
            } catch (Exception e) {
                log.warn("角色[" + account.getCharacterName() + "]钱包流水刷新失败", e);
            }
            // 刷新角色的菜地
            try {
                accountService.refreshPI(account);
            } catch (Exception e) {
                log.warn("角色[" + account.getCharacterName() + "]菜地刷新失败", e);
            }
            // 刷新角色的技能
            try {
                accountService.refreshSkill(account);
            } catch (Exception e) {
                log.warn("角色[" + account.getCharacterName() + "]资产刷新失败", e);
            }

            // 刷新主角色的工业
            if (account.getIsMain()) {
                try {
                    accountService.refreshIndustry(account);
                } catch (Exception e) {
                    log.warn("角色[" + account.getCharacterName() + "]工业刷新失败", e);
                }
            }


        }


    }
}
