package com.example.orderService.mapper;

import com.example.orderService.dto.OrderDTO;
import com.example.orderService.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    Order orderDTOToOrder(OrderDTO orderDTO);
}
