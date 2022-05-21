package com.yuxuan66.modules.system.rest;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.yuxuan66.modules.system.service.MenuService;
import com.yuxuan66.modules.user.entity.Menu;
import com.yuxuan66.modules.user.entity.query.MenuQuery;
import com.yuxuan66.modules.user.entity.query.RoleQuery;
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
@RequestMapping(path = "/menu")
@SaCheckLogin
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 查询菜单列表
     *
     * @param menuQuery 查询参数
     * @return 菜单列表
     */
    @GetMapping
    public PageEntity list(MenuQuery menuQuery){
        return menuService.list(menuQuery);
    }

    /**
     * 添加一个新的菜单
     *
     * @param menu 菜单
     * @return 标准返回
     */
    @PostMapping
    public RespEntity add(@RequestBody Menu menu){
        return menuService.add(menu);
    }

    /**
     * 编辑一个菜单
     * @param menu 菜单
     * @return 标准返回
     */
    @PutMapping
    public RespEntity edit(@RequestBody Menu menu){
        return menuService.edit(menu);
    }

    /**
     * 批量删除菜单列表
     *
     * @param ids id列表
     * @return 标准返回
     */
    @DeleteMapping
    public RespEntity del(@RequestBody Set<Long> ids){
        return menuService.del(ids);
    }


    @GetMapping(path = "/build")
    public RespEntity build(){
        return menuService.build();
    }

    /**
     * 查询部门树，根据父id，查询所有上级
     * @param id 父id
     * @return 部门数
     */
    @GetMapping(path = "/queryMenuTree")
    public RespEntity queryMenuTree(Long id){
        return menuService.queryMenuTree(id);
    }
    /**
     * 获取自身及子集的id
     * @param id 菜单id
     * @return id列表
     */
    @GetMapping(path = "/child")
    public RespEntity child(Long id){
        return menuService.child(id);
    }

    /**
     * 导出菜单列表
     * @param menuQuery 查询条件
     * @throws IOException
     */
    @GetMapping(path = "/download")
    public void download(MenuQuery menuQuery) throws IOException {
        menuService.download(menuQuery);
    }
}
