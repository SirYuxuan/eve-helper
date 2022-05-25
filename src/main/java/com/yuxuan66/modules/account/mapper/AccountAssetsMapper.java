package com.yuxuan66.modules.account.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuxuan66.modules.account.entity.AccountAssets;
import com.yuxuan66.modules.account.entity.query.AccountAssetsQuery;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 2022/5/25
 */
@Mapper
public interface AccountAssetsMapper extends BasicMapper<AccountAssets> {

    /**
     * 分页查询资产列表
     * @param page 分页参数
     * @param query 查询参数
     * @return 资产列表
     */
    Page<AccountAssets> selectAccountAssets(Page<AccountAssets> page,@Param("query") AccountAssetsQuery query);

    /**
     * 批量插入
     * @param list 数据
     * @return 插入条数
     */
    long batchInsert(List<AccountAssets> list);
}
