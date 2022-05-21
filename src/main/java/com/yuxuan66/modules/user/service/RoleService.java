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
package com.yuxuan66.modules.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.common.utils.ExcelUtil;
import com.yuxuan66.common.utils.Lang;
import com.yuxuan66.modules.user.entity.Menu;
import com.yuxuan66.modules.user.entity.Role;
import com.yuxuan66.modules.user.entity.RolesMenus;
import com.yuxuan66.modules.user.entity.query.RoleQuery;
import com.yuxuan66.modules.user.mapper.RoleMapper;
import com.yuxuan66.modules.user.mapper.RolesMenusMapper;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 角色管理
 * @author Sir丶雨轩
 * @since 2021/9/2
 */
@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RolesMenusMapper rolesMenusMapper;
    /**
     * 查询角色列表
     *
     * @param roleQuery 查询参数
     * @return 角色列表
     */
    public PageEntity list(RoleQuery roleQuery) {
        return PageEntity.success(roleMapper.countRole(roleQuery),roleMapper.listRole(roleQuery));
    }

    /**
     * 查询所有已经启用的角色
     * @return 角色列表
     */
    public RespEntity queryAllRole(){
        return RespEntity.success(roleMapper.selectList(new QueryWrapper<Role>().eq("enabled",true)));
    }

    /**
     * 编辑一个角色的菜单
     * @param role 角色信息->菜单
     * @return 标准返回
     */
    public RespEntity editMenu(Role role){
        rolesMenusMapper.delete(new QueryWrapper<RolesMenus>().eq("role_id",role.getId()));
        for (Menu menu : role.getMenus()) {
            if(menu.getId() != null){
                RolesMenus rolesMenus = new RolesMenus();
                rolesMenus.setRoleId(role.getId());
                rolesMenus.setMenuId(menu.getId());
                rolesMenusMapper.insert(rolesMenus);
            }
        }
        return RespEntity.success();
    }

    /**
     * 添加一个新的角色
     * @param role 角色
     * @return 标准返回
     */
    public RespEntity add(Role role){
        role.setCreateTime(Lang.getNow());
        role.setEnabled(true);
        roleMapper.insert(role);
        return RespEntity.success();
    }

    /**
     * 修改一个角色
     * @param role 角色
     * @return 标准返回
     */
    public RespEntity edit(Role role){
        roleMapper.updateById(role);
        return RespEntity.success();
    }

    /**
     * 删除一组角色
     * @param ids 角色id
     * @return 标准返回
     */
    public RespEntity del(Set<Long> ids){

        roleMapper.deleteBatchIds(ids);
        rolesMenusMapper.delete(new QueryWrapper<RolesMenus>().in("role_id",ids));

        return RespEntity.success();
    }

    /**
     * 导出角色列表
     * @param roleQuery 查询条件
     * @throws IOException io异常
     */
    public void download(RoleQuery roleQuery) throws IOException {

        List<Role> roleList = roleMapper.listRole(roleQuery);

        List<Map<String, Object>> list = new ArrayList<>();
        for (Role role : roleList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("描述", role.getRemark());
            map.put("创建日期", role.getCreateTime());
            list.add(map);
        }
        ExcelUtil.downloadExcel(list);
    }
}
