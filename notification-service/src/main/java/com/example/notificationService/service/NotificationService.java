package com.example.notificationService.service;

import com.example.notificationService.dto.response.ApiResponse;
import com.example.notificationService.dto.response.UserResponse;
import com.example.notificationService.enums.OrderSimpleStatus;
import com.example.notificationService.event.CreateEventToForgotPassword;
import com.example.notificationService.event.CreateEventToNotification;
import com.example.notificationService.event.RequestUpdateStatusOrder;
import com.example.notificationService.email.EmailService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmailService emailService;
    private final OrderClient orderClient;
    private final UserClient userClient;

    public void sendMailOrder(CreateEventToNotification orderSendMail) {
//        ApiResponse<?> response = restTemplate.getForObject("http://localhost:8081/api/v1/users/" + orderSendMail.getUserId(), ApiResponse.class);
        ApiResponse<?> response = userClient.getUserById(orderSendMail.getUserId());

        assert response != null;
        ObjectMapper mapper = new ObjectMapper();
        UserResponse userDTO = mapper.convertValue(response.getData(), UserResponse.class);

        List<Object> emailParameters = new ArrayList<>();
        emailParameters.add(userDTO.getUsername());
        emailParameters.add(orderSendMail.getPrice().toString());

        emailService.sendMail(orderSendMail.getEmail(), "Order successfully", emailParameters, "thank-you");
    }

    public void consumerUpdateStatusOrder(RequestUpdateStatusOrder requestUpdateStatusOrder) {
        if (requestUpdateStatusOrder.getStatus()){
            orderClient.changeStatus(requestUpdateStatusOrder.getOrderId(), OrderSimpleStatus.PENDING);
            log.info("Order status is pending");
        }else {
            orderClient.changeStatus(requestUpdateStatusOrder.getOrderId(), OrderSimpleStatus.CANCEL);
            log.info("Order status is cancel");
        }
    }

    public void sendMailForgotPassword(CreateEventToForgotPassword forgotPasswordEvent) {
//        ApiResponse<?> response = restTemplate.getForObject("http://localhost:8081/api/v1/users/" + forgotPasswordEvent.getId(), ApiResponse.class);
        ApiResponse<?> response = userClient.getUserById(forgotPasswordEvent.getId());

        assert response != null;
        ObjectMapper mapper = new ObjectMapper();
        UserResponse userDTO = mapper.convertValue(response.getData(), UserResponse.class);

        List<Object> emailParameters = new ArrayList<>();
        emailParameters.add(userDTO.getUsername());
        emailParameters.add(userDTO.getEmail());
        emailParameters.add(forgotPasswordEvent.getSecretKey());

        emailService.sendMail(userDTO.getEmail(), "Forgot Password", emailParameters, "forgot-password");
    }
}

