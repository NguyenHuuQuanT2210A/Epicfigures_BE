package com.example.notificationService.service;

import com.example.notificationService.dto.response.UserResponse;
import com.example.notificationService.email.EmailService;
import com.example.notificationService.enums.OrderSimpleStatus;
import com.example.orderservice.dto.request.ReturnItemMail;
import com.example.paymentService.event.CreateEventToNotification;
import com.example.paymentService.event.RequestUpdateStatusOrder;
import com.example.userservice.dtos.request.ContactRequest;
import com.example.userservice.dtos.request.CreateEventToForgotPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final EmailService emailService;
    private final OrderClient orderClient;
    private final UserClient userClient;

    public void sendMailOrder(CreateEventToNotification orderSendMail) {
        UserResponse response = userClient.getUserById(orderSendMail.getUserId()).getData();

        List<Object> emailParameters = new ArrayList<>();
        emailParameters.add(response.getUsername());
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
        UserResponse response = userClient.getUserById(forgotPasswordEvent.getId()).getData();

        List<Object> emailParameters = new ArrayList<>();
        emailParameters.add(response.getUsername());
        emailParameters.add(response.getEmail());
        emailParameters.add(forgotPasswordEvent.getUrlPlatform());
        emailParameters.add(forgotPasswordEvent.getSecretKey());

        emailService.sendMail(response.getEmail(), "Forgot Password", emailParameters, "forgot-password");
    }

    public void sendMailContact(ContactRequest contactRequest) {
        List<Object> emailParameters = new ArrayList<>();
        emailParameters.add(contactRequest.getUsername());
        emailParameters.add(contactRequest.getEmail());
        emailParameters.add(contactRequest.getPhoneNumber());
        emailParameters.add(contactRequest.getNote());

        emailService.sendMail(contactRequest.getEmail(), "Contact", emailParameters, "contact");
    }

    public void sendMailReturnItem(ReturnItemMail returnItemMail) {
        List<Object> emailParameters = new ArrayList<>();
        emailParameters.add(returnItemMail.getUsername());
        emailParameters.add(returnItemMail.getEmail());
        emailParameters.add(returnItemMail.getOrderCode());
        emailParameters.add(returnItemMail.getStatus());
        emailParameters.add(returnItemMail.getStatusNote());

        emailService.sendMail(returnItemMail.getEmail(), "Return Item", emailParameters, "return-item");
    }
}
