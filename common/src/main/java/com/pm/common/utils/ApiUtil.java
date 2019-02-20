package com.pm.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.pm.common.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class ApiUtil {

    /**
     * m默认成功调用返回
     *
     * @return
     */
    public Map<String, Object> setSuccessResult() {
        return setResult(Constant.SUCCESS_MSG, Constant.SUCCESS_CODE, null);
    }

    /**
     * 自定义数据的成功调用返回
     *
     * @param data
     * @return
     */
    public Map<String, Object> setSuccessResult(Object data) {
        return setResult(Constant.SUCCESS_MSG, Constant.SUCCESS_CODE, data);
    }

    /**
     * 自定义返回信息的成功调用返回
     *
     * @param msg 返回的message
     * @return
     */
    public Map<String, Object> setSuccessResult(String msg) {
        return setResult(msg, Constant.SUCCESS_CODE, null);
    }

    /**
     * 自定义返回信息和数据的成功调用返回
     *
     * @param msg  返回的message
     * @param data 返回的数据
     * @return
     */
    public Map<String, Object> setSuccessResult(String msg, Object data) {
        return setResult(msg, Constant.SUCCESS_CODE, data);
    }

    public Map<String, Object> setErrorResult(String msg) {
        return setResult(msg, Constant.ERROR_500_CODE, null);
    }


    /**
     * 设置调用接口返回内容
     *
     * @param msg  返回的message
     * @param code 返回的状态码code
     * @param data 返回的数据
     * @return
     */
    public Map<String, Object> setResult(String msg, String code, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.CODE, code);
        result.put(Constant.MSG, msg);
        result.put(Constant.DATA, data);
        return result;
    }

    /**
     * 获取返回内容的data数据
     *
     * @param result
     * @param cls
     * @return
     */
    public <T> T getData(Map result, Class cls) {
        LinkedHashMap data = (LinkedHashMap) result.get(Constant.DATA);
        String jsonObject = JSONObject.toJSONString(data);
        log.info("jsonObject： " + jsonObject);
        return (T) JSONObject.parseObject(jsonObject, cls);
    }

    /**
     * 将错误信息存到request中，并返回转发的页面路径
     * @param request
     * @param url
     * @param errorMsg
     * @return
     */
    public String setErrorMsg(HttpServletRequest request, String url, String errorMsg) {
        request.setAttribute("error", errorMsg);
        return url;
    }
}
