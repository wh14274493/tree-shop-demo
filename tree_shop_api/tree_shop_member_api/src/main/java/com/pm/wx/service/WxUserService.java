package com.pm.wx.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RequestMapping("/wx/user")
public interface WxUserService {
    @GetMapping("/login")
    Map wxLogin(@RequestParam String code);
}
