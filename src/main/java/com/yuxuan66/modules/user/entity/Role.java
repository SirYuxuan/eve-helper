package com.yuxuan66.modules.user.entity;/*
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


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuxuan66.annotation.Mapper;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Setter
@Getter
@TableName("sys_role")
public class Role implements Serializable {

    private Long id;
    /**
     * 角色名
     */
    private String name;
    /**
     * 是否启用 0=未启用,1=启用
     */
    private Boolean enabled;
    /**
     * 角色备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 是否删除
     */
    private Integer isDel;

    /**
     * 菜单列表
     */
    @TableField(exist = false)
    private Set<Menu> menus;


}
