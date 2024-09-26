package com.example.paymentService.service;

import com.example.paymentService.dto.response.OrderResponse;
import com.example.paymentService.config.KafkaProducer;
import com.example.paymentService.dto.response.ApiResponse;
import com.example.paymentService.dto.response.UserResponse;
import com.example.paymentService.entity.Payment;
import com.example.paymentService.enums.PaymentStatus;
import com.example.paymentService.event.CreateEventToNotification;
import com.example.paymentService.event.PaymentCreatedEvent;

import com.example.paymentService.event.RequestUpdateStatusOrder;
import com.example.paymentService.repository.PaymentRepository;
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

    public String creatPayment( String urlReturn, String orderId, String paymentMethod) throws UnsupportedEncodingException, PayPalRESTException {
        ApiResponse<OrderResponse> orderResponse = orderClient.getOrderById(orderId);

        if (paymentMethod.equalsIgnoreCase("PAYPAL")){
            return paypalService.createPayment(orderId, orderResponse.getData(), urlReturn);
        }
        else if (paymentMethod.equalsIgnoreCase("VNPAY")){
            return vnPayService.createPaymentVnPay(orderId, orderResponse.getData(), urlReturn);
        }
        return null;
    }

    public Page<Payment> getByUsername(Pageable pageable, Long userId){
        return paymentRepository.findByUserId(pageable,userId);
    }

    public void savePayment(String orderId){
        ApiResponse<OrderResponse> orderResponse = orderClient.getOrderById(orderId);

        paymentRepository.save(Payment.builder()
                .userId(orderResponse.getData().getUserId())
                .paidAt(now())
                .total(orderResponse.getData().getTotalPrice())
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

    public void UpdateStatusOrder(Boolean a, String orderId){
        ApiResponse<OrderResponse> orderResponse = orderClient.getOrderById(orderId);
        ApiResponse<UserResponse> userResponse = userClient.getUserById(orderResponse.getData().getUserId());

        if (a){
            kafkaProducer.sendEmail(new CreateEventToNotification(orderResponse.getData().getUserId(), userResponse.getData().getEmail(), orderResponse.getData().getTotalPrice().intValueExact()));
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
