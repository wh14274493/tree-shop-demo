package com.pm.mapper;

import com.pm.domain.LoginLog;

import java.util.List;

public interface LoginLogMapper {
    int insert(LoginLog record);

    List<LoginLog> getAll();

}