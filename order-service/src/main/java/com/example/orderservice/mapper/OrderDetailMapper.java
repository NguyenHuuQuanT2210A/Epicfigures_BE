package com.example.orderservice.mapper;

import com.example.orderservice.dto.request.OrderDetailRequest;
import com.example.orderservice.dto.response.OrderDetailResponse;
import com.example.orderservice.entities.OrderDetail;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);
    @Mapping(source = "order.id", target = "orderId")
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
    OrderDetail toOrderDetail(OrderDetailRequest request);
    @Mapping(source = "product.productId", target = "productId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderDetail orderDetailResponsetoOrderDetail(OrderDetailResponse response);
}
