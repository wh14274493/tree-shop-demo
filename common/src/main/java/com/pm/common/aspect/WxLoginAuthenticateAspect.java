package com.pm.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class WxLoginAuthenticateAspect {

    @Pointcut("execution(public * com.pm.wx.service.impl.*.*(..))")
    public void pointcut(){

    }

//    @Around("pointcut()")
//    public void around(){
//
//    }
}
