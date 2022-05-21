package com.yuxuan66.common.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import com.yuxuan66.modules.user.entity.User;
import lombok.SneakyThrows;

/**
 * 扩展{@link StpUtil} <br/>
 * 提供针对本项目的快捷方法
 * @author Sir丶雨轩
 * @since 2021/6/23
 */
public class TokenUtil extends StpUtil {


    private final static String KEY_USER = "token_user";

    /**
     * 保存当前登录用户对象
     * @param user 用户对象
     */
    public static void setUser(User user) {
        getSession().set(KEY_USER, user);
    }

    /**
     * 获取当前登录用户对象
     * @return 用户对象
     */
    public static User getUser() {
        return getSession().getModel(KEY_USER, User.class);
    }

    /**
     * 获取当前登录用户id
     * @return 用户id
     */
    public static Long getUserId(){
        return Convert.toLong(getLoginId());
    }


    /**
     * 延时指定时间退出
     * @param id 用户id
     * @param time 时间
     */
    public static void sleepLogout(Long id,Long time){
        new Thread(() -> {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TokenUtil.login(id);
        }).start();
    }


}
