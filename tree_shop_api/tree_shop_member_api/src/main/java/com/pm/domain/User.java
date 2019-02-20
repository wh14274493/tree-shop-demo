package com.pm.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class User implements Serializable{
    private Long id;

    private String account;

    private String password;

    private String userName;

    private String email;

    private String mobilephone;

    private String md5Param;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Integer isDelete;

    private List<Role> roleList;

}