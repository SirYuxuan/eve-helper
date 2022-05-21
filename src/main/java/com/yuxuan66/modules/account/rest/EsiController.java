package com.yuxuan66.modules.account.rest;

import com.yuxuan66.modules.account.service.EsiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sir丶雨轩
 * @since 2022/1/5
 */
@Controller
@RequestMapping(path = "/esi")
@RequiredArgsConstructor
public class EsiController {

    private final EsiService esiService;

    /**
     * 前往授权
     *
     * @return 授权地址
     */
    @GetMapping(path = "/auth")
    public String auth(String token) {
        return "redirect:" + esiService.buildAuthPath(token);
    }

    /**
     * 授权回调
     *
     * @param code  esi code
     * @param state esi state
     * @return 前端地址
     */
    @GetMapping(path = "/callback")
    public String callback(String code, String state) throws InterruptedException {
        return "redirect:" + esiService.callback(code, state);
    }

}
