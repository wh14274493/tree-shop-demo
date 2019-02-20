package com.pm.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Good implements Serializable {
    private Long id;

    private Merchant merchant;

    private String name;

    private String detail;

    private Integer state;

}