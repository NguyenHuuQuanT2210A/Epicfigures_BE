package com.example.orderservice.mapper;

import com.example.orderservice.dto.request.OrderDetailRequest;
import com.example.orderservice.dto.response.OrderDetailResponse;
import com.example.orderservice.entities.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
    OrderDetail toOrderDetail(OrderDetailRequest request);
    OrderDetail orderDetailResponsetoOrderDetail(OrderDetailResponse response);
}
