package com.yuxuan66.modules.system.rest;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.yuxuan66.modules.system.entity.Log;
import com.yuxuan66.modules.system.service.LogService;
import com.yuxuan66.support.basic.BasicQuery;
import com.yuxuan66.support.basic.model.PageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sir丶雨轩
 * @since 2021/12/7
 */
@RestController
@RequestMapping(path = "/log")
@RequiredArgsConstructor
@SaCheckLogin
public class LogController {

    private final LogService logService;

    /**
     * 分页查询我的操作日志
     * @param logBasicQuery 查询条件
     * @return 查询日志列表
     */
    @GetMapping(path = "/myLogList")
    private PageEntity myLogList(BasicQuery<Log> logBasicQuery) {
        return logService.myLogList(logBasicQuery);
    }
}
