package com.yuxuan66.modules.order.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.modules.order.entity.Order;
import com.yuxuan66.modules.order.entity.query.OrderQuery;
import com.yuxuan66.modules.order.mapper.OrderMapper;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Sir丶雨轩
 * @since 2022/5/27
 */
@Service
public class OrderService {

    @Resource
    private OrderMapper orderMapper;

    public PageEntity list(OrderQuery orderQuery){
        QueryWrapper<Order> queryWrapper = orderQuery.getQueryWrapper();
        return PageEntity.success(orderMapper.selectPage(orderQuery.getPage(),queryWrapper));
    }

    /**
     * 添加一条信的订单记录
     * @param order 订单
     * @return 标准返回
     */
    public RespEntity add(Order order){
        order.setUserId(TokenUtil.getUserId());
        orderMapper.insert(order);
        return RespEntity.success();
    }
}
