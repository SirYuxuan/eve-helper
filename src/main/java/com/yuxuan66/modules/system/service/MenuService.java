package com.yuxuan66.modules.system.service;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.common.utils.ExcelUtil;
import com.yuxuan66.common.utils.FileUtil;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.common.utils.tree.TreeUtil;
import com.yuxuan66.modules.user.entity.Dept;
import com.yuxuan66.modules.user.entity.Menu;
import com.yuxuan66.modules.user.entity.MenuMapper;
import com.yuxuan66.modules.user.entity.query.MenuQuery;
import com.yuxuan66.modules.user.mapper.UserMapper;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sir丶雨轩
 * @since 2021/6/23
 */
@Service
public class MenuService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private MenuMapper menuMapper;

    /**
     * 查询菜单列表
     *
     * @param menuQuery 查询参数
     * @return 菜单列表
     */
    public PageEntity list(MenuQuery menuQuery) {

        if (menuQuery.getAll()) {
            return PageEntity.success(new TreeUtil().menuList(menuMapper.selectList(null)));
        }

        List<Menu> menuList = menuMapper.selectList(getQueryWrapper(menuQuery));
        for (Menu menu : menuList) {
            menu.setHasChildrenData(!menu.getHasChildren());
        }
        return PageEntity.success(menuList);
    }

    /**
     * 添加一个新的菜单
     *
     * @param menu 菜单
     * @return 标准返回
     */
    public RespEntity add(Menu menu) {

        if (menu.getPid() == 0) {
            menu.setPid(0L);
        }
        menu.setHasChildren(false);
        if (menu.getType() == 0 && !menu.getIsLink()) {
            menu.setComponent("Layout");
        }
        if (menu.getType() == 0 && !menu.getPath().startsWith("/")) {
            menu.setPath("/" + menu.getPath());
        }
        menuMapper.insert(menu);
        if (Convert.toLong(menu.getPid(), 0L) > 0) {
            // 给这个ID所在的数据修改子集
            long count = menuMapper.selectCount(new QueryWrapper<Menu>().eq("pid", menu.getPid()));
            Menu resource = new Menu();
            resource.setId(menu.getPid());
            resource.setHasChildren(count > 0);
            menuMapper.updateById(resource);
        }
        return RespEntity.success();
    }

    /**
     * 批量删除菜单列表
     *
     * @param ids id列表
     * @return 标准返回
     */
    public RespEntity del(Set<Long> ids) {

        for (Long id : ids) {
            Menu menu = menuMapper.selectById(id);
            menuMapper.deleteById(id);
            handleParentMenu(menu);

            menuMapper.delete(new QueryWrapper<Menu>().eq("pid", id));
        }
        return RespEntity.success();
    }

    /**
     * 编辑一个菜单
     *
     * @param resource 菜单
     * @return 标准返回
     */
    public RespEntity edit(Menu resource) {

        if (resource.getId().equals(resource.getPid())) {
            return RespEntity.error("不允许父级为自己");
        }

        Menu oldMenu = menuMapper.selectById(resource.getId());

        menuMapper.updateById(resource);

        handleParentMenu(resource);
        handleParentMenu(oldMenu);

        return RespEntity.success();
    }

    /**
     * 处理父级id的是否有子集
     *
     * @param menu 父级id
     */
    public void handleParentMenu(Menu menu) {

        if (menu.getPid() != null) {
            Menu resource = new Menu();
            resource.setId(menu.getPid());
            resource.setHasChildren(menuMapper.selectCount(new QueryWrapper<Menu>().eq("pid", menu.getPid())) > 0);
            menuMapper.updateById(resource);
        }
    }

    /**
     * 构建左侧菜单
     * 移除掉所有按钮级菜单
     *
     * @return 菜单
     */
    public RespEntity build() {
        TreeUtil menuTreeUtil = new TreeUtil();
        List<Menu> menuList = userMapper.findUserById(TokenUtil.getUserId()).getMenus();
        return RespEntity.success(menuTreeUtil.menuList(menuList.stream().filter(item -> item.getType() != 2).collect(Collectors.toList())));
    }

    /**
     * 查询菜单树，根据父id，查询所有上级
     *
     * @param id 父id
     * @return 菜单树
     */
    public RespEntity queryMenuTree(Long id) {
        Menu deptDto = menuMapper.selectById(id);
        List<Menu> deptList = getSuperior(deptDto, new ArrayList<>());
        return RespEntity.success(new TreeUtil().menuList(deptList));
    }

    /**
     * 根据ID获取同级与上级数据
     *
     * @param menu     菜单
     * @param menuList 菜单列表
     * @return 菜单列表
     */
    private List<Menu> getSuperior(Menu menu, List<Menu> menuList) {

        if (menu.getPid() == 0) {
            menuList.addAll(menuMapper.selectList(new QueryWrapper<Menu>().eq("pid", 0)));
            return menuList;
        }
        menuList.addAll(menuMapper.selectList(new QueryWrapper<Menu>().eq("pid", menu.getPid())));

        return getSuperior(menuMapper.selectById(menu.getPid()), menuList);
    }

    /**
     * 递归获取所有子菜单
     *
     * @param menuList 菜单列表
     * @param menuSet  菜单集合
     * @return 菜单集合
     */
    private Set<Menu> getChildMenus(List<Menu> menuList, Set<Menu> menuSet) {
        for (Menu menu : menuList) {
            menuSet.add(menu);
            List<Menu> menus = menuMapper.selectList(new QueryWrapper<Menu>().eq("pid", menu.getId()));
            if (menus != null && menus.size() != 0) {
                getChildMenus(menus, menuSet);
            }
        }
        return menuSet;
    }

    /**
     * 获取指定pid下的菜单
     *
     * @param pid pid
     * @return 菜单
     */
    public List<Menu> getMenus(Long pid) {
        List<Menu> menus;
        if (pid != null && !pid.equals(0L)) {
            menus = menuMapper.selectList(new QueryWrapper<Menu>().eq("pid", pid));
        } else {
            menus = menuMapper.selectList(new QueryWrapper<Menu>().eq("pid", 0));
        }
        return menus;
    }

    /**
     * 获取自身及子集的id
     *
     * @param id 菜单id
     * @return id列表
     */
    public RespEntity child(Long id) {
        Set<Menu> menuSet = new HashSet<>();
        List<Menu> menuList = getMenus(id);
        menuSet.add(menuMapper.selectById(id));
        menuSet = getChildMenus(menuList, menuSet);
        Set<Long> ids = menuSet.stream().map(Menu::getId).collect(Collectors.toSet());
        return RespEntity.success(ids);
    }

    /**
     * 转换查询条件为mybatis使用的
     *
     * @param menuQuery 查询条件
     * @return queryWrapper
     */
    private QueryWrapper<Menu> getQueryWrapper(MenuQuery menuQuery) {
        menuQuery.processingBlurry("title", "icon", "permission_str", "path", "name", "component", "badge");
        QueryWrapper<Menu> queryWrapper = menuQuery.getQueryWrapper();
        if (!menuQuery.getAll()) {
            queryWrapper.eq("pid", menuQuery.getPid());
        }

        if (menuQuery.getCreateTime() != null && menuQuery.getCreateTime().length > 0) {
            queryWrapper.ge("create_time", menuQuery.getCreateTime()[0] + " 00:00:00");
            queryWrapper.le("create_time", menuQuery.getCreateTime()[1] + " 23:59:59");
        }
        return queryWrapper;
    }

    /**
     * 导出菜单
     *
     * @param menuQuery 查询条件
     * @throws IOException io异常
     */
    public void download(MenuQuery menuQuery) throws IOException {

        menuQuery.setAll(true);
        List<Menu> menuList = menuMapper.selectList(getQueryWrapper(menuQuery));

        List<Map<String, Object>> list = new ArrayList<>();
        for (Menu menuDTO : menuList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("菜单标题", menuDTO.getTitle());
            map.put("菜单类型", menuDTO.getType() == 0 ? "目录" : menuDTO.getType() == 1 ? "菜单" : "按钮");
            map.put("权限标识", menuDTO.getPermissionStr());
            map.put("外链菜单", menuDTO.getIsLink() ? "是" : "否");
            map.put("菜单可见", menuDTO.getHidden() ? "否" : "是");
            map.put("是否缓存", menuDTO.getNoCache() == null ? "否" : menuDTO.getNoCache() ? "否" : "是");
            map.put("创建日期", menuDTO.getCreateTime());
            list.add(map);
        }
        ExcelUtil.downloadExcel(list);
    }
}
