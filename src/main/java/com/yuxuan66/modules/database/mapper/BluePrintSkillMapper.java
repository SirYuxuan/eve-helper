package com.yuxuan66.modules.database.mapper;

import com.yuxuan66.modules.database.entity.BluePrintSkill;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 2021/12/16
 */
@Mapper
public interface BluePrintSkillMapper extends BasicMapper<BluePrintSkill> {

    /**
     * 批量插入
     * @param list 数据
     * @return 插入条数
     */
    long batchInsert(List<BluePrintSkill> list);
}
