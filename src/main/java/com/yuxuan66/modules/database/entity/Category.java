package com.yuxuan66.modules.database.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * EVE分类表(EveCategory)实体类
 *
 * @author Sir丶雨轩
 * @since 2021-12-15 15:43:59
 */
@Setter
@Getter
@TableName("eve_category")
public class Category implements Serializable {

    private Integer id;
    
    private String name;
    
    private String nameEn;
    
    private Timestamp createTime;


}

