package com.pm.mapper;

import com.pm.domain.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    @CacheEvict(value = "user",key = "'allUser'")
    int insert(User user);

    @CacheEvict(value = "user",key = "'allUser'")
    void update(User user);

    @CacheEvict(value = "user",key = "'allUser'")
    void delete(long id);

    User getById(long id);

    @Cacheable(value = "user",key = "'allUser'")
    List<User> getAll();

    User getByAccount(String account);

    User getByAccountAndPassword(Map paramMap);

}