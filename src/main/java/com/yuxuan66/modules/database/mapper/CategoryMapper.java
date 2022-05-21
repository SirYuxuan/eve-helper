package com.yuxuan66.modules.database.mapper;

import com.yuxuan66.modules.database.entity.Category;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 2021/12/15
 */
@Mapper
public interface CategoryMapper extends BasicMapper<Category> {

    /**
     * 批量插入eve Category数据
     * @param categoryList Category集合
     * @return 插入条数
     */
    long batchInsert(List<Category> categoryList);
}
