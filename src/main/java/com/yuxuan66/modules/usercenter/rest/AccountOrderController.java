package com.yuxuan66.modules.usercenter.rest;

import com.yuxuan66.modules.usercenter.entity.query.AccountOrderQuery;
import com.yuxuan66.modules.usercenter.service.AccountOrderService;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sir丶雨轩
 * @since 2022/5/24
 */
@RestController
@RequestMapping(path = "/accountOrder")
@RequiredArgsConstructor
public class AccountOrderController {

    private final AccountOrderService accountOrderService;

    /**
     * 查询一个角色当前的订单状态
     * @param accountId 角色
     * @return 标准返回
     */
    @GetMapping(path = "/marketMargin")
    public RespEntity marketMargin(Long accountId){
        return accountOrderService.marketMargin(accountId);
    }
    /**
     * 分页查询角色订单记录
     *
     * @param accountOrderQuery 查询分页条件
     * @return 标准返回
     */
    @GetMapping
    public PageEntity list(AccountOrderQuery accountOrderQuery) {
        return accountOrderService.list(accountOrderQuery);
    }

    /**
     * 对当前登录用户的所有角色订单进行价格建议
     * @return 标准返回
     */
    @GetMapping(path = "/priceProposal")
    public RespEntity priceProposal(boolean allJita){
        return accountOrderService.priceProposal(allJita);
    }

    /**
     * 打开游戏窗口
     * @param id 订单id
     * @return 标准返回
     */
    @GetMapping(path = "/openWindow")
    public RespEntity openWindow(Long id){
        return accountOrderService.openWindow(id);
    }
}
