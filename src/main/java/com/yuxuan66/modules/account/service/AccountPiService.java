package com.yuxuan66.modules.account.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.modules.account.entity.Account;
import com.yuxuan66.modules.account.entity.AccountPi;
import com.yuxuan66.modules.account.entity.query.AccountPiQuery;
import com.yuxuan66.modules.account.mapper.AccountMapper;
import com.yuxuan66.modules.account.mapper.AccountPiMapper;
import com.yuxuan66.support.basic.model.PageEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountPiService {

    @Resource
    private AccountPiMapper accountPiMapper;

    @Resource
    private AccountMapper accountMapper;

    /**
     * 分页查询角色行星列表
     *
     * @param piQuery 查询条件
     * @return 标准返回
     */
    public PageEntity list(AccountPiQuery piQuery) {
        piQuery.processingSort();
        QueryWrapper<AccountPi> queryWrapper = piQuery.getQueryWrapper();
        if (piQuery.getAccountId() == null) {
            queryWrapper.eq("user_id", TokenUtil.getUserId());
        } else {
            queryWrapper.eq("account_id", piQuery.getAccountId());
        }
        List<Account> accountList = accountMapper.selectList(new QueryWrapper<Account>().eq("user_id", TokenUtil.getUserId()));
        Map<Long, String> accountNameMap = new HashMap<>();
        accountList.forEach(item -> accountNameMap.put(item.getId(), item.getAccountName()));

        Page<AccountPi> accountPiPage = accountPiMapper.selectPage(piQuery.getPage(), queryWrapper);
        for (AccountPi record : accountPiPage.getRecords()) {
            if (StrUtil.isNotBlank(accountNameMap.get(record.getAccountId()))) {
                record.setAccountName(accountNameMap.get(record.getAccountId()));
            }
        }
        return PageEntity.success(accountPiPage);
    }
}
