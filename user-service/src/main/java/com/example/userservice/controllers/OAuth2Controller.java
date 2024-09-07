package com.example.userservice.controllers;


import com.example.userservice.models.response.JwtResponse;
import com.example.userservice.securities.jwt.JwtUtils;
import com.example.userservice.securities.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            throw new RuntimeException("User is not authenticated");
        }
        return principal.getAttributes();
    }
    @GetMapping("/oauth2/authorization/github")
    public ResponseEntity test(){
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/login-success")
    public ResponseEntity<?> loginSuccess(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

            try {
                String jwt = jwtUtils.generateJwtOAuth2Token(authentication);

                List<String> roles = List.of("ROLE_USER");

                return ResponseEntity.ok(JwtResponse.builder()
                        .accessToken(jwt)
                        .id(Long.parseLong((Boolean.TRUE.equals(oauth2User.getAttribute("id"))) ? Objects.requireNonNull(oauth2User.getAttribute("id")) : "0"))
                        .username(oauth2User.getAttribute("name"))
                        .email(Boolean.TRUE.equals(oauth2User.getAttribute("email")) ? oauth2User.getAttribute("email") : oauth2User.getAttribute("login"))
                        .roles(roles)
                        .build());
            } catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot generate token");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authentication failed");
        }
    }
    @GetMapping("/login-failure")
    public ResponseEntity<String> loginFail() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UserName or PassWord wrong");
    }
}
