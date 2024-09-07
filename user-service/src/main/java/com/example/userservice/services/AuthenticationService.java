package com.example.userservice.services;

import com.example.common.event.CreateEventToForgotPassword;
import com.example.userservice.configs.KafkaProducer;
import com.example.userservice.dtos.request.ResetPasswordDto;
import com.example.userservice.entities.Role;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.models.requests.LoginRequest;
import com.example.userservice.models.requests.SignupRequest;
import com.example.userservice.models.response.JwtResponse;
import com.example.userservice.repositories.RoleRepository;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.securities.jwt.JwtUtils;
import com.example.userservice.securities.services.TokenBlacklistService;
import com.example.userservice.securities.services.UserDetailsImpl;
import com.example.userservice.statics.enums.ERole;
import com.example.userservice.statics.enums.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.userservice.statics.enums.TokenType.REFRESH_TOKEN;
import static com.example.userservice.statics.enums.TokenType.RESET_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenBlacklistService tokenBlacklistService;

    private final KafkaProducer kafkaProducer;

    public JwtResponse login(LoginRequest loginRequest) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String accessToken = jwtUtils.generateAccessToken(userDetails);
            String refreshToken = jwtUtils.generateRefreshToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .id(userDetails.getId())
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .roles(roles)
                    .build();
    }


    public void register(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new CustomException("Error: Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new CustomException("Error: Email is already in use!", HttpStatus.BAD_REQUEST);
        }
        
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new CustomException("Error: Role is not found.", HttpStatus.NOT_FOUND));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);
    }

    public JwtResponse refresh(HttpServletRequest request) {
        String refreshToken = request.getHeader("Authorization");
        if (StringUtils.isBlank(refreshToken) && jwtUtils.validateJwtToken(refreshToken, REFRESH_TOKEN)) {
            throw new CustomException("Invalid token", HttpStatus.BAD_REQUEST);
        }

        String token = refreshToken.substring(7);

        var userDetail = UserDetailsImpl.build(getUser(token, REFRESH_TOKEN));

        String accessToken = jwtUtils.generateAccessToken(userDetail);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(userDetail.getId())
                .username(userDetail.getUsername())
                .email(userDetail.getEmail())
                .roles(userDetail.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();
    }

    public String logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            tokenBlacklistService.invalidateToken(token);
            return "Logged out successfully";
        } else {
            return "Invalid token";
        }
    }

    public String forgotPassword(String email) {
        log.info("---------- forgotPassword ----------");

        // check email exists or not
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        // generate reset token
        String resetToken = jwtUtils.generateResetToken(UserDetailsImpl.build(user));

        // send email to user
//        String confirmLink = String.format("curl --location 'http://localhost:8081/auth/reset-password' \\\n" +
//                "--header 'accept: */*' \\\n" +
//                "--header 'Content-Type: application/json' \\\n" +
//                "--data '%s'", resetToken);
//        log.info("--> confirmLink: {}", confirmLink);


        kafkaProducer.sendEmailForgotPassword(new CreateEventToForgotPassword(user.getId(), user.getEmail(), resetToken));

        return resetToken;
    }


    public String resetPassword(String resetToken) {
        log.info("----- resetPassword -----");
        jwtUtils.validateJwtToken(resetToken, RESET_TOKEN);
//        tokenBlacklistService.invalidateToken(resetToken);
        return "Reset";
    }

    public String changePassword(ResetPasswordDto request) {
        log.info("----- changePassword -----");
        var user = getUser(request.getSecretKey(), RESET_TOKEN);

        if (!request.getPassword().equals(request.getConfirmPassword())){
            throw new CustomException("Password not match", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return "Changed";
    }

    private User getUser(String secretKey, TokenType type){
        final String userName= jwtUtils.getUserNameFromJwtToken(secretKey, type);

        var user = userRepository.findByUsername(userName).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        var userDetail = UserDetailsImpl.build(user);

        if (!userDetail.isEnabled()){
            throw new CustomException("User is not active", HttpStatus.BAD_REQUEST);
        }

        if (!jwtUtils.validateJwtToken(secretKey, REFRESH_TOKEN)){
            throw new CustomException("Not allow access with this token", HttpStatus.BAD_REQUEST);
        }
        return user;
    }

}
