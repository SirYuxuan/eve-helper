package com.yuxuan66.modules.market.mapper;

import com.yuxuan66.modules.market.entity.Market;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 6/23/2022
 */
@Mapper
public interface MarketMapper extends BasicMapper<Market> {

    /**
     * 批量插入
     * @param list 集合
     * @return 插入条数
     */
    long batchInsert(List<Market> list);
}
