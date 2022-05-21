package com.yuxuan66.support.basic.model;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口分页数据统一
 * @author Sir丶雨轩
 * @since 2021/6/24
 */
public class PageEntity extends Dict {


    /**
     * 状态码
     */
    public static final String TOTAL = "totalElements";


    /**
     * 数据对象
     */
    public static final String DATA_TAG = "content";

    /**
     * 初始化一个新创建的 RespEntity 对象，使其表示一个空消息。
     */
    public PageEntity() {
    }

    /**
     * 初始化一个新创建的 PageEntity 对象
     *
     * @param total   条数
     * @param content 返回内容
     */
    public PageEntity(long total, List<?> content) {
        super.put("code",200);
        Map<String,Object> data = new LinkedHashMap<>();
        data.put(TOTAL, total);
        data.put(DATA_TAG, content);
        super.put("data",data);
    }


    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static PageEntity success(List<?> content) {
        return success(content.size(), content);
    }
    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static PageEntity success(int count,List<?> content) {
        return new PageEntity(count, content);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static PageEntity success(long count,List<?> content) {
        return new PageEntity(count, content);
    }
    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static PageEntity success(IPage<?> page) {
        return new PageEntity((int) page.getTotal(), page.getRecords());
    }
}
