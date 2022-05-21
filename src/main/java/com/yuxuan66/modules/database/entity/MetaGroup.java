package com.yuxuan66.modules.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * EVE元分组表(EveMetaGroup)实体类
 *
 * @author Sir丶雨轩
 * @since 2021-12-15 16:24:06
 */
@Setter
@Getter
@TableName("eve_meta_group")
public class MetaGroup implements Serializable {
    /**
     * 元分组ID
     */
    private Integer id;
    /**
     * 元分组名称
     */
    private String name;
    /**
     * 元分组名称英文
     */
    private String nameEn;
    /**
     * 创建时间
     */
    private Timestamp createTime;


}

