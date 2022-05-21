package com.yuxuan66.support.basic.model;

import cn.hutool.core.lang.Dict;

import java.util.Objects;

/**
 * 接口返回数据统一
 * @author Sir丶雨轩
 * @since 2021/6/23
 */
public class RespEntity extends Dict {
    
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 RespEntity 对象，使其表示一个空消息。
     */
    public RespEntity() {
    }

    /**
     * 初始化一个新创建的 RespEntity 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public RespEntity(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 RespEntity 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public RespEntity(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (Objects.nonNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static RespEntity success() {
        return RespEntity.successMsg("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static RespEntity success(Object data) {
        return RespEntity.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static RespEntity successMsg(String msg) {
        return RespEntity.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static RespEntity success(String msg, Object data) {
        return new RespEntity(HttpCode.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static RespEntity error() {
        return RespEntity.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static RespEntity error(String msg) {
        return RespEntity.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static RespEntity error(String msg, Object data) {
        return new RespEntity(HttpCode.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static RespEntity error(int code, String msg) {
        return new RespEntity(code, msg, null);
    }

    /**
     * 返回没有登录的信息
     *
     * @return 警告消息
     */
    public static RespEntity notLogin(String msg) {
        return new RespEntity(HttpCode.FORBIDDEN, msg, null);
    }
}
