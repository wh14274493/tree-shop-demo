package com.pm.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class Merchant implements Serializable {
    private Long id;

    private String name;

    private User user;

    private Timestamp createTime;

    private String detail;

}