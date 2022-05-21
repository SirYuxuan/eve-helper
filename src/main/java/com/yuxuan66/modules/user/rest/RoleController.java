package com.yuxuan66.modules.user.rest;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.yuxuan66.modules.user.entity.Role;
import com.yuxuan66.modules.user.entity.query.RoleQuery;
import com.yuxuan66.modules.user.entity.query.UserQuery;
import com.yuxuan66.modules.user.service.RoleService;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

/**
 * @author Sir丶雨轩
 * @since 2021/6/23
 */
@RestController
@SaCheckLogin
@RequestMapping(path = "/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 查询角色列表
     *
     * @param roleQuery 查询参数
     * @return 角色列表
     */
    @GetMapping
    public PageEntity list(RoleQuery roleQuery){
        return roleService.list(roleQuery);
    }


    /**
     * 添加一个新的角色
     * @param role 角色
     * @return 标准返回
     */
    @PostMapping
    public RespEntity add(@RequestBody Role role){
        return roleService.add(role);
    }

    /**
     * 修改一个角色
     * @param role 角色
     * @return 标准返回
     */
    @PutMapping
    public RespEntity edit(@RequestBody Role role){
        return roleService.edit(role);
    }
    /**
     * 删除一组角色
     * @param ids 角色id
     * @return 标准返回
     */
    @DeleteMapping
    public RespEntity del(@RequestBody Set<Long> ids){
        return roleService.del(ids);
    }

    /**
     * 查询所有已经启用的角色
     * @return 角色列表
     */
    @GetMapping(path = "/queryAllRole")
    public RespEntity queryAllRole(){
        return roleService.queryAllRole();
    }

    /**
     * 编辑一个角色的菜单
     * @param role 角色信息->菜单
     * @return 标准返回
     */
    @PutMapping(path = "/editMenu")
    public RespEntity editMenu(@RequestBody Role role){
        return roleService.editMenu(role);
    }


    /**
     * 导出角色列表
     * @param roleQuery 查询条件
     * @throws IOException
     */
    @GetMapping(path = "/download")
    public void download(RoleQuery roleQuery) throws IOException {
        roleService.download(roleQuery);
    }
}
