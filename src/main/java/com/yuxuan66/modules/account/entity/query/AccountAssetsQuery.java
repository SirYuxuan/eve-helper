package com.yuxuan66.modules.account.entity.query;

import com.yuxuan66.modules.account.entity.AccountAssets;
import com.yuxuan66.support.basic.BasicQuery;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 2022/5/25
 */
@Setter
@Getter
public class AccountAssetsQuery extends BasicQuery<AccountAssets> {

    private Long userId;
    private Long accountId;
    private Integer marketGroupId;
    private List<Integer> marketGroupIds;
    private Integer metaGroupId;
    private Boolean isBlueprintCopy;


}
