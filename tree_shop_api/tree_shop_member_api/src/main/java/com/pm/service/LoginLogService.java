package com.pm.service;

import com.pm.common.utils.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/log")
public interface LoginLogService {

    @RequestMapping("/login")
    public Map getLoginLog();

}
