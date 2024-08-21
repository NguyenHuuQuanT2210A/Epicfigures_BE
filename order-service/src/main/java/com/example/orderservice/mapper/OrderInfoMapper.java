package com.example.orderservice.mapper;

import com.example.orderservice.dto.request.OrderInfoRequest;
import com.example.orderservice.dto.response.OrderInfoResponse;
import com.example.orderservice.entities.OrderInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderInfoMapper {
    OrderInfoMapper INSTANCE = Mappers.getMapper(OrderInfoMapper.class);
    OrderInfoResponse orderInfoToOrderInfoResponse(OrderInfo orderInfo);
    OrderInfo orderInfoRequestToOrderInfo(OrderInfoRequest request);
    void updateOrderInfoRequestToOrderInfo(@MappingTarget OrderInfo orderInfo, OrderInfoRequest request);
}
