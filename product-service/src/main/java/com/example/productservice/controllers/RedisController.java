package com.example.productservice.controllers;

import com.example.productservice.dto.request.RedisRequest;
import com.example.productservice.services.BaseRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/redis")
public class RedisController {
    private final BaseRedisService<String, String, Object> baseRedisService;

    @PostMapping("/set")
    public String set(@RequestBody RedisRequest request) {
        baseRedisService.set(request.getKey(), request.getFieldValue());
        return "success";
    }

    @PostMapping("/setTimeToLive")
    public String setTimeToLive(@RequestBody RedisRequest request) {
        baseRedisService.setTimeToLive(request.getKey(), request.getTimeoutInDays());
        return "success";
    }

    @PostMapping("/hashSet")
    public String hashSet(@RequestBody RedisRequest request) {
        baseRedisService.hashSet(request.getKey(), request.getField(), request.getFieldValue());
        return "success";
    }

    @PostMapping("/hashSetAll")
    public String hashSetAll(@RequestBody RedisRequest request) {
        baseRedisService.hashSetAll(request.getKey(), request.getFieldValue());
        return "success";
    }

    @PostMapping("/sortedAdd")
    public Object sortedAdd(@RequestBody RedisRequest request) {
        return baseRedisService.sortedAdd(request.getKey(), request.getFieldValue());
    }

    @PostMapping("/rightPush")
    public Object rightPush(@RequestBody RedisRequest request) {
        return baseRedisService.rightPush(request.getKey(), request.getFieldValue());
    }

    @PostMapping("/rightPushAll")
    public Object rightPushAll(@RequestBody RedisRequest request) {
        return baseRedisService.rightPushAll(request.getKey(), request.getValues());
    }

    @GetMapping("/getList")
    public List<Object> getList(@RequestParam String key) {
        return baseRedisService.getList(key);
    }

    @GetMapping("/hashExists")
    public boolean hashExists(@RequestBody RedisRequest request) {
        return baseRedisService.hashExists(request.getKey(), request.getField());
    }

    @GetMapping("/keyExists")
    public boolean keyExists(String key) {
        return baseRedisService.keyExists(key);
    }

    @GetMapping("/get")
    public Object get(String key) {
        return baseRedisService.get(key);
    }

    @GetMapping("/getField")
    public Map<String, Object> getField(String key) {
        return baseRedisService.getField(key);
    }

    @GetMapping("/hashGet")
    public Object hashGet(@RequestBody RedisRequest request) {
        return baseRedisService.hashGet(request.getKey(), request.getField());
    }

    @GetMapping("/hashGetByFieldPrefix")
    public List<Object> hashGetByFieldPrefix(@RequestBody RedisRequest request) {
        return baseRedisService.hashGetByFieldPrefix(request.getKey(), request.getFieldPrefix());
    }

    @GetMapping("/getFieldPrefixes")
    public Set<String> getFieldPrefixes(String key) {
        return baseRedisService.getFieldPrefixes(key);
    }

    @GetMapping("/getKeyTimestamp")
    public List<Object> getKeyTimestamp(String keyTimestamp) {
        return baseRedisService.getKeyTimestamp(keyTimestamp);
    }

    @DeleteMapping("/deleteKey")
    public String deleteKey(String key) {
        baseRedisService.delete(key);
        return "success";
    }

    @DeleteMapping("/deleteKEYField")
    public String deleteKEYField(@RequestBody RedisRequest request) {
        baseRedisService.delete(request.getKey(), request.getField());
        return "success";
    }

    @DeleteMapping("/deleteKeyFields")
    public String deleteKeyFields(@RequestBody RedisRequest request) {
        baseRedisService.delete(request.getKey(), request.getFields());
        return "success";
    }

}
