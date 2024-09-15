package com.example.productservice.process;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.services.ProductService;
import com.example.productservice.services.RedisCacheService;
import com.example.productservice.services.impl.BaseRedisServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.productservice.constant.CommonDefine.GET_ALL_PRODUCTS;
import static com.example.productservice.constant.CommonDefine.PRODUCT_ID;

@Service
@RequiredArgsConstructor
public class ProductCacheUpdater {
    private final ProductService productService;
    private final RedisCacheService redisCacheService;
    private final BaseRedisServiceImpl<String, String, Object> baseRedisService;

    public void updateCache() {
        Long totalProducts = productService.countProducts();

        int pageSize = 10;
        int totalPages = (totalProducts.intValue() + pageSize - 1) / pageSize;

        ExecutorService executor = Executors.newFixedThreadPool(1);

        // For each page, create a task to update the cache
        for (int i = 1; i <= totalPages; i++) {
            int pageIndex = i;
            executor.submit(() -> {
                Page<ProductDTO> products = productService.getAllProducts(PageRequest.of(pageIndex -1, pageSize));

                String key = String.format(GET_ALL_PRODUCTS, pageIndex -1, pageSize);
                baseRedisService.delete(key);

                for (ProductDTO product : products) {
                    baseRedisService.rightPush(key, product);
                    baseRedisService.hashSetAll(PRODUCT_ID + product.getProductId(), product);
                }
            });
        }

        executor.shutdown();
    }
}
