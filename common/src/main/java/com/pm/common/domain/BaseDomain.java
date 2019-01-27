package com.pm.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * 实体类基类，用来封装相同的属性
 */
@Getter
@Setter
public class BaseDomain {

    /**
     * 主键
     */
    private Long id;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 更新时间
     */
    private Timestamp updateTime;
    /**
     * 是否删除
     */
    private Integer isDelete;

}
