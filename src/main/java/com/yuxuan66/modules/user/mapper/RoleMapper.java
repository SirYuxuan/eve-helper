package com.yuxuan66.modules.user.mapper;

import com.yuxuan66.modules.user.entity.Role;
import com.yuxuan66.modules.user.entity.User;
import com.yuxuan66.modules.user.entity.query.RoleQuery;
import com.yuxuan66.modules.user.entity.query.UserQuery;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 2021/11/25
 */
@Mapper
public interface RoleMapper extends BasicMapper<Role> {

    /**
     * 分页查询角色列表
     *
     * @param roleQuery 查询参数
     * @return 用户列表
     */
    List<Role> listRole(@Param("query") RoleQuery roleQuery);

    /**
     * 计算角色总条数
     * @param roleQuery 查询参数
     * @return 条数
     */
    long countRole(@Param("query") RoleQuery roleQuery);
}
