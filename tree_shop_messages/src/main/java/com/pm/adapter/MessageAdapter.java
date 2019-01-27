package com.pm.adapter;

import com.alibaba.fastjson.JSONObject;

public interface MessageAdapter {

    void consumeMessage(JSONObject jsonObject);
}
