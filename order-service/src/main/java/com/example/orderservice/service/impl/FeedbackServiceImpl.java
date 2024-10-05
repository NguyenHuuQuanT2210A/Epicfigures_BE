package com.example.orderservice.service.impl;

import com.example.orderservice.enums.ErrorCode;
import com.example.orderservice.dto.request.FeedbackRequest;
import com.example.orderservice.dto.response.FeedbackResponse;
import com.example.orderservice.entities.Feedback;
import com.example.orderservice.entities.OrderDetailId;
import com.example.orderservice.exception.AppException;
import com.example.orderservice.exception.CustomException;
import com.example.orderservice.mapper.FeedbackMapper;
import com.example.orderservice.mapper.OrderDetailMapper;
import com.example.orderservice.repositories.FeedbackRepository;
import com.example.orderservice.service.FeedbackService;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.ProductServiceClient;
import com.example.orderservice.service.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final OrderDetailService orderDetailService;
    private final OrderDetailMapper orderDetailMapper;
    private final OrderService orderService;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

    @Override
    public FeedbackResponse findById(Long id) {
        return feedbackMapper.toFeedbackResponse(getFeedbackById(id));
    }

    @Override
    public FeedbackResponse findByOrderDetailId(OrderDetailId orderDetailId) {
        var orderDetail = orderDetailMapper.orderDetailResponsetoOrderDetail(orderDetailService.findOrderDetailById(orderDetailId));
        return feedbackMapper.toFeedbackResponse(feedbackRepository.findByOrderDetail(orderDetail));
    }

    @Override
    @Transactional
    public Page<FeedbackResponse> findByUserId(Pageable pageable, Long userId) {
        userServiceClient.getUserById(userId);
        return feedbackRepository.findByUserId(userId, pageable).map(feedbackMapper::toFeedbackResponse);
    }

    @Override
    @Transactional
    public Page<FeedbackResponse> findByProductId(Pageable pageable, Long productId) {
        productServiceClient.getProductById(productId);
        return feedbackRepository.findByProductId(productId, pageable).map(feedbackMapper::toFeedbackResponse);
    }

    @Override
    public void createFeedback(FeedbackRequest request) {
        try {
            Feedback feedback = feedbackMapper.toFeedback(request);
            var oder = orderService.findById(request.getOrderDetail().getId().getOrderId());
            feedback.setUserId(oder.getUserId());
            feedbackRepository.save(feedback);
        }catch (Exception e) {
            throw new CustomException("error while create feedback", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public FeedbackResponse updateFeedback(Long id, FeedbackRequest request) {
        Feedback feedbackUpdate = getFeedbackById(id);
        feedbackMapper.updateFeedback(feedbackUpdate, request);
        try {
            return feedbackMapper.toFeedbackResponse(feedbackRepository.save(feedbackUpdate));
        }catch (Exception e) {
            throw new CustomException("error while updating feedback", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Override
    public Page<FeedbackResponse> findByProductIdAndRateStar(Pageable pageable, Long productId, Integer rateStar) {
        productServiceClient.getProductById(productId);
        if (rateStar == null){
            return feedbackRepository.findByProductId(productId, pageable).map(feedbackMapper::toFeedbackResponse);
        }
        return feedbackRepository.findByProductIdAndRateStar(productId, rateStar, pageable).map(feedbackMapper::toFeedbackResponse);
    }

    private Feedback getFeedbackById(Long id){
        return feedbackRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_EXISTED));
    }
}
