package com.pm.wx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pm.common.utils.ApiUtil;
import com.pm.common.utils.HttpClientUtil;
import com.pm.common.utils.RedisUtil;
import com.pm.common.utils.TokenUtil;
import com.pm.wx.domain.WxSessionModel;
import com.pm.wx.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class WxUserServiceImpl implements WxUserService {

    @Value("${wechat.url}")
    private String url;
    @Value("${wechat.appId}")
    private String appId;
    @Value("${wechat.secret}")
    private String secret;
    @Value("${wechat.grant_type}")
    private String grantTtype;
    @Autowired
    private ApiUtil apiUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Map wxLogin(String code) {
        log.info("获取微信端传递的code：" + code);
        Map<String, String> param = new HashMap<>();
        param.put("appId", appId);
        param.put("secret", secret);
        param.put("grant_type", grantTtype);
        param.put("js_code",code);
        String result = HttpClientUtil.doPost(url, param);
        log.info("result: "+result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if(jsonObject.containsKey("errcode")){
            return apiUtil.setErrorResult("登录认证失败！");
        }
        log.info("result: "+result);
        String token = TokenUtil.getToken();
        redisUtil.put(token, appId, secret);
        return apiUtil.setSuccessResult("登录认证成功！",token);
    }
}
