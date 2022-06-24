package com.yuxuan66.modules.market.entity.query;

import com.yuxuan66.modules.market.entity.Market;
import com.yuxuan66.support.basic.BasicQuery;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 6/23/2022
 */
@Setter
@Getter
public class MarketQuery extends BasicQuery<Market> {

    private Integer marketGroupId;

    private List<String> meta;

}
