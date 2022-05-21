/*
 * Copyright 2013-2021 Sir丶雨轩
 *
 * This file is part of Sir丶雨轩/basic-admin.

 * Sir丶雨轩/basic-admin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.

 * Sir丶雨轩/basic-admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Sir丶雨轩/basic-admin.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.yuxuan66.modules.system.service;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.cache.RedisUtil;
import com.yuxuan66.modules.system.entity.Dict;
import com.yuxuan66.modules.system.mapper.DictMapper;
import com.yuxuan66.support.basic.model.RespEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sir丶雨轩
 * @since 2021/10/28
 */
@Service
public class DictService {

    @Resource
    private DictMapper dictMapper;
    private final RedisUtil redisUtil;

    public DictService(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 删除指定分组的缓存
     *
     * @param group 分组名
     */
    public void clearCacheGroup(String group) {
        if (redisUtil.hasKey(CacheKey.CACHE_SYS_DICT_GROUP + group)) {
            redisUtil.del(CacheKey.CACHE_SYS_DICT_GROUP + group);
        }
    }

    /**
     * 根据分组获取字典Map，使用缓存
     *
     * @param group 分组名字
     * @return 字典Map
     */
    public Map<String, Object> findByGroup(String group) {
        if (redisUtil.hasKey(CacheKey.CACHE_SYS_DICT_GROUP + group)) {
            return JSONObject.parseObject(Convert.toStr(redisUtil.get(CacheKey.CACHE_SYS_DICT_GROUP + group), "{}"));
        }
        List<Dict> dictList = dictMapper.selectList(new QueryWrapper<Dict>().eq("`group`", group));
        Map<String, Object> dictMap = new HashMap<>();
        for (Dict dict : dictList) {
            dictMap.put(dict.getLabel(), dict.getValue());
        }
        redisUtil.set(CacheKey.CACHE_SYS_DICT_GROUP + group, JSONObject.toJSONString(dictMap));
        return dictMap;
    }

    /**
     * 查询指定分组的字典列表
     * @param group 分组
     * @return 字典列表
     */
    public RespEntity dictDetail(String group){
        List<Dict> dictList = dictMapper.selectList(new QueryWrapper<Dict>().eq("`group`", group));
        return RespEntity.success(dictList);
    }
}
