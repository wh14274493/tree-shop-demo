package com.pm.common.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

public class TokenUtil {

    public static final String TOKEN = "token";

    public static String getToken(){
        return UUID.randomUUID().toString();
    }
}
