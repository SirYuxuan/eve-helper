/*
 * Copyright 2013-2021 Sir丶雨轩
 *
 * This file is part of Sir丶雨轩/eve-corp-api.

 * Sir丶雨轩/eve-corp-api is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.

 * Sir丶雨轩/eve-corp-api is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Sir丶雨轩/eve-corp-api.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.yuxuan66.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuxuan66.modules.user.entity.User;
import com.yuxuan66.modules.user.entity.query.UserQuery;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户操作
 *
 * @author Sir丶雨轩
 * @since 2021/9/1
 */
@Mapper
public interface UserMapper extends BasicMapper<User> {

    /**
     * 通过用户id查询用户详细信息，包含角色、菜单、部门、头像等
     *
     * @param userId 用户id
     * @return 用户详情
     */
    User findUserById(Long userId);

    /**
     * 分页查询用户列表
     *
     * @param userQuery 查询参数
     * @return 用户列表
     */
    List<User> listUser(@Param("query") UserQuery userQuery);

    /**
     * 计算用户总条数
     * @param userQuery 查询参数
     * @return 条数
     */
    long countUser(@Param("query") UserQuery userQuery);
}
