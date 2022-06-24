package com.yuxuan66.modules.market.rest;

import com.yuxuan66.modules.market.entity.query.MarketQuery;
import com.yuxuan66.modules.market.service.MarketService;
import com.yuxuan66.support.basic.model.PageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sir丶雨轩
 * @since 6/23/2022
 */
@RequestMapping(path = "/market")
@RestController
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;
    /**
     * 分页查询市场数据
     * @param marketQuery 查询条件
     * @return 市场数据
     */
    @GetMapping
    public PageEntity list(MarketQuery marketQuery){
        return marketService.list(marketQuery);
    }

}
