package com.pm.common.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * -------------------------------string类型api-------------------------------
     */

    public void setString(String key, String value, Long timeout) {
        setStringValue(key, value, timeout);
    }

    public void setString(String key, String value) {
        setStringValue(key, value, null);
    }

    public String getString(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value==null?null:(String)value ;
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
     * -------------------------------list类型api-------------------------------
     */

    public void rightPush(Object key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public void leftPush(Object key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public List range(Object key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public List range(Object key, Long begin, Long end) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * -------------------------------hash类型api-------------------------------
     */

    public void putAll(Object key, Map map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public void put(Object key, Object mapKey, Object mapValue) {
        redisTemplate.opsForHash().put(key, mapKey, mapValue);
    }

    public Object get(Object key, Object mapKey) {
        return redisTemplate.opsForHash().get(key, mapKey);
    }

    public Map entries(Object key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Set key(Object key) {
        return redisTemplate.opsForHash().keys(key);
    }

    public boolean hasKey(Object key, Object mapKey) {
        return redisTemplate.opsForHash().hasKey(key, mapKey);
    }

    public List values(Object key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * -------------------------------set类型api-------------------------------
     */


    /**
     * -------------------------------zset类型api-------------------------------
     */

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
