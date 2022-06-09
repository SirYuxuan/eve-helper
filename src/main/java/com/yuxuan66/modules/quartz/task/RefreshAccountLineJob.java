package com.yuxuan66.modules.quartz.task;

import cn.hutool.core.util.StrUtil;
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
public class RefreshAccountLineJob implements Job {
    @Resource
    private AccountMapper accountMapper;
    private final AccountService accountService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Account> accountList = accountMapper.selectList(null);

        for (Account account : accountList) {

            if(StrUtil.isBlank(account.getAccessToken())){
                continue;
            }
            log.info("开始刷新[" + account.getCharacterName()+"]的数据");

            // 刷新角色产线
            try {
                accountService.refreshFinalSkill(account);
            } catch (Exception e) {
                log.warn("角色[" + account.getCharacterName() + "]产线数据刷新失败", e);
            }

        }


    }
}
