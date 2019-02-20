package com.pm.service;

import com.pm.domain.Role;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RequestMapping("/service/role")
public interface RoleService {

    /**
     * 新增角色，默认为1
     * @param role
     * @return
     */
    @RequestMapping("/add")
    Map add(@RequestBody Role role);

    @RequestMapping("/update")
    Map update(@RequestBody Role role);

    /**
     * 查询角色列表
     * @param state 角色状态
     * @param findAll 是否查询所有
     * @return
     */
    @RequestMapping("/find")
    Map find(@RequestParam int state,@RequestParam boolean findAll);

    @RequestMapping("/delete")
    Map delete(@RequestParam long id);
}
