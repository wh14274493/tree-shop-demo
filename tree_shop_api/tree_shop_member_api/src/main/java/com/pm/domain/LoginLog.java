package com.pm.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class LoginLog {
    private Long id;

    private String ip;

    private Integer port;

    private Timestamp loginTime;

    private User loginUser;

}