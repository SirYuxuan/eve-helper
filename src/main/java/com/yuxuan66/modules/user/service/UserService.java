package com.yuxuan66.modules.user.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.cache.RedisUtil;
import com.yuxuan66.common.utils.ExcelUtil;
import com.yuxuan66.common.utils.Lang;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.config.SystemConfig;
import com.yuxuan66.modules.user.entity.*;
import com.yuxuan66.modules.user.entity.dto.PhoneCode;
import com.yuxuan66.modules.user.entity.dto.UpdateEmail;
import com.yuxuan66.modules.user.entity.dto.UpdatePass;
import com.yuxuan66.modules.user.entity.query.UserQuery;
import com.yuxuan66.modules.user.mapper.UserMapper;
import com.yuxuan66.support.aop.log.annotation.Log;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import com.yuxuan66.support.mail.MailHelper;
import com.yuxuan66.support.rsa.RsaKit;
import lombok.RequiredArgsConstructor;
import okhttp3.Cache;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sir丶雨轩
 * @since 2021/6/23
 */
@Service
@RequiredArgsConstructor
public class UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private UsersRolesMapper usersRolesMapper;

    private final MailHelper mailHelper;
    private final SystemConfig config;
    private final RedisUtil redisUtil;

    /**
     * 发送修改邮件的邮件
     *
     * @param mail 新邮件
     * @return 标准返回
     */
    public RespEntity sendUpdateMail(String mail) {

        long count = userMapper.selectCount(new QueryWrapper<User>().eq("email", mail).ne("id", TokenUtil.getUserId()));
        if (count > 0) {
            return RespEntity.error("当前邮箱已被别人使用。");
        }

        String code = mailHelper.sendUpdateUserMail(mail);
        redisUtil.hset(CacheKey.MAIL_CODE, mail, code, Convert.toLong(config.getVal(CacheKey.Config.Mail.MAIL_CODE_INVALID_TIME)));
        return RespEntity.success();
    }

    /**
     * 分页查询用户列表
     *
     * @param userQuery 用户查询
     * @return 用户列表
     */
    public PageEntity list(UserQuery userQuery) {
        setSearchQuery(userQuery);

        userQuery.setLimitStart((userQuery.getPage().getCurrent() - 1) * userQuery.getPage().getSize());
        return PageEntity.success(userMapper.countUser(userQuery), userMapper.listUser(userQuery));
    }

    /**
     * 获取当前登录用户详情，返回昵称，头像，权限组
     *
     * @return 昵称，头像，权限组
     */
    public RespEntity info() {
        Map<String, Object> result = new HashMap<>(4);

        User user = TokenUtil.getUser();

        user = userMapper.findUserById(user.getId());
        result.put("id", user.getId());
        result.put("nickname", user.getNickname());
        result.put("username", user.getUsername());
        result.put("phone", user.getPhone());
        result.put("email", user.getEmail());
        result.put("sex", user.getSex());
        if (user.getDept() != null) {
            result.put("deptName", user.getDept().getName());
        }

        result.put("avatar", user.getAvatar());

        Set<String> permissions = new HashSet<>();

        if (user.getMenus().isEmpty()) {
            permissions.add("NONE");
            result.put("permissions", permissions);
        } else {
            result.put("permissions", user.getMenus().stream().map(Menu::getPermissionStr).filter(StrUtil::isNotBlank).collect(Collectors.toList()));
        }

        return RespEntity.success(result);
    }


    /**
     * 修改用户
     *
     * @param resources 用户
     * @return 标准返回
     */
    @Log("编辑用户")
    public RespEntity update(User resources) {
        userLegitimacyVerification(resources);

        userMapper.updateById(resources);

        if (resources.getRoles() != null) {
            // 删除角色
            usersRolesMapper.delete(new QueryWrapper<UsersRoles>().eq("user_id", resources.getId()));
            for (Role role : resources.getRoles()) {
                UsersRoles usersRoles = new UsersRoles();
                usersRoles.setUserId(resources.getId());
                usersRoles.setRoleId(role.getId());
                usersRolesMapper.insert(usersRoles);
            }
        }


        return RespEntity.success();
    }

    /**
     * 用户合法性校验
     *
     * @param user 用户合法性校验
     */
    private void userLegitimacyVerification(User user) {
        // 校验手机号是否存在
        long count = userMapper.selectCount(new QueryWrapper<User>().eq("phone", user.getPhone()).ne(user.getId() != null, "id", user.getId()));
        if (count > 0) {
            throw new RuntimeException("当前手机号已经存在");
        }
        count = userMapper.selectCount(new QueryWrapper<User>().eq("username", user.getUsername()).ne(user.getId() != null, "id", user.getId()));
        if (count > 0) {
            throw new RuntimeException("当前用户名已经存在");
        }
        count = userMapper.selectCount(new QueryWrapper<User>().eq("email", user.getEmail()).ne(user.getId() != null, "id", user.getId()));
        if (count > 0) {
            throw new RuntimeException("当前邮箱已经存在");
        }
    }


    /**
     * 处理查询条件
     *
     * @param userQuery 查询条件
     */
    private void setSearchQuery(UserQuery userQuery) {
        if (userQuery.getDeptId() != null && !userQuery.getDeptId().equals(0L)) {
            Set<Long> deptIds = new HashSet<>();
            deptIds.add(userQuery.getDeptId());
            List<Dept> deptList = deptMapper.selectList(new QueryWrapper<Dept>().eq("pid", userQuery.getDeptId()));
            while (!deptList.isEmpty()) {
                List<Long> tempDeptIds = deptList.stream().map(Dept::getId).collect(Collectors.toList());
                deptIds.addAll(tempDeptIds);
                deptList = deptMapper.selectList(new QueryWrapper<Dept>().in("pid", tempDeptIds));
            }

            userQuery.setDeptIds(deptIds);
        }
    }

    /**
     * 批量删除用户
     *
     * @param ids 用户id
     * @return 标准返回
     */
    @Log("删除用户")
    public RespEntity del(Set<Long> ids) {
        userMapper.deleteBatchIds(ids);
        usersRolesMapper.delete(new QueryWrapper<UsersRoles>().in("user_id", ids));
        return RespEntity.success();
    }

    /**
     * 添加一个新用户
     *
     * @param user 用户
     * @return 标准返回
     */
    @Log("添加用户")
    public RespEntity add(User user) {
        userLegitimacyVerification(user);

        user.setCreateTime(Lang.getNow());
        user.setAvatar(config.getVal(CacheKey.Config.Admin.USER_DEFAULT_AVATAR));
        RSA rsa = new RSA(config.getVal(CacheKey.Config.Admin.RSA_PRIVATE), config.getVal(CacheKey.Config.Admin.RSA_PUBLIC));
        // 手机号后六位作为密码
        user.setPassword(rsa.encryptBase64(StrUtil.subSuf(user.getPhone(), 5), KeyType.PublicKey));
        userMapper.insert(user);
        for (Role role : user.getRoles()) {
            UsersRoles usersRoles = new UsersRoles();
            usersRoles.setUserId(user.getId());
            usersRoles.setRoleId(role.getId());
            usersRolesMapper.insert(usersRoles);
        }
        return RespEntity.success();
    }

    /**
     * 修改用户邮箱
     * @param updateEmail 参数
     * @return 返回
     */
    public RespEntity updateEmail(UpdateEmail updateEmail) {
        if(!redisUtil.hHasKey(CacheKey.MAIL_CODE,updateEmail.getEmail())){
            return RespEntity.error("验证码输入错误");
        }
        String cacheCode = Convert.toStr(redisUtil.hget(CacheKey.MAIL_CODE,updateEmail.getEmail()));
        if(cacheCode.equalsIgnoreCase(updateEmail.getCode())){

            RSA rsa = RsaKit.getRSA();
            User user = userMapper.selectById(TokenUtil.getUserId());
            String password = rsa.decryptStr(updateEmail.getPass(), KeyType.PrivateKey);
            String dbPassword = rsa.decryptStr(user.getPassword(), KeyType.PrivateKey);

            if (!Objects.equals(password, dbPassword)) {
                return RespEntity.error("用户密码输入错误");
            }

            user.setEmail(updateEmail.getEmail());
            userMapper.updateById(user);
            redisUtil.hdel(CacheKey.MAIL_CODE,updateEmail.getEmail());
            return RespEntity.success();

        }


        return RespEntity.error("验证码输入错误");
    }

    /**
     * 修改用户手机号
     * @param phoneCode 参数
     * @return 标准返回
     */
    public RespEntity updatePhone(PhoneCode phoneCode){
        // 判断当前手机号没有被别人占用,哪怕是自己也不行
        long count = userMapper.selectCount(new QueryWrapper<User>().eq("phone",phoneCode.getCode()));
        if(count > 0){
            return RespEntity.error("对不起 此手机号已被使用");
        }

        if(redisUtil.hHasKey(CacheKey.PHONE_CODE,phoneCode.getPhone()) && phoneCode.getCode().equals(redisUtil.hget(CacheKey.PHONE_CODE,phoneCode.getPhone()))){
            // 修改用户新手机
            User user = userMapper.selectById(TokenUtil.getUserId());
            user.setPhone(phoneCode.getPhone());
            userMapper.updateById(user);
            return RespEntity.success();
        }

        return RespEntity.error("短信验证码输入错误");

    }


    /**
     * 修改密码
     *
     * @param updatePass 修改密码
     * @return 标准返回
     */
    public RespEntity updatePass(UpdatePass updatePass) {

        User user = userMapper.selectById(TokenUtil.getUserId());


        RSA rsa = RsaKit.getRSA();
        String oldPassword = rsa.decryptStr(updatePass.getOldPass() ,KeyType.PrivateKey);
        String dbPassword = rsa.decryptStr(user.getPassword(), KeyType.PrivateKey);


        if (!Objects.equals(oldPassword, dbPassword)) {
            return RespEntity.error("旧密码输入错误");
        }

        if (updatePass.getOldPass().equals(updatePass.getNewPass())) {
            return RespEntity.error("新旧密码一致。");
        }

        user.setPassword(rsa.encryptBase64(updatePass.getNewPass(), KeyType.PublicKey));
        userMapper.updateById(user);
        TokenUtil.sleepLogout(user.getId(),1500L);
        return RespEntity.success();
    }

    /**
     * 发送短信验证码
     * @param phoneCode 参数
     * @return 标准返回
     */
    public RespEntity sendPhoneCode(PhoneCode phoneCode){

        if(redisUtil.hHasKey(CacheKey.CAPTCHA_CODE,phoneCode.getUuid()) && phoneCode.getCode().equals(redisUtil.hget(CacheKey.CAPTCHA_CODE,phoneCode.getUuid()))){
            redisUtil.hdel(CacheKey.CAPTCHA_CODE,phoneCode.getUuid());
            int code = RandomUtil.randomInt(1000,9999);
            redisUtil.hset(CacheKey.PHONE_CODE, phoneCode.getPhone(),Convert.toStr(code),Convert.toLong(config.getVal(CacheKey.Config.Phone.PHONE_CODE_INVALID_TIME)));
            return RespEntity.success();
        }

        return RespEntity.error("验证码错误");

    }

    /**
     * 校验验证码是否正确
     * @param phoneCode 验证码
     * @return 标准返回
     */
    public RespEntity checkPhoneCode(PhoneCode phoneCode){
        return RespEntity.success(redisUtil.hHasKey(CacheKey.PHONE_CODE, phoneCode.getPhone()) && phoneCode.getCode().equals(redisUtil.hget(CacheKey.PHONE_CODE,phoneCode.getPhone())));
    }


    /**
     * 判断指定字段指定值的用户是否存在
     *
     * @param field 字段
     * @param data  值
     * @return 是否存在
     */
    public RespEntity checkExist(String field, String data, String type) {
        return RespEntity.success(userMapper.selectCount(new QueryWrapper<User>().eq(field, data).ne("edit".equals(type), "id", TokenUtil.getUserId())) > 0);
    }


    /**
     * 导出用户列表
     *
     * @param userQuery 查询条件
     * @throws IOException
     */
    public void download(UserQuery userQuery) throws IOException {

        setSearchQuery(userQuery);
        userQuery.setSize(-1);

        List<User> userList = userMapper.listUser(userQuery);

        List<Map<String, Object>> list = new ArrayList<>();
        for (User userDTO : userList) {
            List<String> roles = userDTO.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", userDTO.getUsername());
            map.put("角色", roles);
            map.put("部门", userDTO.getDept().getName());
            map.put("邮箱", userDTO.getEmail());
            map.put("状态", userDTO.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", userDTO.getPhone());
            map.put("创建日期", userDTO.getCreateTime());
            list.add(map);
        }
        ExcelUtil.downloadExcel(list);
    }

}
