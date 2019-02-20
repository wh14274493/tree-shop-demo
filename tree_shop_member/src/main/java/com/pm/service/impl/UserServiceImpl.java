package com.pm.service.impl;

import com.pm.common.constants.Constant;
import com.pm.common.utils.*;
import com.pm.domain.User;
import com.pm.mapper.UserMapper;
import com.pm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ApiUtil apiUtil;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Queue mailQueue;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private MqUtil mqUtil;

    @Override
    public Map<String, Object> getTodayProduceMessages(String intefaceType, String type) {
        StringBuffer today = new StringBuffer(DateUtil.currentFormatDate(DateUtil.DATE_TO_STRING_SHORT_PATTERN));
        String hashKey = today.append(intefaceType).append(type).toString();
        List data = redisUtil.range(hashKey);
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
        //MD5加密加盐保存密码
        String password = user.getPassword();
        String md5Param = UUID.randomUUID().toString();
        user.setMd5Param(md5Param);
        user.setPassword(Md5Util.MD5(md5Param + password));
        user.setCreateTime(DateUtil.getTimestamp());
        user.setUpdateTime(DateUtil.getTimestamp());
        userMapper.insert(user);
        //  向用户发送邮件通知
        mqUtil.sendEmailToQueue(user.getEmail(), user.getUserName(), mailQueue);
        return apiUtil.setSuccessResult("注册成功！",user.getId());
    }

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @Override
    public Map login(User user) {
        User userGetByAccount = userMapper.getByAccount(user.getAccount());
        if (userGetByAccount == null) {
            return apiUtil.setErrorResult("登陆失败，用户不存在！");
        }
        String password = Md5Util.MD5(userGetByAccount.getMd5Param() + user.getPassword());
        if (!userGetByAccount.getPassword().equals(password)) {
            return apiUtil.setErrorResult("登陆失败，账号或密码错误！");
        }
        // 成功登录以后生成token存储到redis中，解决session共享的问题
        // key:token  value:userId 这里不存user对象，避免redis中数据不同步的问题，每次都去数据库查询
        String token = TokenUtil.getToken();
        String userId = String.valueOf(userGetByAccount.getId());
        redisUtil.setString(token, userId, Constant.TOKEN_VALIDATE_TIME);
        // 将token返回给客户端
        return apiUtil.setSuccessResult("登录成功！", token);
    }

    @Override
    public Map realDeleteUserById(Long id) {
        userMapper.delete(id);
        return apiUtil.setSuccessResult("删除成功！");
    }

    @Override
    public Map virtualDeleteUserById(Long id) {
        User user = userMapper.getById(id);
        user.setIsDelete(1);
        user.setUpdateTime(DateUtil.getTimestamp());
        userMapper.update(user);
        return apiUtil.setSuccessResult("删除成功！");
    }

    @Override
    public Map update(User user) {
        User userGetById = userMapper.getById(user.getId());
        user.setUpdateTime(DateUtil.getTimestamp());
        if (user.getPassword() != null) {
            StringBuffer password = new StringBuffer(userGetById.getMd5Param()).append(user.getPassword());
            user.setPassword(Md5Util.MD5(password.toString()));
        }
        User u = ClassUtil.combine(user, userGetById);
        userMapper.update(u);
        return apiUtil.setSuccessResult("修改成功！");
    }

    @Override
    public Map findById(Long id) {
        User user = userMapper.getById(id);
        return apiUtil.setSuccessResult("操作成功！", user);
    }

    @Override
    public Map findAll() {
        List<User> userList = userMapper.getAll();
        return apiUtil.setSuccessResult("操作成功！", userList);
    }

    /**
     * 获取当前用户
     *
     * @param token
     * @return
     */
    @Override
    public Map getCurrentUser(String token) {
        String userId = redisUtil.getString(token);
        if (StringUtils.isEmpty(userId)) {
            return apiUtil.setSuccessResult();
        }
        User user = userMapper.getById(Long.parseLong(userId));
        return apiUtil.setSuccessResult(user);
    }

}
