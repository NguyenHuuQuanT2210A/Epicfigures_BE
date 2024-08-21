package com.example.productservice.process;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.entities.Product;
import com.example.productservice.services.ProductService;
import com.example.productservice.services.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class ProductCacheUpdater {
    private final ProductService productService;
    private final RedisCacheService redisCacheService;

    public void updateCache() {
        int totalProducts = productService.countProducts();

        int pageSize = 100;
        int totalPages = (totalProducts + pageSize - 1) / pageSize;

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // For each page, create a task to update the cache
        for (int i = 0; i < totalPages; i++) {
            int pageIndex = i;
            executor.submit(() -> {
                Page<ProductDTO> products = productService.getAllProducts(PageRequest.of(pageIndex -1, pageSize));

                for (ProductDTO product : products) {
                    redisCacheService.setValue("product:" + product.getProductId(), product);
                }
            });
        }

        executor.shutdown();
    }
}
