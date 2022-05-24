package com.yuxuan66.modules.account.mapper;

import com.yuxuan66.modules.account.entity.AccountWalletTransactions;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author Sir丶雨轩
 * @since 2022/5/21
 */
@Mapper
public interface AccountWalletTransactionsMapper extends BasicMapper<AccountWalletTransactions> {

    /**
     * 获取最大的订单ID
     * @param accountId 用户id
     * @return 订单ID
     */
    @Select("select id from eve_account_wallet_transactions where account_id = #{accountId} order by id desc limit 1")
    Long getLast(Long accountId);


    /**
     * 批量插入
     * @param list 数据
     * @return 插入条数
     */
    long batchInsert(List<AccountWalletTransactions> list);

    /**
     * 查询30天内每日的交易额
     * @return 数据
     */
    @Select("select * from (select DATE_FORMAT(date,'%Y-%m-%d') date,sum(unit_price*quantity) price from eve_account_wallet_transactions  where account_id = #{accountId} and is_buy = 0 and DATE_SUB(CURDATE(), INTERVAL 15 DAY) <= date(date) GROUP BY DATE_FORMAT(date,'%Y-%m-%d')) a ORDER BY date desc")
    List<Map<String,Object>> dailySales(Long accountId);
}
