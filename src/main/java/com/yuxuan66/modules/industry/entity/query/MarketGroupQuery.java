package com.yuxuan66.modules.industry.entity.query;

import com.yuxuan66.modules.database.entity.MarketGroup;
import com.yuxuan66.support.basic.BasicQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Sir丶雨轩
 * @since 2022/2/11
 */
@Setter
@Getter
public class MarketGroupQuery extends BasicQuery<MarketGroup> {

    private Integer pid;

    private Boolean hasType = false;
}
