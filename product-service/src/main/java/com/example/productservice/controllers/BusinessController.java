package com.example.productservice.controllers;

import com.example.productservice.process.BusinessProcess;
import com.example.productservice.process.TaskManager;
import com.example.productservice.services.BusinessService;
import com.example.productservice.services.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1/business/")
public class BusinessController {
    @Autowired
    private BusinessService businessService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private TaskManager taskManager;

    @GetMapping("getSync")
    public String getSyncValue() {
        try {
            String key = "getSync";
            if (redisCacheService.checkExistsKey(key)) {
                return (String)redisCacheService.getValue(key);
            }
            String result1 = businessService.doBusiness("business 01 result ");
            String result2 = businessService.doBusiness("business 02 result ");
            String result3 = businessService.doBusiness("business 03 result ");
            String response = result1 + result2 + result3;
            redisCacheService.setValueWithTimeout(key, response,10, TimeUnit.SECONDS);
            return response;
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }


    @GetMapping("getASync")
    public String getASyncValue() {
        try {
            String response = "";
            List<String> inputs = Arrays.asList("business 01 result ","business 02 result ","business 03 result ");
            List<BusinessProcess> listTask = new ArrayList<>();
            for (String input : inputs) {
                BusinessProcess task = new BusinessProcess(businessService, input);
                listTask.add(task);
            }
            taskManager.execute(listTask);
            for (BusinessProcess task : listTask) {
                response = response + task.result;
            }
            return response;
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return "not success";
    }

    @GetMapping("push")
    public String push() {
        try {
            String key = "list";
            List<String> saleDepartments = Arrays.asList("sale 01", "sale 02", "sale 03", "sale 04", "sale 05");
            redisCacheService.lPushAll(key, String.valueOf(saleDepartments));

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
