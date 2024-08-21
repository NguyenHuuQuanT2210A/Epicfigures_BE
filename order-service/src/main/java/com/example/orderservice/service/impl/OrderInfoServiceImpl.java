package com.example.orderservice.service.impl;

import com.example.orderservice.dto.request.OrderInfoRequest;
import com.example.orderservice.dto.response.OrderInfoResponse;
import com.example.orderservice.entities.Order;
import com.example.orderservice.entities.OrderInfo;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.mapper.OrderInfoMapper;
import com.example.orderservice.repositories.OrderInfoRepository;
import com.example.orderservice.service.OrderInfoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderInfoServiceImpl implements OrderInfoService {
    OrderInfoRepository orderInfoRepository;
    OrderInfoMapper orderInfoMapper;

    @Override
    public OrderInfoResponse findById(String id) {
        return orderInfoMapper.orderInfoToOrderInfoResponse(getOrderInfoById(id));
    }

    @Override
    public void createOrderInfo(OrderInfoRequest request) {
        try {
            OrderInfo orderInfo = orderInfoMapper.orderInfoRequestToOrderInfo(request);
            orderInfoRepository.save(orderInfo);
        }catch (Exception e) {
            throw new RuntimeException("error while updating order info");
        }
    }

    @Override
    public OrderInfoResponse updateOrderInfo(String id, OrderInfoRequest request) {
        OrderInfo orderInfoUpdate = getOrderInfoById(id);
        orderInfoMapper.updateOrderInfoRequestToOrderInfo(orderInfoUpdate, request);
        try {
            return orderInfoMapper.orderInfoToOrderInfoResponse(orderInfoRepository.save(orderInfoUpdate));
        }catch (Exception e) {
            throw new RuntimeException("error while updating order info");
        }
    }

    @Override
    public OrderInfoResponse findOrderInfoByOrder(Order order) {
        OrderInfo orderInfo = orderInfoRepository.findOrderInfoByOrderId(order.getId());
        return orderInfoMapper.orderInfoToOrderInfoResponse(orderInfo);
    }

    private OrderInfo getOrderInfoById(String id){
        return orderInfoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("OrderInfo not found"));
    }
}
