package com.example.orderservice.controller;

import com.example.orderservice.dto.request.FeedbackRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.FeedbackResponse;
import com.example.orderservice.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/orderDetail/{orderDetailId}")
    ApiResponse<FeedbackResponse> getFeedbackByOrderDetailId(@PathVariable String orderDetailId) {
        return ApiResponse.<FeedbackResponse>builder()
                .message("Get Feedback by Order Detail Id")
                .data(feedbackService.findByOrderDetailId(orderDetailId))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<Page<FeedbackResponse>> getFeedbackByUserId(@RequestParam(defaultValue = "1", name = "page") int page,
                                                            @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                            @PathVariable Long userId) {
        return ApiResponse.<Page<FeedbackResponse>>builder()
                .message("Get Feedback by User Id")
                .data(feedbackService.findByUserId(PageRequest.of(page -1, limit, Sort.by("createdAt")), userId))
                .build();
    }

    @GetMapping("/product/{productId}")
    ApiResponse<Page<FeedbackResponse>> getFeedbackByProductId(@RequestParam(defaultValue = "1", name = "page") int page,
                                                               @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                               @PathVariable Long productId) {
        return ApiResponse.<Page<FeedbackResponse>>builder()
                .message("Get Feedback by Product Id")
                .data(feedbackService.findByProductId(PageRequest.of(page -1, limit, Sort.by("createdAt")), productId))
                .build();
    }

    @GetMapping("/productIdAndRateStar")
    ApiResponse<Page<FeedbackResponse>> getFeedbackByProductIdAndRateStar(@RequestParam(defaultValue = "1", name = "page") int page,
                                                                          @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                                          @RequestParam Long productId,
                                                                          @RequestParam(required = false) Integer rateStar) {
        return ApiResponse.<Page<FeedbackResponse>>builder()
                .message("Get Feedback by Product Id And Rate Star")
                .data(feedbackService.findByProductIdAndRateStar(PageRequest.of(page -1, limit, Sort.by("createdAt")), productId, rateStar))
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
