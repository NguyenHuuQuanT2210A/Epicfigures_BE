package com.example.productservice.controllers;

import com.example.productservice.services.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/redis")
public class DemoRedisController {
    private final RedisCacheService redisCacheService;

    @GetMapping("/push")
    public String push() {
        try {
            String key = "list";
            redisCacheService.lPush(key, "value 01");
            redisCacheService.lPush(key, "value 02");
            redisCacheService.lPush(key, "value 03");
            return "success";
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("pop")
    public String pop() {
        try {
            String key = "list";
            return (String)redisCacheService.rPop(key);
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("clearList")
    public String clearList() {
        try {
            String key = "list";
            redisCacheService.setValue(key, "");
            return "success";
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("clearCache")
    public String clearCache() {
        try {
            redisCacheService.setValue("getSync", "");
            return "success";
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("clearAll")
    public String clearAll() {
        try {
            redisCacheService.setValue("getSync", "");
            redisCacheService.setValue("list", "");
            return "success";
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("checkExists")
    public String checkExists() {
        try {
            String key = "getSync";
            return String.valueOf(redisCacheService.checkExistsKey(key));
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("checkExistsList")
    public String checkExistsList() {
        try {
            String key = "list";
            return String.valueOf(redisCacheService.checkExistsKey(key));
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("checkExistsKey")
    public String checkExistsKey(String key) {
        try {
            return String.valueOf(redisCacheService.checkExistsKey(key));
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("getValue")
    public String getValue(String key) {
        try {
            return (String)redisCacheService.getValue(key);
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("setValue")
    public String setValue(String key, String value) {
        try {
            redisCacheService.setValue(key, value);
            return "success";
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("setValueWithTimeout")
    public String setValueWithTimeout(String key, String value, long timeout, TimeUnit timeUnit) {
        try {
            redisCacheService.setValueWithTimeout(key, value, timeout, timeUnit);
            return "success";
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("setTimout")
    public String setTimout(String key, long timeout, TimeUnit timeUnit) {
        try {
            redisCacheService.setTimout(key, timeout, timeUnit);
            return "success";
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("lPush")
    public String lPush(String key, String value) {
        try {
            return (String)redisCacheService.lPush(key, value);
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("rPop")
    public String rPop(String key) {
        try {
            return (String)redisCacheService.rPop(key);
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }
}
