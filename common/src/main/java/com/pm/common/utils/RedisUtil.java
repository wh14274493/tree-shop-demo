package com.pm.common.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    public void setString(String key, String value, Long timeout) {
        setStringValue(key, value, timeout);
    }

    public void setString(String key, String value) {
        setStringValue(key, value, null);
    }

    public String getString(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * @param key     键
     * @param value   值
     * @param timeout 存储时间
     */
    private void setStringValue(String key, String value, Long timeout) {
        if (!StringUtils.isEmpty(key) && value != null) {
            redisTemplate.opsForValue().set(key, value);
        }
        if (timeout != null) {
            setTimeOut(key, timeout);
        }
    }

    /**
     * 单个键和值存入
     *
     * @param hashKey 哈希键
     * @param key     键
     * @param value   值
     */
    public void setHashValue(String hashKey, String key, String value) {
        redisTemplate.opsForHash().put(hashKey, key, value);
    }

    /**
     * 批量存储
     *
     * @param hashKey 哈希键
     * @param map
     */
    public void setHashValue(String hashKey, Map map) {
        if (!StringUtils.isEmpty(hashKey) && map != null) {
            redisTemplate.opsForHash().putAll(hashKey, map);
        }
    }


    /**
     * 根据键值单个删除
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 根据键值批量删除
     *
     * @param collection
     */
    public void delete(Collection collection) {
        redisTemplate.delete(collection);
    }

    /**
     * 设置超时时间
     *
     * @param key
     * @param timeOut
     */
    public void setTimeOut(String key, Long timeOut) {
        redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
    }
}
