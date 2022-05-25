package com.yuxuan66.modules.system.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.cache.RedisUtil;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.config.SystemConfig;
import com.yuxuan66.modules.system.entity.param.LoginParam;
import com.yuxuan66.modules.system.entity.param.RegisterParam;
import com.yuxuan66.modules.user.entity.User;
import com.yuxuan66.modules.user.entity.UsersRoles;
import com.yuxuan66.modules.user.mapper.UserMapper;
import com.yuxuan66.modules.user.mapper.UsersRolesMapper;
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
    @Resource
    private UsersRolesMapper usersRolesMapper;
    private final SystemConfig config;
    private final RedisUtil redis;

    /**
     * 用户注册
     * @param registerParam 注册参数
     * @return 标准返回
     */
    public RespEntity register(RegisterParam registerParam) {
        String code = Convert.toStr(redis.hget(CacheKey.CAPTCHA_CODE ,registerParam.getUuid()));
        redis.del(registerParam.getUuid());

        if (StrUtil.isBlank(registerParam.getCode()) || !registerParam.getCode().equalsIgnoreCase(code)) {
            return RespEntity.error("图形验证码错误");
        }
       /* String phoneCode = Convert.toStr(redis.hget(CacheKey.PHONE_CODE ,registerParam.getPhone()));
        // 手机验证码检测
        if (StrUtil.isBlank(registerParam.getPhoneCode()) || !registerParam.getPhoneCode().equalsIgnoreCase(phoneCode)) {
            return RespEntity.error("手机号验证码错误");
        }*/
        // 判断用户名和手机号是否存在
        long count = userMapper.selectCount(new QueryWrapper<User>().eq("username", registerParam.getUsername()));
        if (count > 0) {
            return RespEntity.error("用户名已被注册");
        }
    /*    count = userMapper.selectCount(new QueryWrapper<User>().eq("phone", registerParam.getPhone()));

        if(count > 0 ){
            return RespEntity.error("手机号已被注册");
        }*/

        User user = new User();
        user.setUsername(registerParam.getUsername());
        user.setNickname(registerParam.getUsername());
        user.setPhone(registerParam.getPhone());
        user.setDeptId(1L);
        user.setPostId(1L);
        RSA rsa = new RSA(config.getVal(CacheKey.Config.Admin.RSA_PRIVATE), config.getVal(CacheKey.Config.Admin.RSA_PUBLIC));
        String password = rsa.encryptBase64(registerParam.getPassword(), KeyType.PublicKey);
        user.setPassword(password);
        user.setEnabled(true);
        user.setSex(1);
        user.setIsDel(false);
        user.setAvatar("https://cdn.base.yuxuan66.com/yuxuan/photo/shop-eve.png");
        userMapper.insert(user);

        UsersRoles usersRoles = new UsersRoles();
        usersRoles.setUserId(user.getId());
        usersRoles.setRoleId(2L);
        usersRolesMapper.insert(usersRoles);

        return RespEntity.success();
    }


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

        RSA rsa = new RSA(config.getVal(CacheKey.Config.Admin.RSA_PRIVATE), config.getVal(CacheKey.Config.Admin.RSA_PUBLIC));
        String password = rsa.decryptStr(loginParam.getPassword(), KeyType.PrivateKey);
        String dbPassword = rsa.decryptStr(user.getPassword(), KeyType.PrivateKey);


        if (!Objects.equals(password, dbPassword)) {
            return RespEntity.error("账户密码输入错误");
        }
        if (!user.getEnabled()) {
            return RespEntity.error("当前用户已被管理员停用，无法登录。");
        }

        TokenUtil.login(user.getId());
        TokenUtil.setUser(userMapper.findUserById(user.getId()));

        return RespEntity.success(new HashMap<String, String>(1) {{
            put(TokenUtil.getTokenInfo().getTokenName(), TokenUtil.getTokenInfo().getTokenValue());
        }});

    }
}
