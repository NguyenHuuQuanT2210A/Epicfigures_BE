package com.example.notificationService.service;

import com.example.notificationService.dto.response.ApiResponse;
import com.example.notificationService.dto.response.UserResponse;
import com.example.notificationService.enums.OrderSimpleStatus;
import com.example.notificationService.email.EmailService;

import com.example.paymentService.event.CreateEventToNotification;
import com.example.paymentService.event.RequestUpdateStatusOrder;
import com.example.userservice.dtos.request.CreateEventToForgotPassword;
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
        ApiResponse<UserResponse> response = userClient.getUserById(orderSendMail.getUserId());

        List<Object> emailParameters = new ArrayList<>();
        emailParameters.add(response.getData().getUsername());
        emailParameters.add(orderSendMail.getPrice().toString());

        emailService.sendMail(orderSendMail.getEmail(), "Order successfully", emailParameters, "thank-you");
    }

    public void consumerUpdateStatusOrder(RequestUpdateStatusOrder requestUpdateStatusOrder) {
        if (requestUpdateStatusOrder.getStatus()){
            orderClient.changeStatus(requestUpdateStatusOrder.getOrderId(), OrderSimpleStatus.PENDING);
            log.info("Order status is pending");
        }else {
            orderClient.changeStatus(requestUpdateStatusOrder.getOrderId(), OrderSimpleStatus.PAYMENT_FAILED);
            log.info("Order status is cancel");
        }
    }

    public void sendMailForgotPassword(CreateEventToForgotPassword forgotPasswordEvent) {
        ApiResponse<UserResponse> response = userClient.getUserById(forgotPasswordEvent.getId());

        List<Object> emailParameters = new ArrayList<>();
        emailParameters.add(response.getData().getUsername());
        emailParameters.add(response.getData().getEmail());
        emailParameters.add(forgotPasswordEvent.getUrlPlatform());
        emailParameters.add(forgotPasswordEvent.getSecretKey());

        emailService.sendMail(response.getData().getEmail(), "Forgot Password", emailParameters, "forgot-password");
    }
}

