package com.yuxuan66.modules.account.mapper;

import com.yuxuan66.modules.account.entity.AccountOrderHistory;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 2022/5/21
 */
@Mapper
public interface AccountOrderHistoryMapper extends BasicMapper<AccountOrderHistory> {


    /**
     * 获取最大的订单ID
     * @param accountId 用户id
     * @return 订单ID
     */
    @Select("select id from eve_account_order_history where account_id = #{accountId} order by id desc limit 1")
    Long getLast(Long accountId);

    /**
     * 批量插入
     * @param list 数据
     * @return 插入条数
     */
    long batchInsert(List<AccountOrderHistory> list);
}
