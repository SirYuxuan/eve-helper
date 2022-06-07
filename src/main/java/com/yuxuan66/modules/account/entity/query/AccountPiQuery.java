package com.yuxuan66.modules.account.entity.query;

import com.yuxuan66.modules.account.entity.AccountPi;
import com.yuxuan66.support.basic.BasicQuery;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountPiQuery extends BasicQuery<AccountPi> {

    private Long accountId;
}
