package com.yuxuan66.modules.system.rest;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.yuxuan66.modules.system.service.DictService;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sir丶雨轩
 * @since 2021/12/2
 */
@RestController
@RequestMapping(path = "/dict")
@SaCheckLogin
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;




    /**
     * 查询指定分组的字典列表
     * @param group 分组
     * @return 字典列表
     */
    @GetMapping(path = "/dictDetail")
    public RespEntity dictDetail(String group){
        return dictService.dictDetail(group);
    }
}
