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
import com.yuxuan66.common.utils.tree.TreeField;
import com.yuxuan66.common.utils.tree.TreeId;
import com.yuxuan66.common.utils.tree.TreeParent;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Mapper
@Setter
@Getter
@TableName("sys_menu")
public class Menu implements Serializable {

    @TreeId
    @TreeField
    private Long id;
    /**
    * 菜单名称
    */
    @TreeField(value = "label", secondLevel = "meta")
    private String title;

    @TreeField(value = "title", like = "title")
    @TableField(exist = false)
    private String treeTitle;
    /**
    * 父级ID
    */
    @TreeParent
    private Long pid;
    /**
    * 菜单类型 0=目录,1=菜单,2=按钮
    */
    @TreeField
    private Integer type;
    /**
    * 菜单图标
    */
    @TreeField(secondLevel = "meta")
    private String icon;

    /**
    * 是否外链
    */
    @TreeField
    private Boolean isLink;
    /**
    * 是否缓存
    */
    @TreeField(value = "noCache")
    private Boolean noCache;
    /**
    * 是否可见
    */
    @TreeField("hidden")
    private Boolean hidden;
    /**
    * 权限字符串
    */
    private String permissionStr;
    /**
    * 路由地址
    */
    @TreeField
    private String path;
    /**
    * 组件名称
    */
    @TreeField
    private String name;
    /**
    * 组件地址
    */
    @TreeField("component")
    private String component;
    /**
    * 排序号
    */
    private Integer sort;
    /**
    * 是否有徽章
    */
    @TreeField(secondLevel = "meta")
    private String badge;
    /**
    * 重定向地址
    */
    private String redirect;
    @TableField(exist = false)
    @TreeField(value = "hasChildrenData",like = "hasChildren")
    private Boolean hasChildrenData;
    /**
    * 创建人ID
    */
    private Long createId;
    /**
    * 创建时间
    */
    private Timestamp createTime;
    /**
    * 更新人ID
    */
    private Long updateId;
    /**
    * 更新时间
    */
    private Timestamp updateTime;
    /**
    * 是否删除
    */
    private Boolean isDel;
    /**
     * 是否有子集
     */
    private Boolean hasChildren;


}
