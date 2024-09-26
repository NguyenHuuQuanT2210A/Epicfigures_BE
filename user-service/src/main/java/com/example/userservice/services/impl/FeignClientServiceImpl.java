package com.example.userservice.services.impl;

import com.example.userservice.dtos.request.ProductRequest;
import com.example.userservice.services.FeignClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeignClientServiceImpl{
    @Autowired
    private FeignClientService feignClientService;

    public String getAllProducts() {
        return feignClientService.getAllProducts();
    }

    public ProductRequest getProductById(Long id) {
        return feignClientService.getProductById(id);
    }

    public String createProduct(ProductRequest product) {
        return feignClientService.createProduct(product);
    }

}
