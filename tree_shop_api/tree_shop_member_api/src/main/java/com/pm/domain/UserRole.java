package com.pm.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserRole implements Serializable {
    private Long id;

    private User user;

    private Role role;

}