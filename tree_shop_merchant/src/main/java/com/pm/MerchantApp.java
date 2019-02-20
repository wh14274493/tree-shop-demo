package com.pm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.pm.mapper"})
public class MerchantApp {

    public static void main(String[] args) {
        SpringApplication.run(MerchantApp.class, args);
    }
}
