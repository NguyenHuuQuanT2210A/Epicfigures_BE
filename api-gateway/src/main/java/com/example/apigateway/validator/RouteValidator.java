package com.example.apigateway.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/auth.*",
            "/eureka",
            "/api/v1/users/role.*",
            "/api/v1/products/search-by-specification.*",
            "/api/v1/products/getAll.*",
            "/api/v1/products/list.*",
            "/api/v1/products/name.*",
            "/api/v1/products/category.*",
            "/api/v1/products/id.*",
            "/api/v1/categories/getAll.*",
            "/api/v1/categories/id.*",
            "/api/v1/categories/name.*",
            "/api/v1/categories/parentCategory.*",
            "/api/v1/categories/parentCategoryIsNull",
            "/api/v1/product-images/productId.*",
            "/api/v1/product-images/imagesPost.*",
            "/api/v1/product-images/getAll.*",
            "/api/v1/product-images/isProductImagesExist.*",
            "/api/v1/white_list/product/.*",
            "/api/v1/feedback/id.*",
            "/api/v1/feedback/orderDetail.*",
            "/api/v1/feedback/product.*",
            "/api/v1/feedback/productIdAndRateStar.*",
            "/api/v1/blogs/getAll.*",
            "/api/v1/blogs/id.*",
            "/api/v1/blogs/blog.*",
            "/api/v1/blogs/title.*"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().matches(uri));
}