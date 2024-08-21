package com.example.productservice.process;

import com.example.productservice.services.BusinessService;

public class BusinessProcess implements Runnable{

    private final BusinessService businessService;
    private final String input;
    public String result;

    public BusinessProcess (BusinessService businessService, String input) {
        this.businessService = businessService;
        this.input = input;
    }

    @Override
    public void run() {
        try {
            result = businessService.doBusiness(input);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
