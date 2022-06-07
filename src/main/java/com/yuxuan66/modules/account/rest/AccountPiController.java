package com.yuxuan66.modules.account.rest;

import com.yuxuan66.modules.account.entity.query.AccountPiQuery;
import com.yuxuan66.modules.account.service.AccountPiService;
import com.yuxuan66.support.basic.model.PageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/accountPi")
@RequiredArgsConstructor
public class AccountPiController {

    private final AccountPiService accountPiService;

    /**
     * 分页查询角色行星列表
     * @param piQuery 查询条件
     * @return 标准返回
     */
    @GetMapping
    public PageEntity list(AccountPiQuery piQuery) {
        return accountPiService.list(piQuery);
    }
}
