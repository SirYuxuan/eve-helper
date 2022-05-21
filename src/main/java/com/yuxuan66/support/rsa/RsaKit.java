package com.yuxuan66.support.rsa;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.common.utils.SpringUtil;
import com.yuxuan66.config.SystemConfig;
import com.yuxuan66.support.basic.model.RespEntity;

import java.util.Objects;

/**
 * RSA工具
 *
 * @author Sir丶雨轩
 * @since 2021/12/9
 */
public class RsaKit {

    public static SystemConfig config;

    /**
     * 获取RSA操作对象
     * @return RSA操作对象
     */
    public static RSA getRSA() {
        if (config == null) {
            config = SpringUtil.getBean(SystemConfig.class);
        }
        return new RSA(config.getVal(CacheKey.Config.Admin.RSA_PRIVATE), config.getVal(CacheKey.Config.Admin.RSA_PUBLIC));
    }
}
