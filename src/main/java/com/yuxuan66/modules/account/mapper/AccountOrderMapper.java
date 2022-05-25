package com.yuxuan66.modules.account.mapper;

import com.yuxuan66.modules.account.entity.AccountOrder;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author Sir丶雨轩
 * @since 2022/5/24
 */
@Mapper
public interface AccountOrderMapper extends BasicMapper<AccountOrder> {

    /**
     * 批量插入
     * @param list 数据
     * @return 插入条数
     */
    long batchInsert(List<AccountOrder> list);

    /**
     * 查询一个角色的在售订单统计
     * @param accountId 角色
     * @return 数据
     */
    @Select("select region_name region,sum(price*volume_remain) price,false buy from eve_account_order where (is_buy_order is null or is_buy_order = 0) and account_id = #{accountId} GROUP BY region_name")
    List<Map<String,Object>> getSellOrderSum(Long accountId);

    /**
     * 查询一个角色的收购订单统计
     * @param accountId 角色
     * @return 数据
     */
    @Select("select region_name region,sum(price*volume_remain) price,true buy from eve_account_order where is_buy_order = 1 and account_id = #{accountId} GROUP BY region_name")
    List<Map<String,Object>> getBuyOrderSum(Long accountId);
}
