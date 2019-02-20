package com.pm.common.aspect;

import com.pm.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(public * com.pm.service.impl.*.*(..))")
    public void pointcut(){

    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("===============请求内容开始===============");
        log.info("time: " + DateUtil.getTimestamp());
        log.info("ip: " + request.getRemoteAddr());
        log.info("port: " + request.getRemotePort());
        log.info("http_method: " + request.getMethod());
        log.info("class_method: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        log.info("===============请求内容结束===============");
    }

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {

        log.info("===============返回结果开始===============");
        log.info("time: " + DateUtil.getTimestamp());
        log.info("class_method: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("result: " + result);
        log.info("===============返回结果结束===============");
    }
}
