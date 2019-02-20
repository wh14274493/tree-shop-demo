package com.pm.wx.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WxSessionModel {

    private String sessionKey;
    private String openId;
}
