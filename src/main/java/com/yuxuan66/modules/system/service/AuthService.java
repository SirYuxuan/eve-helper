package com.yuxuan66.modules.system.service;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.config.SystemConfig;
import com.yuxuan66.modules.system.entity.param.LoginParam;
import com.yuxuan66.modules.user.entity.User;
import com.yuxuan66.modules.user.mapper.UserMapper;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Auth授权相关服务
 *
 * @author Sir丶雨轩
 * @since 2021/06/17
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    @Resource
    private UserMapper userMapper;
    private final SystemConfig config;

    /**
     * 登录
     *
     * @param loginParam 账号密码
     * @return Token信息
     */
    public RespEntity login(LoginParam loginParam) {
        List<User> userList = userMapper.selectList(new QueryWrapper<User>().eq("username", loginParam.getUsername()));
        if (userList.isEmpty()) {
            return RespEntity.error("没有找到登录账户");
        }
        if (userList.size() != 1) {
            return RespEntity.error("账号数据异常");
        }

        User user = userList.get(0);

        RSA rsa = new RSA(config.getVal(CacheKey.Config.Admin.RSA_PRIVATE),config.getVal(CacheKey.Config.Admin.RSA_PUBLIC));
        String password = rsa.decryptStr(loginParam.getPassword(), KeyType.PrivateKey);
        String dbPassword = rsa.decryptStr(user.getPassword(), KeyType.PrivateKey);


        if (!Objects.equals(password, dbPassword)) {
            return RespEntity.error("账户密码输入错误");
        }
        if(!user.getEnabled()){
            return RespEntity.error("当前用户已被管理员停用，无法登录。");
        }

        TokenUtil.login(user.getId());
        TokenUtil.setUser(userMapper.findUserById(user.getId()));

        return RespEntity.success(new HashMap<String, String>(1) {{
            put(TokenUtil.getTokenInfo().getTokenName(), TokenUtil.getTokenInfo().getTokenValue());
        }});

    }
}
