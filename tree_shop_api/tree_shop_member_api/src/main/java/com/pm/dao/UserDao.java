package com.pm.dao;

import com.pm.common.dao.BaseDao;
import com.pm.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseDao<User> {
}
