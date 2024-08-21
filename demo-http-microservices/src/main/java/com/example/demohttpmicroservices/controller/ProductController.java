package com.example.demohttpmicroservices.controller;


import com.example.demohttpmicroservices.dto.ProductRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @GetMapping
    public List<ProductRequest> getAllProducts() {
        List<ProductRequest> products = List.of(
                new ProductRequest(1L, "Product 1", 1000),
                new ProductRequest(2L, "Product 2", 2000),
                new ProductRequest(3L, "Product 3", 3000)
        );
        return products;
    }

    @PostMapping
    public String createProduct(@RequestBody ProductRequest product) {
        System.out.println("Processing...");
        ProductRequest productRequest;
        if (product.getProductId() == null) {
            productRequest = new ProductRequest(4L, "Product 4", 4000);
        } else {
            productRequest = new ProductRequest(product.getProductId(), product.getName(), product.getPrice().intValue());
        }
        return productRequest.toString();
    }
}
