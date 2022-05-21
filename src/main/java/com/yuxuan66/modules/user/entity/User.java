/*
 * Copyright 2013-2021 Sir丶雨轩
 *
 * This file is part of Sir丶雨轩/basic-admin.

 * Sir丶雨轩/basic-admin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.

 * Sir丶雨轩/basic-admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Sir丶雨轩/basic-admin.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.yuxuan66.modules.user.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuxuan66.support.basic.model.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@TableName("sys_user")
public class User extends BasicEntity implements Serializable {

    private Long id;
    /**
    * 用户名
    */
    private String username;
    /**
    * 密码
    */
    private String password;
    /**
    * 用户昵称
    */
    private String nickname;
    /**
    * 部门ID
    */
    private Long deptId;
    /**
    * 岗位ID
    */
    private Long postId;
    /**
    * 是否启用
    */
    private Boolean enabled;
    /**
    * 手机号
    */
    private String phone;
    /**
    * 邮箱
    */
    private String email;
    /**
    * 性别 0=女,1=男,2=未知
    */
    private Integer sex;

    /**
    * 创建时间
    */
    private Timestamp createTime;
    /**
    * 是否删除
    */
    private Boolean isDel;

    /**
     * 头像数据
     */
    private String avatar;

    /**
     * 用户所属角色列表
     */
    @TableField(exist = false)
    private Set<Role> roles;

    /**
     * 菜单列表
     */
    @TableField(exist = false)
    private List<Menu> menus;

    /**
     * 部门
     */
    @TableField(exist = false)
    private Dept dept;


}
