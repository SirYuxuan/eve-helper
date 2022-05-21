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
package com.yuxuan66.common.utils;

import java.sql.Timestamp;

/**
 * 常用工具类
 * @author Sir丶雨轩
 * @since 2021/10/26
 */
public class Lang {


    /**
     * 获取当前的时间戳
     * @return 时间戳
     */
    public static Timestamp getNow(){
        return new Timestamp(System.currentTimeMillis());
    }
}
