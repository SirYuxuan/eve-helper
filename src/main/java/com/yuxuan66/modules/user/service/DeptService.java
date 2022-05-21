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
import com.yuxuan66.common.utils.tree.TreeUtil;
import com.yuxuan66.modules.user.entity.Dept;
import com.yuxuan66.modules.user.entity.query.DeptQuery;
import com.yuxuan66.modules.user.mapper.DeptMapper;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author Sir丶雨轩
 * @since 2021/9/1
 */
@Service
public class DeptService {

    @Resource
    private DeptMapper deptMapper;


    /**
     * 查询部门列表
     *
     * @param deptQuery 查询参数
     * @return 部门列表
     */
    public RespEntity queryDeptList(DeptQuery deptQuery) {

        List<Dept> deptList = deptMapper.selectList(getQueryWrapper(deptQuery));
        for (Dept dept : deptList) {
            dept.setIsLeaf(!dept.getHasChildren());
        }
        return RespEntity.success(deptList);
    }

    /**
     * 查询部门树，根据父id，查询所有上级
     *
     * @param id 父id
     * @return 部门数
     */
    public RespEntity queryDeptTree(Long id) {
        Dept deptDto = deptMapper.selectById(id);
        List<Dept> deptList = getSuperior(deptDto, new ArrayList<>());
        return RespEntity.success(new TreeUtil().menuList(deptList));
    }

    /**
     * 根据ID获取同级与上级数据
     *
     * @param dept     部门
     * @param deptList 部门列表
     * @return 部门列表
     */
    private List<Dept> getSuperior(Dept dept, List<Dept> deptList) {

        if (dept.getPid() == 0) {
            deptList.addAll(deptMapper.selectList(new QueryWrapper<Dept>().eq("pid", 0)));
            return deptList;
        }
        deptList.addAll(deptMapper.selectList(new QueryWrapper<Dept>().eq("pid", dept.getPid())));

        return getSuperior(deptMapper.selectById(dept.getPid()), deptList);
    }

    /**
     * 转换查询条件为mybatis使用的
     *
     * @param deptQuery 查询条件
     * @return queryWrapper
     */
    private QueryWrapper<Dept> getQueryWrapper(DeptQuery deptQuery) {
        deptQuery.processingBlurry("name", "remake");
        QueryWrapper<Dept> queryWrapper = deptQuery.getQueryWrapper();
        queryWrapper.eq(deptQuery.getPid() != null, "pid", deptQuery.getPid());
        queryWrapper.eq(deptQuery.getEnabled() != null, "enabled", deptQuery.getEnabled());
        return queryWrapper;
    }

    /**
     * 查询部门列表
     *
     * @param deptQuery 查询参数
     * @return 标准返回
     */
    public PageEntity list(DeptQuery deptQuery) {
        if (deptQuery.getPid() == null) {
            deptQuery.setPid(0L);
        }
        List<Dept> deptList = deptMapper.selectList(getQueryWrapper(deptQuery));
        for (Dept dept : deptList) {
            dept.setIsLeaf(!dept.getHasChildren());
        }
        return PageEntity.success(deptList);
    }

    /**
     * 更新是否有子集
     * @param deptId 部门id
     */
    private void updateSubChildren(Long deptId) {
        if (deptId != null) {
            long count = deptMapper.selectCount(new QueryWrapper<Dept>().eq("pid", deptId));
            Dept dept = new Dept();
            dept.setId(deptId);
            dept.setHasChildren(count > 0);
            deptMapper.updateById(dept);
        }
    }

    /**
     * 编辑部门
     * @param resources
     * @return 标准返回
     */
    public RespEntity edit(Dept resources) {
        if(resources.getPid() == null){
            resources.setPid(0L);
        }
        Long oldPid = deptMapper.selectById(resources.getId()).getPid();
        Long newPid = resources.getPid();
        if (resources.getPid() != null && resources.getId().equals(resources.getPid())) {
            return RespEntity.error("上级不能为自己");
        }
        deptMapper.updateById(resources);
        // 更新父节点中子节点数目
        updateSubChildren(oldPid);
        updateSubChildren(newPid);
        return RespEntity.success();
    }


    /**
     * 添加一个部门
     * @param resources 部门
     * @return 标准返回
     */
    public RespEntity add(Dept resources) {
        if(resources.getPid() == null){
            resources.setPid(0L);
        }
        resources.setHasChildren(false);
        deptMapper.insert(resources);
        // 清理缓存
        updateSubChildren(resources.getPid());
        return RespEntity.success();
    }

    /**
     * 批量删除部门
     * @param ids 部门列表
     */
    public RespEntity delete(Set<Long> ids) {

        for (Dept deptDto : deptMapper.selectBatchIds(ids)) {
            // 清理缓存
            deptMapper.deleteById(deptDto.getId());
            updateSubChildren(deptDto.getPid());
        }
        return RespEntity.success();
    }


    /**
     * 导出部门
     * @param deptQuery 查询条件
     * @throws IOException io异常
     */
    public void download(DeptQuery deptQuery) throws IOException {

        List<Map<String, Object>> list = new ArrayList<>();
        deptQuery.setPid(null);
        List<Dept> deptList = deptMapper.selectList(getQueryWrapper(deptQuery));
        for (Dept dept : deptList) {
            dept.setIsLeaf(!dept.getHasChildren());
        }

        for (Dept deptDTO : deptList) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("部门名称", deptDTO.getName());
            map.put("部门状态", deptDTO.getEnabled() ? "启用" : "停用");
            map.put("创建日期", deptDTO.getCreateTime());
            list.add(map);
        }
        ExcelUtil.downloadExcel(list);
    }


}
