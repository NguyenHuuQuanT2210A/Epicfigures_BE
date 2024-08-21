package com.example.orderservice.service.impl;

import com.example.common.enums.ErrorCode;
import com.example.orderservice.dto.request.FeedbackRequest;
import com.example.orderservice.dto.response.FeedbackResponse;
import com.example.orderservice.entities.Feedback;
import com.example.orderservice.entities.OrderDetailId;
import com.example.orderservice.exception.AppException;
import com.example.orderservice.mapper.FeedbackMapper;
import com.example.orderservice.mapper.OrderDetailMapper;
import com.example.orderservice.repositories.FeedbackRepository;
import com.example.orderservice.service.FeedbackService;
import com.example.orderservice.service.OrderDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackServiceImpl implements FeedbackService {
    FeedbackRepository feedbackRepository;
    FeedbackMapper feedbackMapper;
    OrderDetailService orderDetailService;
    OrderDetailMapper orderDetailMapper;

    @Override
    public FeedbackResponse findById(Long id) {
        return feedbackMapper.toFeedbackResponse(getFeedbackById(id));
    }

    @Override
    public FeedbackResponse findByOrderDetailId(OrderDetailId orderDetailId) {
        var orderDetail = orderDetailMapper.orderDetailDTOToOrderDetail(orderDetailService.findOrderDetailById(orderDetailId));
        return feedbackMapper.toFeedbackResponse(feedbackRepository.findByOrderDetail(orderDetail));
    }

    @Override
    @Transactional
    public List<FeedbackResponse> findByUserId(Long userId) {
        List<FeedbackResponse> feedbackResponses = new ArrayList<>();

        List<Feedback> feedbacks = feedbackRepository.findByUserId(userId);
        for (Feedback feedback : feedbacks) {
            feedbackResponses.add(feedbackMapper.toFeedbackResponse(feedback));
        }

        return feedbackResponses;
    }

    @Override
    @Transactional
    public List<FeedbackResponse> findByProductId(Long productId) {
        List<FeedbackResponse> feedbackResponses = new ArrayList<>();
        List<Feedback> feedbacks = feedbackRepository.findByProductId(productId);
        for (Feedback feedback : feedbacks) {
            feedbackResponses.add(feedbackMapper.toFeedbackResponse(feedback));
        }
        return feedbackResponses;
    }

    @Override
    public void createFeedback(FeedbackRequest request) {
        try {
            Feedback feedback = feedbackMapper.toFeedback(request);
            log.info("Feedback {}", feedback.toString());
            feedbackRepository.save(feedback);
        }catch (Exception e) {
            throw new RuntimeException("error while create feedback");
        }
    }

    @Override
    public FeedbackResponse updateFeedback(Long id, FeedbackRequest request) {
        Feedback feedbackUpdate = getFeedbackById(id);
        feedbackMapper.updateFeedback(feedbackUpdate, request);
        try {
            return feedbackMapper.toFeedbackResponse(feedbackRepository.save(feedbackUpdate));
        }catch (Exception e) {
            throw new RuntimeException("error while updating feedback");
        }
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    private Feedback getFeedbackById(Long id){
        return feedbackRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_EXISTED));
    }
}
