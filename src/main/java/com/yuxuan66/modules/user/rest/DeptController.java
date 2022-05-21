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
package com.yuxuan66.modules.user.rest;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.yuxuan66.modules.user.entity.Dept;
import com.yuxuan66.modules.user.entity.Menu;
import com.yuxuan66.modules.user.entity.query.DeptQuery;
import com.yuxuan66.modules.user.entity.query.MenuQuery;
import com.yuxuan66.modules.user.service.DeptService;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

/**
 * 部门管理
 * @author Sir丶雨轩
 * @since 2021/9/1
 */
@RequestMapping(path = "/dept")
@RestController
@RequiredArgsConstructor
@SaCheckLogin
public class DeptController {

    private final DeptService deptService;

    /**
     * 查询部门列表
     * @param deptQuery 查询参数
     * @return 部门列表
     */
    @GetMapping(path = "/queryDeptList")
    public RespEntity queryDeptList(DeptQuery deptQuery){
        return deptService.queryDeptList(deptQuery);
    }


    /**
     * 查询部门树，根据父id，查询所有上级
     * @param id 父id
     * @return 部门数
     */
    @GetMapping(path = "/queryDeptTree")
    public RespEntity queryDeptTree(Long id){
        return deptService.queryDeptTree(id);
    }

    /**
     * 查询部门列表
     * @param deptQuery 查询参数
     * @return 标准返回
     */
    @GetMapping
    public PageEntity list(DeptQuery deptQuery){
        return deptService.list(deptQuery);
    }


    /**
     * 添加一个新的部门
     *
     * @param dept 部门
     * @return 标准返回
     */
    @PostMapping
    public RespEntity add(@RequestBody Dept dept){
        return deptService.add(dept);
    }

    /**
     * 编辑一个部门
     * @param dept 部门
     * @return 标准返回
     */
    @PutMapping
    public RespEntity edit(@RequestBody Dept dept){
        return deptService.edit(dept);
    }

    /**
     * 批量删除部门列表
     *
     * @param deptSet 部门列表
     * @return 标准返回
     */
    @DeleteMapping
    public RespEntity del(@RequestBody Set<Long> deptSet){
        return deptService.delete(deptSet);
    }
    /**
     * 导出部门列表
     * @param deptQuery 查询条件
     * @throws IOException
     */
    @GetMapping(path = "/download")
    public void download(DeptQuery deptQuery) throws IOException {
        deptService.download(deptQuery);
    }

}
