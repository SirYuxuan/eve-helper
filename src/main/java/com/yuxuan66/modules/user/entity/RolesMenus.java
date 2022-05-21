package com.yuxuan66.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuxuan66.support.basic.model.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@TableName("sys_roles_menus")
public class RolesMenus extends BasicEntity implements Serializable {

    private static final long serialVersionUID = -6781425321007691826L;
    private Long menuId;
    private Long roleId;
}
