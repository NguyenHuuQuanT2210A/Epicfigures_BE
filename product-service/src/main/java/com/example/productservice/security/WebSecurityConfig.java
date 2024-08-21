//package com.example.productservice.security;
//
//import com.example.productservice.security.jwt.AuthEntryPointJwt;
//import com.example.productservice.security.jwt.AuthTokenFilter;
//import com.example.productservice.security.service.UserDetailsServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//        // securedEnabled = true,
//        // jsr250Enabled = true,
//        prePostEnabled = true)
//public class WebSecurityConfig {
//    @Autowired
//    UserDetailsServiceImpl userDetailsService;
//
//    @Autowired
//    AuthTokenFilter authTokenFilter;
//    @Autowired
//    private AuthEntryPointJwt unauthorizedHandler;
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//    //mã hóa băng BCrypt
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)  // Vô hiệu hóa CSRF
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                            .requestMatchers("/oauth2/user").authenticated()
////                            .requestMatchers("/api/v1/users/**").authenticated() // Yêu cầu xác thực cho /api/v1/**
//                            .requestMatchers("/api/private/**").hasRole("ADMIN")//.anyRequest().hasAnyRole("ADMIN") // Yêu cầu xác thực cho /api/private/**
//                            .requestMatchers("/api/v1/auth/logout").authenticated()
////                            .requestMatchers("/oauth2/login-success").authenticated()
//                            .anyRequest().permitAll() // Mở quyền truy cập cho tất cả các yêu cầu khác
//                )
////                .sessionManagement(sessionManagement ->
////                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                )
//                .headers(headers -> headers
//                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
//                        )
//                )
//                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling(exceptionHandling ->
//                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler) // Xử lý lỗi xác thực
//                )
//                .oauth2Login(oauth2Login ->
//                        oauth2Login
//                                .loginPage("/login")
//                                .defaultSuccessUrl("/oauth2/login-success")
//                                .failureUrl("/oauth2/login-failure")
//                ); // Thêm bộ lọc JWT;
//
//        return http.build();
//    }
//
//}
