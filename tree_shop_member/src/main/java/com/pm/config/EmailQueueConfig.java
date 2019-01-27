package com.pm.config;

import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

@Configuration
public class EmailQueueConfig {
    @Value("${email_queue}")
    private String email_queue;
    @Bean
    public Queue getQueue(){
        return new ActiveMQQueue(email_queue);
    }
}
