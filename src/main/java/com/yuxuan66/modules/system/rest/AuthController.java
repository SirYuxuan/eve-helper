package com.yuxuan66.modules.system.rest;

import cn.dev33.satoken.stp.StpUtil;
import com.yuxuan66.modules.system.entity.param.LoginParam;
import com.yuxuan66.modules.system.entity.param.RegisterParam;
import com.yuxuan66.modules.system.service.AuthService;
import com.yuxuan66.support.aop.log.LogType;
import com.yuxuan66.support.aop.log.annotation.Log;
import com.yuxuan66.support.basic.model.RespEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Sir丶雨轩
 * @since 2021/6/23
 */
@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 登录系统
     * @param loginParam 登录参数
     * @return 标准返回
     */
    @Log(type = LogType.LOGIN,value = "用户登录")
    @PostMapping(path = "/login")
    public RespEntity login(@RequestBody LoginParam loginParam){
        return authService.login(loginParam);
    }

    /**
     * 用户注册
     * @param registerParam 注册参数
     * @return 标准返回
     */
    @Log(type = LogType.LOGIN,value = "用户注册")
    @PostMapping(path = "/register")
    public RespEntity register(@RequestBody RegisterParam registerParam){
        return authService.register(registerParam);
    }

    /**
     * 退出登录
     * @return 标准返回
     */
    @PostMapping(path = "/logout")
    public RespEntity logout(){
        StpUtil.logout();
        return RespEntity.success();
    }
}
