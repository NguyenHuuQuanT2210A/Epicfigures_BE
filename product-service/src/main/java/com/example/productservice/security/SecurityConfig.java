package com.example.productservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private AuthTokenFilter authTokenFilter;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/products/search-by-specification/**",
            "/api/v1/products/getAll/**",
            "/api/v1/products/list/**",
            "/api/v1/products/name/**",
            "/api/v1/products/category/**",
            "/api/v1/products/id/**",
            "/api/v1/categories/getAll/**",
            "/api/v1/categories/id/**",
            "/api/v1/categories/name/**",
            "/api/v1/product-images/productId/**",
            "/api/v1/product-images/imagesPost/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)  // Vô hiệu hóa CSRF
                .authorizeHttpRequests(authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
//                                        .requestMatchers("/api/v1/products/**").hasRole("ADMIN")
//                                        .requestMatchers("/api/v1/categories/**").hasRole("ADMIN")
//                                        .requestMatchers("/api/v1/product-images/**").hasRole("ADMIN")
//                                        .requestMatchers("/api/v1/cart/**").hasRole("USER")
                                        .anyRequest().authenticated() // Tất cả các request đều cần được xác thực
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
                        )
                )
                ; // Thêm bộ lọc JWT;
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

