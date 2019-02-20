package com.pm.mapper;

import com.pm.domain.MerchantApplyRecord;

public interface MerchantApplyRecordMapper {
    int insert(MerchantApplyRecord record);

    int insertSelective(MerchantApplyRecord record);
}