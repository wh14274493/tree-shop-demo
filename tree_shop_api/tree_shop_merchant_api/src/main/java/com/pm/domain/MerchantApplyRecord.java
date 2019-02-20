package com.pm.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MerchantApplyRecord {
    private Long id;

    private User applyUser;

    private Date applyTime;

    private Date checkTime;

    private String merchantName;

    private String merchantDetail;

    private Integer state;

}