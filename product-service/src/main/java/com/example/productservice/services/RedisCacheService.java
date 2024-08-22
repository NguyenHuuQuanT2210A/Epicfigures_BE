package com.example.productservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {
    @Autowired
    private RedisTemplate redisTemplate;

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setValue(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setTimout(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key,timeout, timeUnit);
    }

    public void setValueWithTimeout(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key,timeout, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean checkExistsKey(String key){
        boolean check = false;
        try {
            check = redisTemplate.hasKey(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return check;
    }
    public Object lPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public Object lPushAll(String key, String... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    // set type of key to HASH
    public void setHash(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }
}
