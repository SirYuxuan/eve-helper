package com.yuxuan66.modules.database.mapper;

import com.yuxuan66.modules.database.entity.MarketOrders;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 2021/12/15
 */
@Mapper
public interface MarketOrdersMapper extends BasicMapper<MarketOrders> {

    /**
     * 批量插入eve market orders
     * @param list  market orders
     * @return 插入条数
     */
    long batchInsert(List<MarketOrders> list,int regionId);
}
