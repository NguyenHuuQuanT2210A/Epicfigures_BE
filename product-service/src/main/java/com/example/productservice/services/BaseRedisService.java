package com.example.productservice.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseRedisService<K, F, V> {
    void set(K key, V value);

    void setTimeToLive(K key, long timeoutInDays);

    void hashSet(K key, F field, V value);

    void hashSetAll(K key, V value);

    Object rightPush(K key, V value);

    Object rightPushAll(K key, List<V> values);

    List<V> getList(K key);

    boolean hashExists(K key, F field);

    boolean keyExists(K key);

    Object get(K key);

    Map<String, Object> getField(K key);

    Object hashGet(K key, F field);

    Object sortedAdd(K key, V value);

    List<Object> hashGetByFieldPrefix(K key, F fieldPrefix);

    Set<F> getFieldPrefixes(K key);

    Set<K> getKeyPrefixes(K keyPrefix);

    List<Object> getKeyTimestamp(K keyTimestamp);

    void delete(K key);

    void delete(K key, F field);

    void delete(K key, List<F> fields);
}
