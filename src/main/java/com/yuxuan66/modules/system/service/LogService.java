package com.yuxuan66.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.modules.system.entity.Log;
import com.yuxuan66.modules.system.entity.LogMapper;
import com.yuxuan66.support.basic.BasicQuery;
import com.yuxuan66.support.basic.model.PageEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Sir丶雨轩
 * @since 2021/12/7
 */
@Service
public class LogService {

    @Resource
    private LogMapper logMapper;

    /**
     * 分页查询我的操作日志
     * @param logBasicQuery 查询条件
     * @return 查询日志列表
     */
    public PageEntity myLogList(BasicQuery<Log> logBasicQuery){
        QueryWrapper<Log> queryWrapper = logBasicQuery.getQueryWrapper();
        queryWrapper.eq("create_id", TokenUtil.getUserId());
        queryWrapper.orderByDesc("create_time");
        return PageEntity.success(logMapper.selectPage(logBasicQuery.getPage(),queryWrapper));
    }
}
