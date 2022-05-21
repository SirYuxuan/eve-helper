package com.yuxuan66.modules.system.rest;

import com.yuxuan66.modules.system.service.CaptchaService;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sir丶雨轩
 * @since 2021/12/9
 */
@RestController
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    /**
     * 生成验证码
     *
     * @return 结果
     */
    @GetMapping(path = "/captchaImage")
    public RespEntity captchaImage(){
        return captchaService.captchaImage();
    }
}
