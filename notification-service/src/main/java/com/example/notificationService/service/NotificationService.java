package com.example.notificationService.service;

import com.example.common.dto.response.ApiResponse;
import com.example.common.dto.response.OrderResponse;
import com.example.common.event.CreateEventToForgotPassword;
import com.example.common.event.CreateEventToNotification;
import com.example.notificationService.email.EmailService;

import com.example.common.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmailService emailService;
    private final RestTemplate restTemplate;

    public void sendMailOrder(CreateEventToNotification orderSendMail) {
        ApiResponse<?> response = restTemplate.getForObject("http://localhost:8081/api/v1/users/" + orderSendMail.getUserId(), ApiResponse.class);

        assert response != null;
        ObjectMapper mapper = new ObjectMapper();
        UserDTO userDTO = mapper.convertValue(response.getData(), UserDTO.class);

        List<Object> emailParameters = new ArrayList<>();
        emailParameters.add(userDTO.getUsername());
        emailParameters.add(orderSendMail.getPrice().toString());

        emailService.sendMail(orderSendMail.getEmail(), "Order successfully", emailParameters, "thank-you");
    }

    public void sendMailForgotPassword(CreateEventToForgotPassword forgotPasswordEvent) {
        ApiResponse<?> response = restTemplate.getForObject("http://localhost:8081/api/v1/users/" + forgotPasswordEvent.getId(), ApiResponse.class);

        assert response != null;
        ObjectMapper mapper = new ObjectMapper();
        UserDTO userDTO = mapper.convertValue(response.getData(), UserDTO.class);

        List<Object> emailParameters = new ArrayList<>();
        emailParameters.add(userDTO.getUsername());
        emailParameters.add(userDTO.getEmail());
        emailParameters.add(forgotPasswordEvent.getSecretKey());

        emailService.sendMail(userDTO.getEmail(), "Forgot Password", emailParameters, "forgot-password");
    }
}

