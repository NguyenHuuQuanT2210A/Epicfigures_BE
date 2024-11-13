package com.example.apigateway.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.server.WebFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable();
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/api/v1/auth/**").permitAll()
//                        .pathMatchers("/api/v1/users/{id}").authenticated()
//                        .pathMatchers("/api/v1/products/**").authenticated()
//                        .anyExchange().authenticated()
//                );
//                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);

        return http.build();
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    CorsWebFilter corsWebFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));

        org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/ws/**", corsConfiguration);

        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/ws/customer/**")
                        .filters(f -> f
                                        .setResponseHeader("Access-Control-Allow-Credentials", "true")
                                        .setResponseHeader("Access-Control-Allow-Origin", "http://localhost:3000")  // Địa chỉ của ReactJS
//                                .setResponseHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
//                                .setResponseHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                        )
                        .uri("http://localhost:8087"))
                .route(r -> r.path("/ws/admin/**")
                        .filters(f -> f
                                .setResponseHeader("Access-Control-Allow-Credentials", "true")
                                .setResponseHeader("Access-Control-Allow-Origin", "http://localhost:3001")  // Địa chỉ của ReactJS
//                                .setResponseHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
//                                .setResponseHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                        )
                        .uri("http://localhost:8087"))

                .build();
    }
}