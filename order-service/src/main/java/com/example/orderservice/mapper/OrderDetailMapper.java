package com.example.orderservice.mapper;

import com.example.orderservice.dto.OrderDetailDTO;
import com.example.orderservice.entities.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);
    OrderDetailDTO orderDetailToOrderDetailDTO(OrderDetail orderDetail);
    OrderDetail orderDetailDTOToOrderDetail(OrderDetailDTO orderDetailDTO);
}
