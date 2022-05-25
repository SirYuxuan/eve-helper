package com.yuxuan66.modules.usercenter.rest;

import com.yuxuan66.modules.account.entity.query.AccountAssetsQuery;
import com.yuxuan66.modules.usercenter.service.AccountAssetsService;
import com.yuxuan66.support.basic.model.PageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sir丶雨轩
 * @since 2022/5/24
 */
@RestController
@RequestMapping(path = "/accountAssets")
@RequiredArgsConstructor
public class AccountAssetsController {

    private final AccountAssetsService accountAssetsService;

    /**
     * 分页查询角色资产列表
     *
     * @param accountAssetsQuery 查询分页条件
     * @return 标准返回
     */
    @GetMapping
    public PageEntity list(AccountAssetsQuery accountAssetsQuery) {
        return accountAssetsService.list(accountAssetsQuery);
    }

}
