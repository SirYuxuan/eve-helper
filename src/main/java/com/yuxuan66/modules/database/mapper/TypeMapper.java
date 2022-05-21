package com.yuxuan66.modules.database.mapper;

import com.yuxuan66.modules.database.entity.Type;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 2021/12/15
 */
@Mapper
public interface TypeMapper extends BasicMapper<Type> {

    /**
     * 批量插入eve type数据
     * @param typeList type集合
     * @return 插入条数
     */
    long batchInsert(List<Type> typeList);
}
