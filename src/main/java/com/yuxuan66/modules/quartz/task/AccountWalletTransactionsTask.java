package com.yuxuan66.modules.quartz.task;

import com.yuxuan66.modules.account.entity.Account;
import com.yuxuan66.modules.account.mapper.AccountMapper;
import com.yuxuan66.modules.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时刷新角色订单数据
 * @author Sir丶雨轩
 * @since 2022/5/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountWalletTransactionsTask implements Job {

    @Resource
    private AccountMapper accountMapper;
    private final AccountService accountService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Account> allAccount = accountMapper.selectList(null);
        for (Account account : allAccount) {
            try{

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
