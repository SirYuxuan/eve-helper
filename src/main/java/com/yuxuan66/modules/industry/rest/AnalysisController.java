package com.yuxuan66.modules.industry.rest;

import com.yuxuan66.modules.industry.entity.query.MarketGroupQuery;
import com.yuxuan66.modules.industry.service.AnalysisService;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sir丶雨轩
 * @since 6/14/2022
 */
@RestController
@RequestMapping(path = "/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    /**
     * 查询市场分组
     *
     * @param marketGroupQuery 查询条件
     * @return 市场分组
     */
    @GetMapping(path = "/queryMarketGroup")
    public RespEntity queryMarketGroup(MarketGroupQuery marketGroupQuery) {
        return analysisService.queryMarketGroup(marketGroupQuery);
    }

    /**
     * 获取一个物品的详细信息
     *
     * @param id 物品id
     * @return 标准返回
     */
    @GetMapping(path = "/getTypeInfo")
    public RespEntity getTypeInfo(Integer id) {
        return analysisService.getTypeInfo(id);
    }
}
