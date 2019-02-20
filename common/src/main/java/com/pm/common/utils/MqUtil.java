package com.pm.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.pm.common.constants.IntefaceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Component
@Slf4j
public class MqUtil {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private RedisUtil redisUtil;

    public void send(Queue queue, String message) {
        log.info("开始往消息队列发送消息...");
        jmsMessagingTemplate.convertAndSend(queue, message);
        log.info("成功发送消息息： " + message);
    }

    /**
     * 注册成功时获取发送邮件内容
     *
     * @param email
     * @param userName
     * @return
     */
    public String getEmailMessage(String email, String userName) {
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject content = new JSONObject();
        header.put("intefaceType", IntefaceType.SMS_EMAIL);
        content.put("email", email);
        content.put("userName", userName);
        root.put("header", header);
        root.put("content", content);
        return root.toJSONString();
    }

    /**
     * 向消息队列发送邮件通知
     *
     * @param email    发送邮件地址
     * @param userName 发送的用户名
     * @param queue    发送的消息队列
     */
    public void sendEmailToQueue(String email, String userName, Queue queue) {
        String message = getEmailMessage(email, userName);
        send(queue, message);
        //TODO keyi存储到redis中保存发送记录
        String hashKey = DateUtil.currentFormatDate(DateUtil.DATE_TO_STRING_SHORT_PATTERN) + IntefaceType.SMS_EMAIL + "produce";
        log.info("hashKey: " + hashKey);
        redisUtil.rightPush(hashKey, message);
    }
}
