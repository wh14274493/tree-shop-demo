package com.pm.controller;

import com.alibaba.fastjson.JSONObject;
import com.pm.common.constants.Constant;
import com.pm.common.utils.ApiUtil;
import com.pm.common.utils.TokenUtil;
import com.pm.domain.User;
import com.pm.feign.UserFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private static final String REGISTER = "register";
    private static final String LOGIN = "login";
    private static final String INDEX = "index";
    @Autowired
    private UserFeign userFeign;
    @Autowired
    private ApiUtil apiUtil;

    @GetMapping("/get/currentuser")
    public String getCurrentUser(@RequestParam String token, HttpServletRequest request) {
        log.info("获取token： " + token);
        Map result = userFeign.getCurrentUser(token);
        String code = (String) result.get(Constant.CODE);
        if (!Constant.SUCCESS_CODE.equals(code)) {
            String errorMsg = (String) result.get(Constant.MSG);
            return apiUtil.setErrorMsg(request, REGISTER, errorMsg);
        }
        LinkedHashMap data = (LinkedHashMap) result.get(Constant.DATA);
        String jsonObject = JSONObject.toJSONString(data);
        log.info("jsonObject： " + jsonObject);
        User user = JSONObject.parseObject(jsonObject, User.class);
        return REGISTER;
    }

    @RequestMapping("/2register")
    public String toRegister() {
        return REGISTER;
    }

    @RequestMapping("/2login")
    public String toLogin() {
        return LOGIN;
    }

    @PostMapping("/register")
    public String register(User user, HttpServletRequest request) {
        Map result = userFeign.register(user);
        String code = (String) result.get(Constant.CODE);
        if (!Constant.SUCCESS_CODE.equals(code)) {
            String errorMsg = (String) result.get(Constant.MSG);
            return apiUtil.setErrorMsg(request, REGISTER, errorMsg);
        }
        return LOGIN;
    }

    @PostMapping("/login")
    public String login(User user, HttpServletRequest request) {
        Map result = userFeign.login(user);
        String code = (String) result.get(Constant.CODE);
        if (!Constant.SUCCESS_CODE.equals(code)) {
            String errorMsg = (String) result.get(Constant.MSG);
            return apiUtil.setErrorMsg(request, LOGIN, errorMsg);
        }
        String token = (String) result.get(Constant.DATA);
        log.info("token： " + token);
        request.setAttribute(TokenUtil.TOKEN, token);
        return INDEX;
    }

}
