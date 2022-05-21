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


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@TableName("sys_attach")
public class Attach implements Serializable {

    private static final long serialVersionUID = -637704174630942282L;
    private Long id;
    /**
    * 附件类型 1=URL,2=本地,3=OSS,4=七牛,5=...
    */
    private Integer type;
    /**
    * 具体内容
    */
    private String content;
    /**
    * 创建时间
    */
    private Timestamp createTime;
    /**
    * 访问URL
    */
    private String url;
    /**
    * 是否删除
    */
    private Integer isDel;


}
