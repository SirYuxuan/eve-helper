package com.yuxuan66.modules.account.rest;

import com.yuxuan66.modules.account.entity.Account;
import com.yuxuan66.modules.account.service.AccountService;
import com.yuxuan66.support.basic.BasicQuery;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sir丶雨轩
 * @since 2022/5/21
 */
@RestController
@RequestMapping(path = "/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    /**
     * 分页查询当前登录用户的角色列表
     * @param basicQuery 分页条件
     * @return 用户角色列表
     */
    @GetMapping
    public PageEntity list(BasicQuery<Account> basicQuery){
        return accountService.list(basicQuery);
    }

    /**
     * 查询当前登录的角色列表
     * @return 角色列表
     */
    @GetMapping(path = "/listLoginAccount")
    public RespEntity listLoginAccount(){
        return accountService.listLoginAccount();
    }

    /**
     * 刷新当前登录用户所有的订单
     * @return 标准返回
     */
    @GetMapping(path = "/refreshMarketTransactions")
    public RespEntity refreshMarketTransactions(){
        return accountService.refreshMarketTransactions();
    }
    /**
     * 刷新当前登录用户所有的订单
     * @return 标准返回
     */
    @GetMapping(path = "/refreshAccountOrder")
    public RespEntity refreshAccountOrder(){
        return accountService.refreshAccountOrder();
    }

    /**
     * 刷新当前登录用户所有的资产
     *
     * @return 标准返回
     */
    @GetMapping(path = "/refreshAccountAssets")
    public RespEntity refreshAccountAssets(){
        return accountService.refreshAccountAssets();
    }
}
