package com.pm.service.impl;

import com.netflix.discovery.converters.Auto;
import com.pm.common.utils.ApiUtil;
import com.pm.domain.LoginLog;
import com.pm.mapper.LoginLogMapper;
import com.pm.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private ApiUtil apiUtil;
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public Map getLoginLog() {
        List<LoginLog> loginLogList = loginLogMapper.getAll();
        return apiUtil.setSuccessResult(loginLogList);
    }
}
