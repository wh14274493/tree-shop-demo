package com.pm.mapper;

import com.pm.domain.Role;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface RoleMapper {

    @CacheEvict(value = "role", key = "'allRole'")
    int insert(Role record);

    @CacheEvict(value = "role", key = "'allRole'")
    void update(Role record);

    @CacheEvict(value = "role", key = "'allRole'")
    void delete(long id);

    Role getById(long id);

    List<Role> getByState(int state);

    @Cacheable(value = "role", key = "'allRole'")
    List<Role> getAll();
}