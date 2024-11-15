package com.example.notificationService.service;

import com.example.notificationService.dto.response.UserResponse;
import com.example.notificationService.email.EmailService;
import com.example.notificationService.enums.OrderSimpleStatus;
import com.example.orderservice.dto.request.ReturnItemMail;
import com.example.paymentService.dto.response.OrderResponse;
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

    public void sendMailOrder(OrderResponse orderSendMail) {
        emailService.sendMail(orderSendMail.getEmail(), "Order successfully", (Object) orderSendMail, "thank-you");
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
        emailService.sendMail(forgotPasswordEvent.getEmail(), "Forgot Password", forgotPasswordEvent, "forgot-password");
    }

    public void sendMailContact(ContactRequest contactRequest) {
        emailService.sendMail(contactRequest.getEmail(), "Contact", contactRequest, "contact");
    }

    public void sendMailReturnItem(ReturnItemMail returnItemMail) {
        emailService.sendMail(returnItemMail.getEmail(), "Return Item", returnItemMail, "return-item");
    }
}
