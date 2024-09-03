package com.example.productservice.services.impl;

import com.example.productservice.services.BaseRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.productservice.constant.CommonDefine.TIMESTAMPS;

@Service
@RequiredArgsConstructor
public class BaseRedisServiceImpl<K, F, V> implements BaseRedisService<K, F, V> {
    private final RedisTemplate<K, V> redisTemplate;
    private final HashOperations<K, F, V> hashOperations;
    private final ObjectMapper objectMapper;

    @Override
    public void set(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setTimeToLive(K key, long timeoutInDays) {
        redisTemplate.expire(key, timeoutInDays, TimeUnit.SECONDS);
    }

    @Override
    public void hashSet(K key, F field, V value) {
        hashOperations.put(key, field, value);
    }

    @Override
    public void hashSetAll(K key, V value) {
        Map<String, Object> convertValue = objectMapper.convertValue(value, new TypeReference<Map<String, Object>>() {});
        hashOperations.putAll(key, (Map<? extends F, ? extends V>) convertValue);
    }

    @Override
    public Object rightPush(K key, V value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public Object rightPushAll(K key, List<V> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public List<V> getList(K key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    @Override
    public boolean hashExists(K key, F field) {
        return hashOperations.hasKey(key, field);
    }

    @Override
    public boolean keyExists(K key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Object get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<String, Object> getField(K key) {
        return (Map<String, Object>) hashOperations.entries(key);
    }

    @Override
    public Object hashGet(K key, F field) {
        return hashOperations.get(key, field);
    }

    @Override
    public List<Object> hashGetByFieldPrefix(K key, F fieldPrefix) {
        List<Object> objects = new ArrayList<>();

        Map<String, Object> hashEntries = (Map<String, Object>) hashOperations.entries(key);

        for (Map.Entry<String, Object> entry : hashEntries.entrySet()) {
            if (entry.getKey().startsWith((String) fieldPrefix)) {
                objects.add(entry.getValue());
            }
        }

        return objects;
    }

    @Override
    public Set<F> getFieldPrefixes(K key) {
        return hashOperations.entries(key).keySet();
    }

    @Override
    public Set<K> getKeyPrefixes(K keyPrefix) {
        return redisTemplate.keys(keyPrefix);
    }

    @Override
    public Object sortedAdd(K key, V value) {
        long timestamp = System.currentTimeMillis();
        K keyTimestamp = (K) (key + TIMESTAMPS);
        return redisTemplate.opsForZSet().add(keyTimestamp, value, timestamp);
    }

    @Override
    public List<Object> getKeyTimestamp(K keyTimestamp) {
        List<Object> objects = new ArrayList<>();

        String key = keyTimestamp.toString().replace(TIMESTAMPS, "");

        Set<String> productIds = (Set<String>) redisTemplate.opsForZSet().reverseRange(keyTimestamp, 0, -1);

        assert productIds != null;
        for (String id : productIds) {
            objects.add(hashGet((K) key, (F) id));
        }
        return objects;
    }

    //delete
    @Override
    public void delete(K key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(K key, F field) {
        hashOperations.delete(key, field);
    }

    @Override
    public void delete(K key, List<F> fields) {
        for (F field : fields) {
            hashOperations.delete(key, field);
        }
    }
}
