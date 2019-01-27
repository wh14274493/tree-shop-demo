package com.pm.service;

import com.alibaba.fastjson.JSONObject;
import com.pm.adapter.MessageAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailService implements MessageAdapter {

    @Value("${spring.mail.username}")
    private String mailServer;

    @Autowired
    private JavaMailSender mailSender; // 自动注入的Bean

    @Override
    public void consumeMessage(JSONObject jsonObject) {
        log.info("成功获取邮件队列发送的消息： " + jsonObject.toJSONString());
        String email = jsonObject.getString("email");
        String userName = jsonObject.getString("userName");
        SimpleMailMessage mimeMessage = new SimpleMailMessage();
        mimeMessage.setFrom(mailServer);
        mimeMessage.setTo(email);
        mimeMessage.setSubject("成功注册****会员");
        mimeMessage.setText("恭喜您！"+userName+"成功注册****平台会员");
        log.info("邮箱地址： "+email+" 邮件开始发送...");
        mailSender.send(mimeMessage);
        log.info("邮箱地址： "+email+" 邮件发送成功！");
    }
}
