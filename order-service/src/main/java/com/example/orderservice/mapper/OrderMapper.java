package com.example.orderservice.mapper;

import com.example.common.dto.OrderDTO;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    OrderDTO toOrderDTO(Order order);
    Order orderDTOToOrder(OrderRequest request);
}
