package com.example.orderservice.service;

import com.example.orderservice.dto.request.FeedbackRequest;
import com.example.orderservice.dto.response.FeedbackResponse;
import com.example.orderservice.entities.OrderDetailId;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse findById(Long id);
    FeedbackResponse findByOrderDetailId(OrderDetailId orderDetailId);

    List<FeedbackResponse> findByUserId(Long userId);
    List<FeedbackResponse> findByProductId(Long productId);

    void createFeedback(FeedbackRequest request);
    FeedbackResponse updateFeedback(Long id, FeedbackRequest request);
    void deleteFeedback(Long id);
}
