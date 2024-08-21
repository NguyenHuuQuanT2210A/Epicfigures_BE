package com.example.orderservice.mapper;

import com.example.orderservice.dto.request.FeedbackRequest;
import com.example.orderservice.dto.response.FeedbackResponse;
import com.example.orderservice.entities.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);
    FeedbackResponse toFeedbackResponse(Feedback feedback);
    Feedback toFeedback(FeedbackRequest request);
    void updateFeedback(@MappingTarget Feedback feedback, FeedbackRequest request);
}
