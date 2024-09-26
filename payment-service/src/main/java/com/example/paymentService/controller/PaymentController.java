package com.example.paymentService.controller;

import com.example.paymentService.dto.response.ApiResponse;
import com.example.paymentService.dto.request.PaymentRequest;
import com.example.paymentService.entity.Payment;
import com.example.paymentService.service.PaymentService;
import com.example.paymentService.service.PaypalService;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaypalService paypalService;

    @PostMapping("/create_payment")
    String creatPayment(@RequestBody PaymentRequest request) throws UnsupportedEncodingException, PayPalRESTException {
        String baseUrl = "http://localhost:3000/thankyou";

        String url = "";
        if (request.getPaymentMethod().equalsIgnoreCase("VNPAY")){
            url = paymentService.creatPayment( baseUrl, request.getOrderId(), request.getPaymentMethod());
            paymentService.savePayment(request.getOrderId());
        }else if(request.getPaymentMethod().equalsIgnoreCase("PAYPAL")){
            url = paymentService.creatPayment( baseUrl, request.getOrderId(), request.getPaymentMethod());
            paymentService.savePayment(request.getOrderId());
        }if (request.getPaymentMethod().equalsIgnoreCase("COD")){
            paymentService.savePayment(request.getOrderId());
            paymentService.updateStatusPayment(true, request.getOrderId());
            paymentService.UpdateStatusOrder(true, request.getOrderId());
            url = "http://localhost:3000/thankyou";
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

                return ResponseEntity.ok("Payment Successfully!");
            } else {
                // Giao dịch không thành công
                paymentService.updateStatusPayment(false,orderId);
                paymentService.UpdateStatusOrder(false,orderId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Failed!");
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

    @PutMapping("/updateStatus/{orderId}")
    ApiResponse<Payment> updateStatus(@PathVariable String orderId, @RequestParam Boolean isDone){
        paymentService.updateStatusPayment(isDone, orderId);
        return ApiResponse.<Payment>builder()
                .message("Update Status Payment Success")
                .build();
    }

    @GetMapping("/executePaypal")
    public ResponseEntity<String> executePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, @RequestParam("orderId") String orderId) {
        try {
            var payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                paymentService.updateStatusPayment(true, orderId);
                paymentService.UpdateStatusOrder(true, orderId);
                return ResponseEntity.ok("Payment Successfully!");
            }else {
                paymentService.updateStatusPayment(false,orderId);
                paymentService.UpdateStatusOrder(false,orderId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Faileddd!");
        }
    }
}


