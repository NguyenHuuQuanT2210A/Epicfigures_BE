package com.example.paymentService.service;

import com.example.common.dto.OrderDTO;
import com.example.common.dto.response.OrderResponse;
import com.example.common.event.CreateEventToNotification;
import com.example.common.event.RequestUpdateStatusOrder;
import com.example.paymentService.config.Config;
import com.example.paymentService.config.KafkaProducer;
import com.example.paymentService.dto.ApiResponse;
import com.example.paymentService.entity.Payment;
import com.example.paymentService.enums.PaymentStatus;
import com.example.paymentService.event.PaymentCreatedEvent;

import com.example.paymentService.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;
    KafkaTemplate<Long, PaymentCreatedEvent> kafkaTemplate;
    private final KafkaProducer kafkaProducer;

    public String creatPayment( String urlReturn, String orderId) throws UnsupportedEncodingException {
        ApiResponse<?> order = restTemplate.getForObject("http://localhost:8084/api/v1/orders/"+ orderId, ApiResponse.class);
//        Order order = restTemplate.getForObject("http://orderService/api/v1/order/"+ orderId, Order.class);
//        Product product = restTemplate.getForObject("http://localhost:8083/api/v1/product/"+ order.getProductId(), Product.class);

        assert order != null;
        ObjectMapper objectMapper = new ObjectMapper();
        OrderDTO orderDTO = objectMapper.convertValue(order.getData(), OrderDTO.class);

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = Config.vnp_TmnCode;
        String orderType = "other";
        String bankCode = "NCB";

        BigDecimal total = orderDTO.getTotalPrice();
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total.multiply(new BigDecimal(100)).intValue()));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "orderInfo");
        vnp_Params.put("vnp_OrderType", orderType);
        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);
        urlReturn += Config.urlReturn;
        vnp_Params.put("vnp_ReturnUrl", urlReturn + "/"+orderId);

        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey,
                hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
    }

    public Page<Payment> getByUsername(Pageable pageable, Long userId){
        return paymentRepository.findByUserId(pageable,userId);
    }

    public void savePayment(String orderId){
        ApiResponse<OrderResponse> order = restTemplate.getForObject("http://localhost:8084/api/v1/orders/"+ orderId, ApiResponse.class);

        assert order != null;
        ObjectMapper objectMapper = new ObjectMapper();
        OrderDTO orderDTO = objectMapper.convertValue(order.getData(), OrderDTO.class);

        paymentRepository.save(Payment.builder()
                .userId(orderDTO.getUserId())
                .paidAt(now())
                .total(orderDTO.getTotalPrice())
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
        ApiResponse<OrderResponse> order = restTemplate.getForObject("http://localhost:8084/api/v1/orders/"+ orderId, ApiResponse.class);

        assert order != null;
        ObjectMapper objectMapper = new ObjectMapper();
        OrderDTO orderDTO = objectMapper.convertValue(order.getData(), OrderDTO.class);

        if (a == true){
            kafkaProducer.sendEmail(new CreateEventToNotification(orderDTO.getUserId(), orderDTO.getEmail(), orderDTO.getTotalPrice().intValueExact()));
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
