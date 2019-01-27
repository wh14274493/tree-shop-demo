package com.pm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pm.common.constants.Constant;
import com.pm.common.constants.IntefaceType;
import com.pm.common.constants.TableName;
import com.pm.common.utils.ApiUtil;
import com.pm.common.utils.DateUtil;
import com.pm.common.utils.Md5Util;
import com.pm.common.utils.RedisUtil;
import com.pm.dao.UserDao;
import com.pm.domain.User;
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
    private UserDao userDao;

    @Autowired
    private Queue mailQueue;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Override
    public Map<String, Object> getTodayProduceMessages(String intefaceType,String type) {
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
        user.setPassword(Md5Util.MD5(md5Param+password));
        user.setCreateTime(DateUtil.getTimestamp());
        user.setUpdateTime(DateUtil.getTimestamp());
        userDao.insert(user, TableName.USER_TABLE_NAME);
        String message = getEmailMessage(user.getEmail(),user.getUsername());
        log.info("开始往消息队列发送消息...");
        jmsMessagingTemplate.convertAndSend(mailQueue,message);
        log.info("成功发送消息息： "+message);
        //TODO keyi存储到redis中保存发送记录
        String hashKey = DateUtil.currentFormatDate(DateUtil.DATE_TO_STRING_SHORT_PATTERN)+ IntefaceType.SMS_EMAIL+"produce";
        log.info("hashKey: "+hashKey);
        redisUtil.rightPush(hashKey,message);
        return apiUtil.setSuccessResult();
    }

    @Override
    public Map realDeleteUserById(Long id) {
        userDao.delete(id, TableName.USER_TABLE_NAME);
        return apiUtil.setSuccessResult("删除成功！");
    }

    @Override
    public Map virtualDeleteUserById(Long id) {
        User user = userDao.findById(id, TableName.USER_TABLE_NAME);
        user.setIsDelete(1);
        user.setUpdateTime(DateUtil.getTimestamp());
        userDao.update(user, TableName.USER_TABLE_NAME);
        return apiUtil.setSuccessResult("删除成功！");
    }

    @Override
    public Map update(User user) {
        user.setUpdateTime(DateUtil.getTimestamp());
        userDao.update(user, TableName.USER_TABLE_NAME);
        return apiUtil.setSuccessResult("修改成功！");
    }

    @Override
    public Map findById(Long id) {
        User user = userDao.findById(id, TableName.USER_TABLE_NAME);
        return apiUtil.setSuccessResult("操作成功！", user);
    }

    @Override
    public Map findAll() {
        List<User> userList = userDao.findAll(TableName.USER_TABLE_NAME);
        return apiUtil.setSuccessResult("操作成功！", userList);
    }

    /**
     * 获取发送邮件内容
     * @param email
     * @param userName
     * @return
     */
    public String getEmailMessage(String email,String userName){
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject content = new JSONObject();
        header.put("intefaceType", IntefaceType.SMS_EMAIL);
        content.put("email",email);
        content.put("userName",userName);
        root.put("header",header);
        root.put("content",content);
        return  root.toJSONString();
    }
}
