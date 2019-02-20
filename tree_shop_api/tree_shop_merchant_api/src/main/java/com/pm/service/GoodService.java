package com.pm.service;

import com.pm.domain.Good;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/service/good")
public interface GoodService {
    @RequestMapping("/add")
    Map add(Good good);
}
