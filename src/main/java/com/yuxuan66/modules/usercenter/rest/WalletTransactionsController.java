package com.yuxuan66.modules.usercenter.rest;

import com.yuxuan66.modules.usercenter.entity.query.AccountWalletTransactionsQuery;
import com.yuxuan66.modules.usercenter.service.WalletTransactionsService;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sir丶雨轩
 * @since 2022/5/23
 */
@RestController
@RequestMapping(path = "/walletTransactions")
@RequiredArgsConstructor
public class WalletTransactionsController {

    private final WalletTransactionsService walletTransactionsService;

    /**
     * 分页查询角色市场交易记录
     * @param accountWalletTransactionsQuery 查询分页条件
     * @return 标准返回
     */
    @GetMapping
    public PageEntity list(AccountWalletTransactionsQuery accountWalletTransactionsQuery){
        return walletTransactionsService.list(accountWalletTransactionsQuery);
    }

    /**
     * 查询30天内每日的交易额
     * @return 标准返回
     */
    @GetMapping(path = "/dailySales")
    public RespEntity dailySales(Long accountId){
        return walletTransactionsService.dailySales(accountId);
    }
}
