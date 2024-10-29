package com.example.orderservice.service.impl;

import com.example.orderservice.dto.request.OrderDetailRequest;
import com.example.orderservice.dto.request.ProductQuantityRequest;
import com.example.orderservice.dto.response.OrderDetailResponse;
import com.example.orderservice.dto.response.ProductResponse;
import com.example.orderservice.entities.OrderDetail;
import com.example.orderservice.exception.CustomException;
import com.example.orderservice.mapper.OrderDetailMapper;

import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.repositories.OrderDetailRepository;
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
    private final OrderMapper orderMapper;
    private final ProductServiceClient productServiceClient;

    public List<OrderDetailResponse> findOrderDetailByOrderId(String id) {
        return orderDetailRepository.findOrderDetailsByOrder_Id(id).stream().map(orderDetail -> {
            var product = getProductById(orderDetail.getProductId());
            var orderDetailResponse = orderDetailMapper.INSTANCE.toOrderDetailResponse(orderDetail);
            orderDetailResponse.setProduct(product);
            return orderDetailResponse;
        }).collect(Collectors.toList());
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

        OrderDetail orderDetail = OrderDetail.builder()
                .order(request.getOrder())
                .quantity(request.getQuantity())
                .returnableQuantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .totalPrice(request.getTotalPrice())
                .productId(request.getProductId())
                .build();

        orderDetailRepository.save(orderDetail);

        var product = productServiceClient.getProductById(orderDetail.getProductId());
        Integer reservedQuantity = product.getData().getReservedQuantity() + orderDetail.getQuantity();

        productServiceClient.updateQuantity(product.getData().getProductId(), ProductQuantityRequest.builder().reservedQuantity(reservedQuantity).build());

        return orderDetailMapper.INSTANCE.toOrderDetailResponse(orderDetail);
    }

    public OrderDetailResponse updateOrderDetail(OrderDetailRequest request, String id) {
        if (request == null) {
            throw new CustomException("OrderDetailDTO is null", HttpStatus.BAD_REQUEST);
        }
        OrderDetail orderDetail = orderDetailRepository.findOrderDetailById(id);
        if (orderDetail == null) {
            throw new CustomException("OrderDetail not found", HttpStatus.NOT_FOUND);
        }
        orderDetail.setQuantity(request.getQuantity());
        return orderDetailMapper.INSTANCE.toOrderDetailResponse(orderDetailRepository.save(orderDetail));
    }

    public void deleteOrderDetail(String id) {
        OrderDetail orderDetail = orderDetailRepository.findOrderDetailById(id);
        if (orderDetail == null) {
            throw new CustomException("OrderDetail not found", HttpStatus.NOT_FOUND);
        }
        orderDetailRepository.delete(orderDetail);
    }

    public OrderDetailResponse findOrderDetailById(String id) {
        OrderDetail orderDetail = orderDetailRepository.findOrderDetailById(id);
//        if (orderDetail == null) {
//            throw new CustomException("OrderDetail not found", HttpStatus.NOT_FOUND);
//        }
        var product = getProductById(orderDetail.getProductId());
        var orderDetailResponse = orderDetailMapper.INSTANCE.toOrderDetailResponse(orderDetail);
        orderDetailResponse.setProduct(product);
        return orderDetailResponse;
    }

    public void updateQuantity(String id, Integer quantity) {
        OrderDetail orderDetail = orderDetailRepository.findOrderDetailById(id);
        var productDTO = getProductById(orderDetail.getProductId());
        orderDetail.setQuantity(quantity);
        orderDetail.setReturnableQuantity(quantity);
        orderDetail.setUnitPrice(productDTO.getPrice());
        orderDetail.setTotalPrice(productDTO.getPrice().multiply(new BigDecimal(quantity)));
        orderDetailRepository.save(orderDetail);
    }

    private ProductResponse getProductById(Long productId) {
        return productServiceClient.getProductById(productId).getData();
    }
}
