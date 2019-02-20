package com.pm.service;

import com.pm.domain.Merchant;
import com.pm.domain.MerchantApplyRecord;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RequestMapping("/service/merchant")
public interface MerchantService {
    @PostMapping("/apply")
    Map apply(@RequestBody MerchantApplyRecord merchantApplyRecord, String token);

    @PostMapping("/web/check")
    Map check(@RequestParam long merchantApplyRecordId,@RequestParam int state);

}
