package com.example.orderservice.service.impl;

import com.example.orderservice.dto.request.OrderDetailRequest;
import com.example.orderservice.dto.request.ProductQuantityRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.OrderDetailResponse;
import com.example.orderservice.dto.response.ProductResponse;
import com.example.orderservice.entities.OrderDetail;
import com.example.orderservice.entities.OrderDetailId;
import com.example.orderservice.exception.CustomException;
import com.example.orderservice.mapper.OrderDetailMapper;

import com.example.orderservice.repositories.OrderDetailRepository;
import com.example.orderservice.service.ProductQuantityClient;
import com.example.orderservice.service.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final ProductServiceClient productServiceClient;
    private final ProductQuantityClient productQuantityClient;

    public List<OrderDetailResponse> findOrderDetailByOrderId(String id) {
        return orderDetailRepository.findOrderDetailsByOrder_Id(id).stream().map(orderDetailMapper.INSTANCE::toOrderDetailResponse).collect(Collectors.toList());
    }

    public OrderDetailResponse createOrderDetail(OrderDetailRequest request) {
        if (request == null) {
            throw new CustomException("OrderDetailDTO is null", HttpStatus.BAD_REQUEST);
        }
//        OrderDetail orderDetail = orderDetailRepository.findOrderDetailById(orderDetailDTO.getId());
//        if (orderDetail != null) {
//            if (orderDetail.getUnitPrice() == null) {
//                orderDetail.setUnitPrice(orderDetailDTO.getUnitPrice());
//            }
//            orderDetail.setQuantity(orderDetail.getQuantity() + orderDetailDTO.getQuantity());
//            return orderDetailMapper.INSTANCE.orderDetailToOrderDetailDTO(orderDetailRepository.save(orderDetail));
//        }

        OrderDetail orderDetail = orderDetailMapper.INSTANCE.toOrderDetail(request);
        orderDetail.setId(new OrderDetailId(request.getId().getOrderId(), request.getId().getProductId()));
        orderDetail.setUnitPrice(request.getUnitPrice());
        orderDetail.setQuantity(request.getQuantity());
        orderDetailRepository.save(orderDetail);

        var productQuantity = productQuantityClient.getProductQuantityByProductId(orderDetail.getId().getProductId()).getData();
        Long stockQuantity = productQuantity.getReservedQuantity() + orderDetail.getQuantity();

        productQuantityClient.updateProductQuantity(productQuantity.getId(), ProductQuantityRequest.builder().reservedQuantity(stockQuantity).build());
        return orderDetailMapper.INSTANCE.toOrderDetailResponse(orderDetail);
    }

    public OrderDetailResponse updateOrderDetail(OrderDetailRequest request) {
        if (request == null) {
            throw new CustomException("OrderDetailDTO is null", HttpStatus.BAD_REQUEST);
        }
        OrderDetail orderDetail = orderDetailRepository.findOrderDetailById(request.getId());
        if (orderDetail == null) {
            throw new CustomException("OrderDetail not found", HttpStatus.NOT_FOUND);
        }
        orderDetail.setQuantity(request.getQuantity());
        return orderDetailMapper.INSTANCE.toOrderDetailResponse(orderDetailRepository.save(orderDetail));
    }

    public void deleteOrderDetail(OrderDetailId id) {
        OrderDetail orderDetail = orderDetailRepository.findOrderDetailById(id);
        if (orderDetail == null) {
            throw new CustomException("OrderDetail not found", HttpStatus.NOT_FOUND);
        }
        orderDetailRepository.delete(orderDetail);
    }

    public OrderDetailResponse findOrderDetailById(OrderDetailId id) {
        OrderDetail orderDetail = orderDetailRepository.findOrderDetailById(id);
//        if (orderDetail == null) {
//            throw new CustomException("OrderDetail not found", HttpStatus.NOT_FOUND);
//        }
        return orderDetailMapper.INSTANCE.toOrderDetailResponse(orderDetail);
    }

    public OrderDetailResponse updateQuantity(OrderDetailId orderDetailId, Integer quantity) {
        OrderDetail orderDetail = orderDetailMapper.INSTANCE.orderDetailResponsetoOrderDetail(findOrderDetailById(orderDetailId));
        orderDetail.setQuantity(quantity);
        ApiResponse<ProductResponse> productDTO = productServiceClient.getProductById(orderDetailId.getProductId());
        orderDetail.setUnitPrice(productDTO.getData().getPrice().multiply(new BigDecimal(quantity)));
        orderDetailRepository.save(orderDetail);
        return orderDetailMapper.INSTANCE.toOrderDetailResponse(orderDetail);
    }
}
