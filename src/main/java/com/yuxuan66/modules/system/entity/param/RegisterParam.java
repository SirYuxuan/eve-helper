package com.yuxuan66.modules.system.entity.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sir丶雨轩
 * @since 2022/5/25
 */
@Setter
@Getter
public class RegisterParam {
    private String username;
    private String phone;
    private String code;
    private String uuid;
    private String phoneCode;
    private String password;
}
