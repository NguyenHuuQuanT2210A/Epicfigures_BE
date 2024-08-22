package com.example.paymentService.controller;

import com.example.paymentService.dto.ApiResponse;
import com.example.paymentService.dto.OrderInfoRequest;
import com.example.paymentService.dto.PaymentRequest;
import com.example.paymentService.entity.Payment;
import com.example.paymentService.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create_payment")
    String creatPayment(@RequestBody PaymentRequest request) throws UnsupportedEncodingException {
//        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/api/v1/payment";
        String baseUrl = "http://localhost:8080";

        String url = "";
        if (request.getPayment_method().equalsIgnoreCase("VNPAY")){
            url = paymentService.creatPayment( baseUrl, request.getOrderId());
            Map<String, String> response = new HashMap<>();
            response.put("paymentUrl", url);
            paymentService.savePayment(request);
        }if (request.getPayment_method().equalsIgnoreCase("COD")){
            paymentService.savePayment(request);
            url = "payment success";
        }
        return url;
    }

    @GetMapping("/vnPayReturn/{orderId}")
    ResponseEntity<String> handleVnPayReturn(@RequestParam(name = "vnp_ResponseCode") String responseCode,
                                                    @PathVariable String orderId
                                                    ) {
            // Xử lý phản hồi từ VNPAY

            if ("00".equals(responseCode)) {
                // Giao dịch thành công
                paymentService.updateStatusPayment(true, orderId);
                paymentService.UpdateStatusOrder(true, orderId);

                return ResponseEntity.ok("Giao dịch thành công");
            } else {
                // Giao dịch không thành công
                paymentService.updateStatusPayment(false,orderId);
                paymentService.UpdateStatusOrder(false,orderId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Giao dịch không thành công");
            }
    }

    @GetMapping("/id/{userId}")
    ApiResponse<Page<Payment>> getByUsername(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "5") int limit,
                                             @PathVariable Long userId){
        return ApiResponse.<Page<Payment>>builder()
                .message("Get Payment by UserName")
                .data(paymentService.getByUsername(PageRequest.of(page-1, limit),userId))
                .build();
    }
     @GetMapping("/{id}")
     ApiResponse<Payment> getById(@PathVariable Long id){
         return ApiResponse.<Payment>builder()
                 .message("Get Payment by Id")
                 .data(paymentService.getById(id))
                 .build();
     }
    @GetMapping("/order/{id}")
    ApiResponse<Payment> getByOrderId(@PathVariable String id){
        return ApiResponse.<Payment>builder()
                .message("Get Payment by OrderId")
                .data(paymentService.getByOrderId(id))
                .build();
    }

}


