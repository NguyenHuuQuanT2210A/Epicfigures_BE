package com.example.orderservice.controller;

import com.example.orderservice.dto.request.FeedbackRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.FeedbackResponse;
import com.example.orderservice.entities.OrderDetailId;
import com.example.orderservice.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
@Tag(name = "Feedback", description = "Feedback Controller")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping("/id/{id}")
    ApiResponse<FeedbackResponse> getFeedbackById(@PathVariable Long id) {
        return ApiResponse.<FeedbackResponse>builder()
                .message("Get Feedback by Id")
                .data(feedbackService.findById(id))
                .build();
    }

    @GetMapping("/orderDetail")
    ApiResponse<FeedbackResponse> getFeedbackByOrderDetailId(@RequestBody OrderDetailId orderDetailId) {
        return ApiResponse.<FeedbackResponse>builder()
                .message("Get Feedback by Order Detail Id")
                .data(feedbackService.findByOrderDetailId(orderDetailId))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<FeedbackResponse>> getFeedbackByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<FeedbackResponse>>builder()
                .message("Get Feedback by User Id")
                .data(feedbackService.findByUserId(userId))
                .build();
    }

    @GetMapping("/product/{productId}")
    ApiResponse<List<FeedbackResponse>> getFeedbackByProductId(@PathVariable Long productId) {
        return ApiResponse.<List<FeedbackResponse>>builder()
                .message("Get Feedback by Product Id")
                .data(feedbackService.findByProductId(productId))
                .build();
    }

    @PostMapping
    ApiResponse<String> createFeedback(@RequestBody FeedbackRequest request) {
        feedbackService.createFeedback(request);
        return ApiResponse.<String>builder()
                .message("Create Feedback success")
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<FeedbackResponse> updateFeedback(@PathVariable Long id, @RequestBody FeedbackRequest request) {
        return ApiResponse.<FeedbackResponse>builder()
                .message("Updated Feedback")
                .data(feedbackService.updateFeedback(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ApiResponse.<String>builder()
                .message("Deleted Feedback Successfully!")
                .build();
    }

}
