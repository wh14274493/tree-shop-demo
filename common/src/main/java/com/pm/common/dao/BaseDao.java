package com.pm.common.dao;

import com.pm.common.utils.SqlUtil;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BaseDao<T> {

    @InsertProvider(type = SqlUtil.class,method = "insert")
    void insert(@Param("obj") Object obj,@Param("table") String table);

    @DeleteProvider(type = SqlUtil.class,method = "delete")
    void delete(@Param("id") Long id,@Param("table") String table);

    @UpdateProvider(type = SqlUtil.class,method = "update")
    void update(@Param("obj") Object object,@Param("table") String table);

    @SelectProvider(type = SqlUtil.class,method = "findById")
    T findById(@Param("id") Long id,@Param("table") String table);

    @SelectProvider(type = SqlUtil.class,method = "findAll")
    List<T> findAll(@Param("table") String table);
}
