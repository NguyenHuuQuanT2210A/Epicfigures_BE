package com.example.orderservice.service;

import com.example.common.dto.ProductDTO;
import com.example.common.enums.OrderSimpleStatus;
import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.dto.OrderDetailDTO;
import com.example.common.dto.UserDTO;
import com.example.orderservice.dto.request.FeedbackRequest;
import com.example.orderservice.dto.request.PaymentRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.entities.Order;
import com.example.orderservice.entities.OrderDetailId;
import com.example.orderservice.exception.CustomException;
import com.example.orderservice.helper.LocalDatetimeConverter;
import com.example.orderservice.mapper.FeedbackMapper;
import com.example.orderservice.mapper.OrderDetailMapper;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.repositories.FeedbackRepository;
import com.example.orderservice.repositories.OrderRepository;
import com.example.orderservice.specification.OrderSpecification;
import com.example.orderservice.specification.SearchBody;
import com.example.orderservice.specification.SearchCriteria;
import com.example.orderservice.specification.SearchCriteriaOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final UserServiceClientImpl userService;
    private final ProductServiceClientImpl productService;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final PaymentClient paymentClient;

    Specification<jakarta.persistence.criteria.Order> specification = Specification.where(null);

    @Override
    public Page<OrderDTO> getAll(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(OrderMapper.INSTANCE::orderToOrderDTO);
    }

    public Page<OrderDTO> findAllAndSorting(SearchBody searchBody){

        if (searchBody.getStatus() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("status", SearchCriteriaOperator.EQUALS, searchBody.getStatus())));
        }

        if (searchBody.getStartDate() != null){
            LocalDateTime startDate = LocalDatetimeConverter.toLocalDateTime(searchBody.getStartDate(), true);
            specification = specification.and(new OrderSpecification(new SearchCriteria("createdAt", SearchCriteriaOperator.GREATER_THAN_OR_EQUALS,startDate)));
        }

        if (searchBody.getEndDate() != null){
            LocalDateTime endDate = LocalDatetimeConverter.toLocalDateTime(searchBody.getEndDate(), false);
            specification = specification.and(new OrderSpecification(new SearchCriteria("createdAt", SearchCriteriaOperator.LESS_THAN_OR_EQUALS,endDate)));
        }

        if (searchBody.getProductName() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("", SearchCriteriaOperator.PRODUCT_JOIN_PRODUCT_NAME_LIKE, searchBody.getProductName().trim())));
        }

        if (searchBody.getCustomerName() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("accountName", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerName().trim())));
        }

        if (searchBody.getCustomerEmail() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("email", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerEmail().trim())));
        }

        if (searchBody.getCustomerPhone() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("phoneNumber", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerPhone().trim())));
        }

        if (searchBody.getOrderId() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("id", SearchCriteriaOperator.EQUALS, searchBody.getOrderId().trim())));
        }

        List<Sort.Order> orders = new ArrayList<>();

        Sort.Order order1;
        order1 = new Sort.Order(Sort.Direction.DESC, "createdAt");
        if (searchBody.getTimeSorting() !=null){
            if (searchBody.getTimeSorting().contains("oldest")){
                order1 = new Sort.Order(Sort.Direction.ASC, "createdAt");
            }
        }
        if (searchBody.getPriceSorting() !=null){
            Sort.Order order2;
            if (searchBody.getPriceSorting().contains("descending")){
               order2 = new Sort.Order(Sort.Direction.DESC, "totalPrice");
            }else {
               order2 = new Sort.Order(Sort.Direction.ASC, "totalPrice");
            }
            orders.add(order2);
        }

        orders.add(order1);
        Pageable sortedPage = PageRequest.of(searchBody.getPage()-1, searchBody.getLimit(), Sort.by(orders));
        Page<OrderDTO> ordersPage;
        try {
           ordersPage = orderRepository.findAll(specification, sortedPage).map(orderMapper.INSTANCE::orderToOrderDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error while fetching orders", HttpStatus.BAD_REQUEST);
        }

        return ordersPage;
    }

    public OrderDTO findById(String id){
        return orderRepository.findById(id).map(orderMapper::orderToOrderDTO).orElse(null);
    }

    public String createOrder(OrderDTO orderDTO){
        try {
            Order newOrder;
            String paymentResponse;

            ApiResponse<UserDTO> user = userService.getUserById(orderDTO.getUserId());
            if (user.getData() == null) {
                throw new CustomException("User not found", HttpStatus.BAD_REQUEST);
            }
            try {
                newOrder = orderMapper.orderDTOToOrder(orderDTO);
                newOrder.setTotalPrice(BigDecimal.ZERO);
                newOrder.setStatus(OrderSimpleStatus.CREATED);

                // Save the order first to get the order ID
                newOrder = orderRepository.save(newOrder);

                Set<OrderDetailDTO> orderDetails = new HashSet<>();
                Set<Long> productIds = new HashSet<>();

                Order finalNewOrder = newOrder;
                orderDTO.getCartItems().forEach(cartItem -> {
                    orderDetails.add(OrderDetailDTO.builder()
                            .order(finalNewOrder)
                            .id(new OrderDetailId(finalNewOrder.getId(), cartItem.getProductId()))
                            .quantity(cartItem.getQuantity())
                            .unitPrice(cartItem.getUnitPrice())
                            .build());

                    productIds.add(cartItem.getProductId());
                });

                ApiResponse<List<ProductDTO>> products = productService.getProductsByIds(productIds);

                Map<Long, BigDecimal> productPriceMap = products.getData().stream()
                        .collect(Collectors.toMap(ProductDTO::getProductId, ProductDTO::getPrice));

                // Set unit price for each order detail
                orderDetails.forEach(orderDetailDTO -> {
                    BigDecimal price = productPriceMap.get(orderDetailDTO.getId().getProductId());
                    orderDetailDTO.setUnitPrice(price);
                });

                // Convert OrderDetailDTO to OrderDetail and set them to newOrder
                newOrder.setOrderDetails(orderDetails.stream()
                        .map(orderDetailService::createOrderDetail)
                        .map(orderDetailMapper::orderDetailDTOToOrderDetail)
                        .collect(Collectors.toSet()));

                BigDecimal totalPrice = newOrder.getTotalPrice().add(orderDetails.stream()
                        .map(orderDetail -> orderDetail.getUnitPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

                newOrder.setTotalPrice(totalPrice);
                // Save the order again with the new order details and total price
                newOrder = orderRepository.save(newOrder);

            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Error while creating order", HttpStatus.BAD_REQUEST);
            }

            paymentResponse = paymentClient.creatPayment(new PaymentRequest(newOrder.getId(), orderDTO.getPayment_method()));

            return paymentResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error while creating order", HttpStatus.BAD_REQUEST);
        }
    }

    public Object updateOrder(OrderDTO order) {
        OrderDTO existingOrder = findById(order.getId());
        if (existingOrder == null) {
            return "Order not found";
        }
        Order updatedOrder = orderMapper.orderDTOToOrder(order);
        return orderMapper.orderToOrderDTO(orderRepository.save(updatedOrder));
    }

    public ResponseEntity<?> deleteOrder(String id) {
        orderRepository.deleteById(id);
        List<OrderDetailDTO> orderDetailDTOs = orderDetailService.findOrderDetailByOrderId(id);
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOs) {
            orderDetailService.deleteOrderDetail(orderDetailDTO.getId());
        }
        return ResponseEntity.ok("Order deleted successfully");
    }

    public Page<OrderDTO> findByUserId(Long userId, SearchBody searchBody) {

        if (searchBody.getStatus() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("status", SearchCriteriaOperator.EQUALS, searchBody.getStatus())));
        }

        if (searchBody.getStartDate() != null){
            LocalDateTime startDate = LocalDatetimeConverter.toLocalDateTime(searchBody.getStartDate(), true);
            specification = specification.and(new OrderSpecification(new SearchCriteria("createdAt", SearchCriteriaOperator.GREATER_THAN_OR_EQUALS,startDate)));
        }

        if (searchBody.getEndDate() != null){
            LocalDateTime endDate = LocalDatetimeConverter.toLocalDateTime(searchBody.getEndDate(), false);
            specification = specification.and(new OrderSpecification(new SearchCriteria("createdAt", SearchCriteriaOperator.LESS_THAN_OR_EQUALS,endDate)));
        }

        if (searchBody.getProductName() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("", SearchCriteriaOperator.PRODUCT_JOIN_PRODUCT_NAME_LIKE, searchBody.getProductName().trim())));
        }

        if (searchBody.getCustomerName() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("accountName", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerName().trim())));
        }

        if (searchBody.getCustomerEmail() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("email", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerEmail().trim())));
        }

        if (searchBody.getCustomerPhone() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("phoneNumber", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerPhone().trim())));
        }

        if (searchBody.getOrderId() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("id", SearchCriteriaOperator.EQUALS, searchBody.getOrderId().trim())));
        }

        List<Sort.Order> orders = new ArrayList<>();

        Sort.Order order1;
        order1 = new Sort.Order(Sort.Direction.DESC, "createdAt");
        if (searchBody.getTimeSorting() !=null){
            if (searchBody.getTimeSorting().contains("oldest")){
                order1 = new Sort.Order(Sort.Direction.ASC, "createdAt");
            }
        }
        if (searchBody.getPriceSorting() !=null){
            Sort.Order order2;
            if (searchBody.getPriceSorting().contains("descending")){
                order2 = new Sort.Order(Sort.Direction.DESC, "totalPrice");
            }else {
                order2 = new Sort.Order(Sort.Direction.ASC, "totalPrice");
            }
            orders.add(order2);
        }

        orders.add(order1);
        Pageable sortedPage = PageRequest.of(searchBody.getPage()-1, searchBody.getLimit(), Sort.by(orders));
        Page<OrderDTO> ordersPage;
        try {
            ordersPage = orderRepository.findOrderByUserId(userId, specification, sortedPage).map(orderMapper.INSTANCE::orderToOrderDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error while fetching orders", HttpStatus.BAD_REQUEST);
        }

        return ordersPage;
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public OrderDTO findCartByUserId(Long id) {
        return orderMapper.INSTANCE.orderToOrderDTO(orderRepository.findOrderByUserIdAndStatus(id, OrderSimpleStatus.CREATED));
    }

    @Override
    public OrderDTO changeStatus(String id, OrderSimpleStatus status) {
        var order = orderMapper.orderDTOToOrder(findById(id));

        if (order.getStatus().ordinal() > status.ordinal()) {
            throw new IllegalStateException("Cannot change status from " + order.getStatus() + " to " + status);
        }

        order.setStatus(status);
        if (status == OrderSimpleStatus.COMPLETE){
            for (var orderDetail : order.getOrderDetails()){
                feedbackRepository.save(feedbackMapper.toFeedback(FeedbackRequest.builder()
                        .orderDetail(orderDetail)
                        .build()));
            }
        }
        orderRepository.save(order);
        return orderMapper.orderToOrderDTO(order);
    }
}
