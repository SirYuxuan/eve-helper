package com.yuxuan66.modules.account.rest;

import com.yuxuan66.modules.account.entity.Account;
import com.yuxuan66.modules.account.service.AccountService;
import com.yuxuan66.support.basic.BasicQuery;
import com.yuxuan66.support.basic.model.PageEntity;
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
}
