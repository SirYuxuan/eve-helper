package com.yuxuan66.modules.order.rest;

import com.yuxuan66.modules.order.entity.Order;
import com.yuxuan66.modules.order.entity.query.OrderQuery;
import com.yuxuan66.modules.order.service.OrderService;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Sir丶雨轩
 * @since 2022/5/27
 */
@RequestMapping(path = "/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @GetMapping
    public PageEntity list(OrderQuery orderQuery){
        return orderService.list(orderQuery);
    }



    /**
     * 添加一条信的订单记录
     * @param order 订单
     * @return 标准返回
     */
    @PostMapping
    public RespEntity add(@RequestBody Order order){
        return orderService.add(order);
    }
}
