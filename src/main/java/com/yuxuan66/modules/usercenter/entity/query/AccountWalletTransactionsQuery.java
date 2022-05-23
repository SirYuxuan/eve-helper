package com.yuxuan66.modules.usercenter.entity.query;

import com.yuxuan66.modules.account.entity.AccountWalletTransactions;
import com.yuxuan66.support.basic.BasicQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Sir丶雨轩
 * @since 2022/5/23
 */
@Setter
@Getter
public class AccountWalletTransactionsQuery extends BasicQuery<AccountWalletTransactions> {

    private Long accountId;

}
