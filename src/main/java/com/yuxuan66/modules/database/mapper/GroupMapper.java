package com.yuxuan66.modules.database.mapper;

import com.yuxuan66.modules.database.entity.Group;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 2021/12/15
 */
@Mapper
public interface GroupMapper extends BasicMapper<Group> {

    /**
     * 批量插入eve group
     * @param groupList group集合
     * @return 插入条数
     */
    long batchInsert(List<Group> groupList);
}
