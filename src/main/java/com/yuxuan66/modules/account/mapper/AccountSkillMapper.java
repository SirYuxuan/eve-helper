package com.yuxuan66.modules.account.mapper;

import com.yuxuan66.modules.account.entity.AccountSkill;
import com.yuxuan66.support.basic.BasicMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountSkillMapper extends BasicMapper<AccountSkill> {


    /**
     * 批量插入
     * @param list 数据
     * @return 插入条数
     */
    long batchInsert(List<AccountSkill> list);

}
