package com.yuxuan66.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuxuan66.annotation.Mapper;
import com.yuxuan66.support.basic.model.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Setter
@Getter
@TableName("sys_users_roles")
@Mapper
public class UsersRoles extends BasicEntity implements Serializable {

    private Long userId;
    private Long roleId;
}
