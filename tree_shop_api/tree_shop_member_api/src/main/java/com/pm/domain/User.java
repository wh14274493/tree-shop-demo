package com.pm.domain;

import com.pm.common.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class User extends BaseDomain implements Serializable {

    private String account;
    private String password;
    private String username;
    private String phone;
    private String email;
    private String md5Param;
}
