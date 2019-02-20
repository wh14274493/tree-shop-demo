package com.pm.service.impl;

import com.pm.common.utils.ApiUtil;
import com.pm.domain.Role;
import com.pm.mapper.RoleMapper;
import com.pm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RoleServiceImpl implements RoleService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ApiUtil apiUtil;

    @Override
    public Map add(Role role) {
        role.setState(1);
        roleMapper.insert(role);
        return apiUtil.setSuccessResult(role.getId());
    }

    @Override
    public Map update(Role role) {
        roleMapper.update(role);
        return apiUtil.setSuccessResult();
    }

    @Override
    @CachePut
    public Map find(int state, boolean findAll) {
        List<Role> roleList = null;
        if (findAll) {
            roleList = roleMapper.getAll();
        } else {
            roleList = roleMapper.getByState(state);
        }
        return apiUtil.setSuccessResult(roleList);
    }

    @Override
    public Map delete(long id){
        roleMapper.delete(id);
        return apiUtil.setSuccessResult("角色删除成功！");
    }

}
