package com.yuxuan66.modules.system.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.cache.RedisUtil;
import com.yuxuan66.config.SystemConfig;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sir丶雨轩
 * @since 2021/12/9
 */
@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final RedisUtil redisUtil;
    private final SystemConfig config;


    /**
     * 生成验证码
     *
     * @return 结果
     */
    public RespEntity captchaImage() {

        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果
        String result = captcha.text();
        String uuid = IdUtil.simpleUUID();
        // 保存
        Map<String, Object> imgResult = new HashMap<>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};

        redisUtil.hset(CacheKey.CAPTCHA_CODE,uuid,result, Convert.toLong(config.getVal(CacheKey.Config.Admin.IMG_CODE_INVALID_TIME)));

        return RespEntity.success(imgResult);
    }
}
