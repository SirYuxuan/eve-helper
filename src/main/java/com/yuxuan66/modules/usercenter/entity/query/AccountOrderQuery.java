package com.yuxuan66.modules.usercenter.entity.query;

import com.yuxuan66.modules.account.entity.AccountOrder;
import com.yuxuan66.support.basic.BasicQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Sir丶雨轩
 * @since 2022/5/24
 */
@Setter
@Getter
public class AccountOrderQuery extends BasicQuery<AccountOrder> {

    private Long accountId;

    private Long userId;
    private Boolean isBuyOrder;

    private Boolean min;
}
