package com.example.userservice.controllers;

import com.example.userservice.dtos.request.ResetPasswordDto;
import com.example.userservice.entities.Role;
import com.example.userservice.entities.User;
import com.example.userservice.models.requests.LoginRequest;
import com.example.userservice.models.requests.SignupRequest;
import com.example.userservice.models.response.JwtResponse;
import com.example.userservice.models.response.MessageResponse;
import com.example.userservice.repositories.RoleRepository;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.securities.jwt.JwtUtils;
import com.example.userservice.securities.services.TokenBlacklistService;
import com.example.userservice.securities.services.UserDetailsImpl;
import com.example.userservice.services.AuthenticationService;
import com.example.userservice.statics.enums.ERole;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Auth", description = "Auth Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authenticationService.login(loginRequest));
        } catch (Exception e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UserName or PassWord wrong");
       }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        authenticationService.register(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        return ResponseEntity.ok( authenticationService.refresh(request));
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        return authenticationService.logout(request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        return new ResponseEntity<>(authenticationService.forgotPassword(email), HttpStatus.OK);
    }

//    @PostMapping("/reset-password-post")
//    public ResponseEntity<String> resetPasswordPost(@RequestBody String secretKey) {
//        return new ResponseEntity<>(authenticationService.resetPassword(secretKey), HttpStatus.OK);
//    }

    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String secretKey) {
        return new ResponseEntity<>(authenticationService.resetPassword(secretKey), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ResetPasswordDto request) {
        return new ResponseEntity<>(authenticationService.changePassword(request), HttpStatus.OK);
    }

}
