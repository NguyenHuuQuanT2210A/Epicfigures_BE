//package com.example.apigateway.config;
//
//import com.example.apigateway.filter.AuthFilter;
//import com.example.apigateway.filter.PostGlobalFilter;
//import com.example.apigateway.model.Company;
//import com.example.apigateway.model.Student;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.server.WebFilter;
//
//@Configuration
//public class GatewayConfig {
//
//    @Autowired
//    RequestFilter requestFilter;
//
//    @Autowired
//    AuthFilter authFilter;
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//                // adding 2 rotes to first microservice as we need to log request body if method is POST
//        return builder.routes()
//                .route("user-service",r -> r.path("/api/v1/users")
//                        .and().method("POST")
//                        .and().readBody(Student.class, s -> true).filters(f -> f.filters(requestFilter, authFilter))
//                        .uri("http://localhost:8081"))
//                .route("user-service",r -> r.path("/api/v1/users/**")
//                        .and().method("GET").filters(f-> f.filters(authFilter))
//                        .uri("http://localhost:8081"))
//                .route("auth-service",r -> r.path("/api/v1/auth/**")
//                        .uri("http://localhost:8081"))
//                .build();
//    }
//
//    @Bean
//    public RestTemplate getRestTemplate() {
//        return new RestTemplate();
//    }
//
//    @Bean
//    public WebFilter responseFilter(){
//        return new PostGlobalFilter();
//    }
//
//}
