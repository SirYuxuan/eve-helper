package com.yuxuan66.modules.database.mapper;

import com.yuxuan66.modules.database.entity.MetaGroup;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 2021/12/15
 */
@Mapper
public interface MetaGroupMapper extends BasicMapper<MetaGroup> {

    /**
     * 批量插入eve meta group
     * @param groupList meta group集合
     * @return 插入条数
     */
    long batchInsert(List<MetaGroup> groupList);
}
