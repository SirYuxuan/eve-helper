package com.yuxuan66.modules.usercenter.service;

import com.yuxuan66.cache.modules.EveCache;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.modules.account.entity.query.AccountAssetsQuery;
import com.yuxuan66.modules.account.mapper.AccountAssetsMapper;
import com.yuxuan66.support.basic.model.PageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Sir丶雨轩
 * @since 2022/5/24
 */
@Service
@RequiredArgsConstructor
public class AccountAssetsService {

    @Resource
    private AccountAssetsMapper accountAssetsMapper;
    private final EveCache cache;
    /**
     * 分页查询角色资产列表
     *
     * @param accountOrderQuery 查询分页条件
     * @return 标准返回
     */
    public PageEntity list(AccountAssetsQuery accountOrderQuery) {

        if(accountOrderQuery.getAccountId() == null){
            accountOrderQuery.setUserId(TokenUtil.getUserId());
        }

        if(accountOrderQuery.getMarketGroupId() != null){
            // 查询所有子集数据
            accountOrderQuery.setMarketGroupIds(cache.getMarketGroupIdByPid(accountOrderQuery.getMarketGroupId()));
        }

        return PageEntity.success(accountAssetsMapper.selectAccountAssets(accountOrderQuery.getPage(), accountOrderQuery));
    }




}
