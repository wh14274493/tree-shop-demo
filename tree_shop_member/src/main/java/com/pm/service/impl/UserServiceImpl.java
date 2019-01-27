package com.pm.service.impl;

import com.pm.common.constants.Constant;
import com.pm.common.constants.TableName;
import com.pm.common.utils.ApiUtil;
import com.pm.common.utils.DateUtil;
import com.pm.common.utils.RedisUtil;
import com.pm.dao.UserDao;
import com.pm.domain.User;
import com.pm.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ApiUtil apiUtil;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserDao userDao;

    @Override
    public Map<String, Object> getMenberInfo(String name) {
        String data = redisUtil.getString(name);
        return apiUtil.setSuccessResult(Constant.SUCCESS_MSG, data);
    }

    @Override
    public Map<String, Object> setMemberInfo(String name, String age) {
        redisUtil.setString(name, age);
        return apiUtil.setSuccessResult();
    }

    @Override
    public Map register(User user) {
        if (StringUtils.isEmpty(user.getAccount())) {
            return apiUtil.setErrorResult("用户名不能为空！");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            return apiUtil.setErrorResult("密码不能为空！");
        }
        user.setCreateTime(DateUtil.getTimestamp());
        user.setUpdateTime(DateUtil.getTimestamp());
        userDao.insert(user, TableName.USER_TABLE_NAME);
        return apiUtil.setSuccessResult();
    }

    @Override
    public Map realDeleteUserById(Long id) {
        userDao.delete(id,TableName.USER_TABLE_NAME);
        return apiUtil.setSuccessResult("删除成功！");
    }

    @Override
    public Map virtualDeleteUserById(Long id) {
        User user = userDao.findById(id,TableName.USER_TABLE_NAME);
        user.setIsDelete(1);
        user.setUpdateTime(DateUtil.getTimestamp());
        userDao.update(user,TableName.USER_TABLE_NAME);
        return apiUtil.setSuccessResult("删除成功！");
    }

    @Override
    public Map update(User user) {
        user.setUpdateTime(DateUtil.getTimestamp());
        userDao.update(user,TableName.USER_TABLE_NAME);
        return apiUtil.setSuccessResult("修改成功！");
    }

    @Override
    public Map findById(Long id) {
        User user = userDao.findById(id,TableName.USER_TABLE_NAME);
        return apiUtil.setSuccessResult("操作成功！",user);
    }

    @Override
    public Map findAll() {
        List<User> userList = userDao.findAll(TableName.USER_TABLE_NAME);
        return apiUtil.setSuccessResult("操作成功！",userList);
    }

}
