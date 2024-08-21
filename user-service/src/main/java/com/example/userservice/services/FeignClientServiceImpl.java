package com.example.userservice.services;

import com.example.userservice.dtos.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
