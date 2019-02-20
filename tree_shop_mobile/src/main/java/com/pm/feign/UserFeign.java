package com.pm.feign;

import com.pm.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("member")
public interface UserFeign extends UserService {
}
