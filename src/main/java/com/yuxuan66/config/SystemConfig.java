/*
 * Copyright 2013-2021 Sir丶雨轩
 *
 * This file is part of Sir丶雨轩/eve-corp-api.

 * Sir丶雨轩/eve-corp-api is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.

 * Sir丶雨轩/eve-corp-api is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Sir丶雨轩/eve-corp-api.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.yuxuan66.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.cache.RedisUtil;
import com.yuxuan66.modules.system.entity.Config;
import com.yuxuan66.modules.system.entity.ConfigMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统配置类，对应数据库sys_config表
 *
 * @author Sir丶雨轩
 * @since 2021/7/27
 */
@Component
@RequiredArgsConstructor
public class SystemConfig {

    @Resource
    private ConfigMapper configMapper;

    private final RedisUtil redisUtil;

    /**
     * 获取指定配置的值
     * @return 值
     */
    public String getVal(String key){
        if(redisUtil.hasKey(CacheKey.CACHE_SYS_CONFIG_MAP) && redisUtil.hHasKey(CacheKey.CACHE_SYS_CONFIG_MAP,key)){
            return Convert.toStr(redisUtil.hget(CacheKey.CACHE_SYS_CONFIG_MAP,key));
        }

        List<Config> configList = configMapper.selectList(new QueryWrapper<Config>().eq("name",key));
        if(configList.isEmpty()){
            return StrUtil.EMPTY;
        }

        String val = configList.get(0).getValue();

        if(val == null){
            return StrUtil.EMPTY;
        }
        redisUtil.hset(CacheKey.CACHE_SYS_CONFIG_MAP,key,val);

        return val;
    }

}
