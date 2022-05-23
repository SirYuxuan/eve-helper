package com.yuxuan66.modules.usercenter.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.modules.account.entity.AccountWalletTransactions;
import com.yuxuan66.modules.account.mapper.AccountWalletTransactionsMapper;
import com.yuxuan66.modules.usercenter.entity.query.AccountWalletTransactionsQuery;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Sir丶雨轩
 * @since 2022/5/23
 */
@Service
public class WalletTransactionsService {

    @Resource
    private AccountWalletTransactionsMapper accountWalletTransactionsMapper;


    /**
     * 分页查询角色市场交易记录
     * @param accountWalletTransactionsQuery 查询分页条件
     * @return 标准返回
     */
    public PageEntity list(AccountWalletTransactionsQuery accountWalletTransactionsQuery){
        accountWalletTransactionsQuery.processingSort();
        QueryWrapper<AccountWalletTransactions> queryWrapper = accountWalletTransactionsQuery.getQueryWrapper();
        return PageEntity.success(accountWalletTransactionsMapper.selectPage(accountWalletTransactionsQuery.getPage(),queryWrapper));
    }

    /**
     * 查询30天内每日的交易额
     * @return 标准返回
     */
    public RespEntity dailySales(){
        return RespEntity.success(accountWalletTransactionsMapper.dailySales());
    }
}
