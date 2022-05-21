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
 * along with Sir丶雨轩/eve-corp-api.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.yuxuan66.modules.user.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuxuan66.common.utils.tree.TreeField;
import com.yuxuan66.common.utils.tree.TreeId;
import com.yuxuan66.common.utils.tree.TreeParent;
import com.yuxuan66.support.basic.model.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@TableName("sys_dept")
public class Dept extends BasicEntity implements Serializable {

    private static final long serialVersionUID = -8374503037007506717L;
    @TreeId
    @TreeField
    private Long id;
    /**
    * 部门名称
    */
    @TreeField
    private String name;
    /**
    * 部门备注
    */
    private String remake;
    /**
    * 父级部门
    */
    @TreeParent
    private Long pid;
    /**
    * 是否启用 0=未启用,1=启用
    */
    private Boolean enabled;
    /**
    * 是否有子集
    */
    private Boolean hasChildren;

    @TableField(exist = false)
    @TreeField(value = "hasChildrenData",like = "hasChildren")
    private Integer hasChildrenData;

    @TableField(exist = false)
    private Boolean isLeaf;
    /**
    * 排序号
    */
    private Integer sort;
    /**
    * 是否删除
    */
    private Integer isDel;
    /**
    * 创建时间
    */
    private Timestamp createTime;



}
