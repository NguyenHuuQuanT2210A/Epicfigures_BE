package com.example.orderservice.service;

import com.example.orderservice.dto.request.FeedbackRequest;
import com.example.orderservice.dto.response.FeedbackResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse findById(Long id);
    FeedbackResponse findByOrderDetailId(String orderDetailId);

    Page<FeedbackResponse> findByUserId(Pageable pageable, Long userId);
    Page<FeedbackResponse> findByProductId(Pageable pageable, Long productId);

    void createFeedback(FeedbackRequest request);
    FeedbackResponse updateFeedback(Long id, FeedbackRequest request);
    void deleteFeedback(Long id);

    Page<FeedbackResponse> findByProductIdAndRateStar(Pageable pageable, Long productId, Integer rateStar);
}
