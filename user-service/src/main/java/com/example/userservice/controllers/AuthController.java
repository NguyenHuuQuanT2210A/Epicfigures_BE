package com.example.userservice.controllers;

import com.example.userservice.dtos.request.ResetPasswordDto;
import com.example.userservice.models.requests.LoginRequest;
import com.example.userservice.models.requests.SignupRequest;
import com.example.userservice.models.response.MessageResponse;
import com.example.userservice.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Auth Controller")
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
