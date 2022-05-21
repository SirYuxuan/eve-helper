package com.yuxuan66.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuxuan66.support.basic.model.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Setter
@Getter
@TableName("sys_users_roles")
public class UsersRoles extends BasicEntity implements Serializable {

    private static final long serialVersionUID = -6307949042129238647L;
    private Long userId;
    private Long roleId;
}
