package com.pm.mq.listener;

import com.alibaba.fastjson.JSONObject;
import com.pm.adapter.MessageAdapter;
import com.pm.common.constants.IntefaceType;
import com.pm.common.utils.DateUtil;
import com.pm.common.utils.RedisUtil;
import com.pm.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListener {

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisUtil redisUtil;

    @JmsListener(destination = "mail_queue")
    public void messageDistribute(String json) {
        if (StringUtils.isEmpty(json)) {
            log.info("当前取得队列的消息为空！");
            return;
        }
        JSONObject root = JSONObject.parseObject(json);
        JSONObject header = root.getJSONObject("header");
        String intefaceType = header.getString("intefaceType");
        MessageAdapter messageAdapter = null;
        switch (intefaceType) {
            case IntefaceType.SMS_EMAIL:
                messageAdapter = mailService;
            default:
                break;
        }
        if (null == messageAdapter) {
            log.info("沒找到适配的消息服务器！");
            return;
        }
        JSONObject content = root.getJSONObject("content");
        messageAdapter.consumeMessage(content);
        // TODO 可以存到redis中保存消费记录
        String hashKey = DateUtil.currentFormatDate(DateUtil.DATE_TO_STRING_SHORT_PATTERN) + IntefaceType.SMS_EMAIL + "consume";
        redisUtil.rightPush(hashKey, json);
    }
}
