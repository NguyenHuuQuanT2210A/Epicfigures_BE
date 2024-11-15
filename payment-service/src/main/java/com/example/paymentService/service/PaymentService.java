package com.example.paymentService.service;

import com.example.paymentService.dto.request.PaymentRequest;
import com.example.paymentService.dto.response.OrderResponse;
import com.example.paymentService.config.KafkaProducer;
import com.example.paymentService.dto.response.UserResponse;
import com.example.paymentService.entity.Payment;
import com.example.paymentService.enums.PaymentStatus;
import com.example.paymentService.enums.PaymentType;
import com.example.paymentService.event.CreateEventToNotification;
import com.example.paymentService.event.PaymentCreatedEvent;

import com.example.paymentService.event.RequestUpdateStatusOrder;
import com.example.paymentService.repository.PaymentRepository;
import com.example.paymentService.util.ParseBigDecimal;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;
    KafkaTemplate<Long, PaymentCreatedEvent> kafkaTemplate;
    private final KafkaProducer kafkaProducer;
    private final VnPayService vnPayService;
    private final PaypalService paypalService;
    private final OrderClient orderClient;
    private final UserClient userClient;

    public String creatPayment( PaymentRequest request, String baseUrl) throws UnsupportedEncodingException, PayPalRESTException {
        OrderResponse orderResponse = orderClient.getOrderById(request.getOrderId()).getData();

        if (request.getPaymentType().equals(PaymentType.PAYMENT)){
            savePayment(request.getOrderId());
        }else if (request.getPaymentType().equals(PaymentType.REPAYMENT)){
            orderResponse = orderClient.changePaymentMethod(request.getOrderId(), request.getPaymentMethod()).getData();
        }

        if (request.getPaymentMethod().equalsIgnoreCase("PAYPAL")){
            return paypalService.createPayment(request.getOrderId(), orderResponse, baseUrl);
        }
        else if (request.getPaymentMethod().equalsIgnoreCase("VNPAY")){
            return vnPayService.createPaymentVnPay(request.getOrderId(), orderResponse, baseUrl);
        }else {
            updateStatusPayment(true, request.getOrderId());
            updateStatusOrder(true, request.getOrderId());
            return baseUrl;
        }
    }

    public Page<Payment> getByUsername(Pageable pageable, Long userId){
        return paymentRepository.findByUserId(pageable,userId);
    }

    public void savePayment(String orderId){
        OrderResponse orderResponse = orderClient.getOrderById(orderId).getData();

        paymentRepository.save(Payment.builder()
                .userId(orderResponse.getUserId())
                .paidAt(now())
                .total(orderResponse.getTotalPrice())
                .orderId(orderId)
                .status(PaymentStatus.PENDING).build());
    }

    public void updateStatusPayment(Boolean isDone, String orderId){
        Payment payment = paymentRepository.findByOrderId(orderId);
        if(isDone){
            payment.setStatus(PaymentStatus.COMPLETED);
        }
        else {
            payment.setStatus(PaymentStatus.FAILED);
        }
        paymentRepository.save(payment);
    }

    public void updateStatusOrder(Boolean a, String orderId){
        OrderResponse orderResponse = orderClient.getOrderById(orderId).getData();

        if (a){
                kafkaProducer.sendEmail(orderResponse);
        }
        RequestUpdateStatusOrder requestUpdateStatusOrder = new RequestUpdateStatusOrder();
        requestUpdateStatusOrder.setStatus(a);
        requestUpdateStatusOrder.setOrderId(orderId);
        log.info("Before publishing a OrderCreatedEvent");

        kafkaProducer.sendMessageStatusOrder(requestUpdateStatusOrder);

        log.info("******* Returning");

    }

    public Payment getById(Long id){
        return paymentRepository.findById(id).orElse(null);
    }
    public Payment getByOrderId(String id){
        return paymentRepository.findByOrderId(id);
    }
}
