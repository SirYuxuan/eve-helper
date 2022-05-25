package com.yuxuan66.modules.user.rest;

import com.yuxuan66.modules.user.entity.User;
import com.yuxuan66.modules.user.entity.dto.PhoneCode;
import com.yuxuan66.modules.user.entity.dto.UpdateEmail;
import com.yuxuan66.modules.user.entity.dto.UpdatePass;
import com.yuxuan66.modules.user.entity.query.UserQuery;
import com.yuxuan66.modules.user.service.UserService;
import com.yuxuan66.support.aop.log.annotation.Log;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

/**
 * @author Sir丶雨轩
 * @since 2021/6/23
 */
@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询用户列表
     *
     * @param userQuery 用户查询
     * @return 用户列表
     */
    @GetMapping
    public PageEntity list(UserQuery userQuery) {
        return userService.list(userQuery);
    }

    /**
     * 导出用户列表
     *
     * @param userQuery 查询条件
     * @throws IOException
     */
    @GetMapping(path = "/download")
    public void download(UserQuery userQuery) throws IOException {
        userService.download(userQuery);
    }

    /**
     * 修改用户
     *
     * @param resources 用户
     * @return 标准返回
     */
    @PutMapping
    public RespEntity update(@RequestBody User resources) {
        return userService.update(resources);
    }


    /**
     * 发送修改邮件的邮件
     *
     * @param mail 新邮件
     * @return 标准返回
     */
    @PutMapping(path = "/sendUpdateMail")
    public RespEntity sendUpdateMail(String mail) {
        return userService.sendUpdateMail(mail);
    }


    /**
     * 修改密码
     *
     * @param updatePass 修改密码
     * @return 标准返回
     */
    @Log("修改密码")
    @PutMapping(path = "/updatePass")
    public RespEntity updatePass(@RequestBody UpdatePass updatePass) {
        return userService.updatePass(updatePass);
    }

    /**
     * 修改用户手机号
     * @param phoneCode 参数
     * @return 标准返回
     */
    @Log("修改手机号")
    @PutMapping(path = "/updatePhone")
    public RespEntity updatePhone(@RequestBody PhoneCode phoneCode) {
        return userService.updatePhone(phoneCode);
    }

    /**
     * 发送短信验证码
     * @param phoneCode 参数
     * @return 标准返回
     */
    @PutMapping(path = "/sendPhoneCode")
    public RespEntity sendPhoneCode(@RequestBody PhoneCode phoneCode) throws Exception {
        return userService.sendPhoneCode(phoneCode);
    }

    /**
     * 修改用户邮箱
     * @param updateEmail 参数
     * @return 返回
     */
    @Log("修改邮箱")
    @PutMapping(path = "/updateEmail")
    public RespEntity updateEmail(@RequestBody UpdateEmail updateEmail) {
        return userService.updateEmail(updateEmail);
    }

    /**
     * 添加用户
     *
     * @param user 用户
     * @return 标准返回
     */
    @PostMapping
    public RespEntity add(@RequestBody User user) {
        return userService.add(user);
    }

    /**
     * 判断指定字段指定值的用户是否存在
     *
     * @param field 字段
     * @param data  值
     * @return 是否存在
     */
    @GetMapping(path = "/checkExist")
    public RespEntity checkExist(String field, String data, String type) {
        return userService.checkExist(field, data, type);
    }


    /**
     * 校验验证码是否正确
     * @param phoneCode 验证码
     * @return 标准返回
     */
    @GetMapping(path = "/checkPhoneCode")
    public RespEntity checkPhoneCode(PhoneCode phoneCode) {
        return userService.checkPhoneCode(phoneCode);
    }



    /**
     * 批量删除用户
     *
     * @param ids 用户id
     * @return 标准返回
     */
    @DeleteMapping
    public RespEntity del(@RequestBody Set<Long> ids) {
        return userService.del(ids);
    }

    /**
     * 获取当前登录用户详情，返回昵称，头像，权限组
     *
     * @return 昵称，头像，权限组
     */
    @GetMapping(path = "/info")
    public RespEntity info() {
        return userService.info();
    }


}
