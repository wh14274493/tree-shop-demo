package com.pm.service.impl;

import com.pm.common.utils.ApiUtil;
import com.pm.common.utils.DateUtil;
import com.pm.common.utils.RedisUtil;
import com.pm.domain.MerchantApplyRecord;
import com.pm.domain.User;
import com.pm.mapper.MerchantApplyRecordMapper;
import com.pm.mapper.UserMapper;
import com.pm.service.MerchantService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MerchantServiceImpl implements MerchantService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MerchantApplyRecordMapper merchantApplyRecordMapper;

    @Autowired
    private ApiUtil apiUtil;

    @Autowired
    private RedisUtil redisUtil;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map apply(MerchantApplyRecord merchantApplyRecord,String token) {

        merchantApplyRecord.setApplyTime(DateUtil.getTimestamp());
        merchantApplyRecord.setState(3);
        String userId = redisUtil.getString(token);
        if(StringUtils.isEmpty(userId)){
            return apiUtil.setSuccessResult("当前用户不能为空！");
        }
        User currentUser = userMapper.getById(Long.parseLong(userId));
        merchantApplyRecord.setApplyUser(currentUser);
        merchantApplyRecordMapper.insert(merchantApplyRecord);
        return apiUtil.setSuccessResult("申请成功，正在审核！",merchantApplyRecord.getId());
    }

    @Override
    public Map check(long merchantApplyRecordId, int state) {

        return null;
    }

}
