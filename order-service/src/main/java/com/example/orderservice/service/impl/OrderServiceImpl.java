package com.example.orderservice.service.impl;

import com.example.orderservice.dto.request.*;
import com.example.orderservice.dto.response.*;
import com.example.orderservice.enums.OrderSimpleStatus;
import com.example.orderservice.entities.Order;
import com.example.orderservice.entities.OrderDetailId;
import com.example.orderservice.enums.PaymentType;
import com.example.orderservice.enums.Platform;
import com.example.orderservice.exception.CustomException;
import com.example.orderservice.helper.LocalDatetimeConverter;
import com.example.orderservice.mapper.FeedbackMapper;
import com.example.orderservice.mapper.OrderDetailMapper;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.repositories.FeedbackRepository;
import com.example.orderservice.repositories.OrderRepository;
import com.example.orderservice.repositories.specification.SpecSearchCriteria;
import com.example.orderservice.security.JwtTokenUtil;
import com.example.orderservice.service.*;
import com.example.orderservice.specification.OrderSpecification;
import com.example.orderservice.specification.SearchBody;
import com.example.orderservice.specification.SearchCriteria;
import com.example.orderservice.specification.SearchCriteriaOperator;
import com.example.orderservice.util.GenerateUniqueCode;
import jakarta.persistence.criteria.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.orderservice.repositories.specification.SearchOperation.OR_PREDICATE_FLAG;
import static com.example.orderservice.util.AppConst.SEARCH_SPEC_OPERATOR;
import static com.example.orderservice.util.AppConst.SORT_BY;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final UserServiceClientImpl userService;
    private final ProductServiceClientImpl productService;
    private final ProductQuantityClient productQuantityClient;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final PaymentClient paymentClient;
    private final CartClient cartClient;
    private final CartRedisClient cartRedisClient;
    private final JwtTokenUtil jwtTokenUtil;
    private final AddressOrderClient addressOrderClient;

    Specification<jakarta.persistence.criteria.Order> specification = Specification.where(null);

    @Override
    public Page<OrderResponse> getAll(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(OrderMapper.INSTANCE::toOrderResponse);
    }

    public Page<OrderResponse> findAllAndSorting(SearchBody searchBody){

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
        Page<OrderResponse> ordersPage;
        try {
           ordersPage = orderRepository.findAll(specification, sortedPage).map(orderMapper.INSTANCE::toOrderResponse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error while fetching orders", HttpStatus.BAD_REQUEST);
        }

        return ordersPage;
    }

    public OrderResponse findById(String id) {
        var orderResponse = orderMapper.toOrderResponse(findOrderById(id));
        if (orderResponse != null) {
            orderResponse.getOrderDetails().forEach(orderDetailResponse -> {
                var productResponse = productService.getProductById(orderDetailResponse.getId().getProductId());
                if (productResponse != null && productResponse.getData() != null) {
                    orderDetailResponse.setProduct(productResponse.getData());
                    if (productResponse.getData().getImages() != null) {
                        orderDetailResponse.getProduct().setImages(productResponse.getData().getImages());
                    }
                }
            });
        }
        return orderResponse;
    }

    public String createOrder(OrderRequest request, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        try {
            Order newOrder;
            newOrder = orderMapper.toOrder(request);
            String paymentResponse;

            ApiResponse<UserResponse> user = userService.getUserById(request.getUserId());
            if (user.getData() == null) {
                throw new CustomException("User not found", HttpStatus.BAD_REQUEST);
            }
            if (jwtTokenUtil.getPlatform(token).equals(Platform.MOBILE.name())){
                if (addressOrderClient.getAddressOrderById(request.getAddressOrderId()) == null) {
                    throw new CustomException("Address not found", HttpStatus.BAD_REQUEST);
                }else {
                    newOrder.setAddressOrderId(request.getAddressOrderId());
                }
            }else if (jwtTokenUtil.getPlatform(token).equals(Platform.WEB.name())){
                newOrder.setAddressOrderId(null);
            }

            try {
                do {
                    newOrder.setCodeOrder(GenerateUniqueCode.generateOrderCode());
                } while (orderRepository.existsByCodeOrder(newOrder.getCodeOrder()));

                newOrder.setTotalPrice(request.getTotalPrice());
                newOrder.setStatus(OrderSimpleStatus.CREATED);
                newOrder.setPaymentMethod(request.getPaymentMethod().toUpperCase());

                // Save the order first to get the order ID
                newOrder = orderRepository.save(newOrder);

                Set<OrderDetailRequest> orderDetails = new HashSet<>();
                List<UserAndProductId> ids = new ArrayList<>();

                Order finalNewOrder = newOrder;
                request.getCartItems().forEach(cartItem -> {
                    orderDetails.add(OrderDetailRequest.builder()
                            .order(orderMapper.toOrderResponse(finalNewOrder))
                            .id(new OrderDetailId(finalNewOrder.getId(), cartItem.getProductId()))
                            .quantity(cartItem.getQuantity())
                            .unitPrice(cartItem.getUnitPrice())
                            .build());

                    ids.add(new UserAndProductId(cartItem.getUserId(), cartItem.getProductId()));
                });

                // Convert OrderDetailDTO to OrderDetail and set them to newOrder
                newOrder.setOrderDetails(orderDetails.stream()
                        .map(orderDetailService::createOrderDetail)
                        .map(orderDetailMapper::orderDetailResponsetoOrderDetail)
                        .collect(Collectors.toSet()));
                ;
                // Save the order again with the new order details and total price
                newOrder = orderRepository.save(newOrder);
                cartClient.deleteByIds(ids);
//                cartRedisClient.deleteByIds(ids);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Error while creating order", HttpStatus.BAD_REQUEST);
            }

            paymentResponse = paymentClient.creatPayment(new PaymentRequest(newOrder.getId(), request.getPaymentMethod(), PaymentType.PAYMENT));

            return paymentResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error while creating order", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public Object updateOrder(OrderRequest request) {
        OrderResponse existingOrder = orderMapper.toOrderResponse(findOrderById(request.getId()));
        if (existingOrder == null) {
            return "Order not found";
        }
        Order updatedOrder = orderMapper.toOrder(request);
        return orderMapper.toOrderResponse(orderRepository.save(updatedOrder));
    }

    @Transactional
    public ResponseEntity<?> deleteOrder(String id) {
        orderRepository.deleteById(id);
        List<OrderDetailResponse> orderDetailResponses = orderDetailService.findOrderDetailByOrderId(id);
        for (OrderDetailResponse orderDetailResponse : orderDetailResponses) {
            orderDetailService.deleteOrderDetail(orderDetailResponse.getId());
        }
        return ResponseEntity.ok("Order deleted successfully");
    }

    public Page<OrderResponse> findByUserId(Long userId, SearchBody searchBody) {

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
        Page<OrderResponse> ordersPage;
        try {

            ordersPage = orderRepository.findOrderByUserId(userId, specification, sortedPage).map(orderMapper.INSTANCE::toOrderResponse);
            ordersPage.getContent().forEach(order -> {
                order.getOrderDetails().forEach(orderDetailResponse -> {
                    var data = productService.getProductById(orderDetailResponse.getId().getProductId());
                    orderDetailResponse.setProduct(data.getData());
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error while fetching orders", HttpStatus.BAD_REQUEST);
        }

        return ordersPage;
    }

    @Override
    public List<OrderResponse> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(orderMapper::toOrderResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse changeStatus(String id, OrderSimpleStatus newStatus) {
        var order = findOrderById(id);
        var currentStatus = order.getStatus();

        validateStatusTransition(currentStatus, newStatus);

        order.setStatus(newStatus);

        if (newStatus == OrderSimpleStatus.ONDELIVERY) {
            updateProductQuantities(order);
        }

//        if (status == OrderSimpleStatus.COMPLETE){
//            for (var orderDetail : order.getOrderDetails()){
//                feedbackRepository.save(feedbackMapper.toFeedback(FeedbackRequest.builder()
//                        .orderDetail(orderDetail)
//                        .build()));
//            }
//        }

        orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    private void validateStatusTransition(OrderSimpleStatus currentStatus, OrderSimpleStatus newStatus) {
        if (currentStatus == OrderSimpleStatus.COMPLETE && newStatus == OrderSimpleStatus.CANCEL) {
            throw new CustomException("Cannot change status from COMPLETE to CANCEL", HttpStatus.BAD_REQUEST);
        }

        if (currentStatus == OrderSimpleStatus.CANCEL) {
            throw new CustomException("Cannot change status from " + currentStatus, HttpStatus.BAD_REQUEST);
        }

        boolean isSpecialTransition = (currentStatus == OrderSimpleStatus.PAYMENT_FAILED || currentStatus == OrderSimpleStatus.PENDING) && newStatus == OrderSimpleStatus.CANCEL;
        boolean isCreatedToPending = currentStatus == OrderSimpleStatus.CREATED && newStatus == OrderSimpleStatus.PENDING;
        boolean isSequentialTransition = currentStatus.ordinal() + 1 == newStatus.ordinal();

        if (!isSequentialTransition && !isSpecialTransition && !isCreatedToPending) {
            throw new CustomException("Cannot change status from " + currentStatus + " to " + newStatus, HttpStatus.BAD_REQUEST);
        }
    }

    private void updateProductQuantities(Order order) {
        order.getOrderDetails().forEach(orderDetail -> {
            var productQuantity = productQuantityClient.getProductQuantityByProductId(orderDetail.getId().getProductId()).getData();
            productQuantityClient.updateProductQuantity(
                    productQuantity.getId(),
                    ProductQuantityRequest.builder()
                            .stockQuantity(productQuantity.getStockQuantity() - orderDetail.getQuantity())
                            .reservedQuantity(productQuantity.getReservedQuantity() - orderDetail.getQuantity())
                            .soldQuantity(productQuantity.getSoldQuantity() + orderDetail.getQuantity())
                            .build()
            );
        });
    }

    @Override
    public OrderResponse changePaymentMethod(String id, String paymentMethod) {
        var order = findOrderById(id);
        order.setPaymentMethod(paymentMethod);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse findByCode(String code) {
        var order = orderRepository.findByCodeOrder(code);
        if (order == null) {
            throw new CustomException("Order not found", HttpStatus.NOT_FOUND);
        }
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<ProductResponse> findProductsByOrderId(String orderId) {
        var order = findOrderById(orderId);
        List<ProductResponse> products = new ArrayList<>();
        for (var orderDetail : order.getOrderDetails()){
            var product = productService.getProductById(orderDetail.getId().getProductId());
            products.add(product.getData());
        }
        return products;
    }

    @Override
    public Long countOrders() {
        return orderRepository.count();
    }

    private Order findOrderById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Page<OrderResponse> findOrderByUserIdAndStatus(Long userId, OrderSimpleStatus status, Pageable pageable) {
        return orderRepository.findOrderByUserIdAndStatus(userId, status, pageable).map(orderMapper.INSTANCE::toOrderResponse);
    }

    @Override
    public CountOrderByStatus getCountByStatusOrder() {
        return CountOrderByStatus.builder()
                .created(orderRepository.countOrdersByStatus(OrderSimpleStatus.CREATED))
                .pending(orderRepository.countOrdersByStatus(OrderSimpleStatus.PENDING))
                .processing(orderRepository.countOrdersByStatus(OrderSimpleStatus.PROCESSING))
                .onDelivery(orderRepository.countOrdersByStatus(OrderSimpleStatus.ONDELIVERY))
                .delivered(orderRepository.countOrdersByStatus(OrderSimpleStatus.DELIVERED))
                .complete(orderRepository.countOrdersByStatus(OrderSimpleStatus.COMPLETE))
                .cancel(orderRepository.countOrdersByStatus(OrderSimpleStatus.CANCEL))
                .paymentFailed(orderRepository.countOrdersByStatus(OrderSimpleStatus.PAYMENT_FAILED))
                .build();
    }

    @Override
    public Page<OrderResponse> searchBySpecification(Pageable pageable, String sort, String[] order) {
        List<SpecSearchCriteria> params = new ArrayList<>();
        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
        if (order != null) {
            params.addAll(parseOrderCriteria(order, pattern));
        }

        Specification<Order> statusAndCreatedAtSortSpecification = (root, query, criteriaBuilder) -> {
            OrderSimpleStatus[] statuses = {
                    OrderSimpleStatus.PENDING, OrderSimpleStatus.PROCESSING, OrderSimpleStatus.ONDELIVERY,
                    OrderSimpleStatus.DELIVERED, OrderSimpleStatus.COMPLETE, OrderSimpleStatus.CANCEL,
                    OrderSimpleStatus.CREATED, OrderSimpleStatus.PAYMENT_FAILED
            };

            CriteriaBuilder.Case<Integer> orderCase = criteriaBuilder.selectCase();
            for (int i = 0; i < statuses.length; i++) {
                orderCase = orderCase.when(criteriaBuilder.equal(root.get("status"), statuses[i]), i);
            }

            Predicate statusPredicate = root.get("status").in((Object[]) statuses);
            List<String> validColumns = Arrays.asList(
                    "id", "userId", "codeOrder", "addressOrderId", "firstName", "lastName", "email",
                    "address", "phone", "country", "postalCode", "note", "paymentMethod", "totalPrice",
                    "status", "createdAt", "updatedAt", "deletedAt", "createdBy", "updatedBy", "deletedBy"
            );

            if (StringUtils.hasText(sort)) {
                Pattern patternSort = Pattern.compile(SORT_BY);
                Matcher matcher = patternSort.matcher(sort);
                if (matcher.find()) {
                    String columnName = matcher.group(1);
                    String orderType = matcher.group(3);
                    if (!validColumns.contains(columnName)) throw new IllegalArgumentException("Invalid sort column: " + columnName);

                    boolean isDesc = orderType.equalsIgnoreCase("desc");
                    if (columnName.equals("createdAt")) {
                        query.orderBy(
                                criteriaBuilder.asc(orderCase),
                                isDesc ? criteriaBuilder.desc(root.get(columnName)) : criteriaBuilder.asc(root.get(columnName))
                        );
                    } else {
                        query.orderBy(
                                criteriaBuilder.asc(orderCase),
                                isDesc ? criteriaBuilder.desc(root.get(columnName)) : criteriaBuilder.asc(root.get(columnName)),
                                criteriaBuilder.desc(root.get("createdAt"))
                        );
                    }
                }
            } else {
                query.orderBy(criteriaBuilder.asc(orderCase), criteriaBuilder.desc(root.get("createdAt")));
            }

            return statusPredicate;
        };

        Specification<Order> result;

        if (params.isEmpty()) {
            result = statusAndCreatedAtSortSpecification;
        } else {
            result = new com.example.orderservice.repositories.specification.OrderSpecification(params.get(0));
            for (int i = 1; i < params.size(); i++) {
                result = params.get(i).getOrPredicate()
                        ? Specification.where(result).or(new com.example.orderservice.repositories.specification.OrderSpecification(params.get(i)))
                        : Specification.where(result).and(new com.example.orderservice.repositories.specification.OrderSpecification(params.get(i)));
            }

            result = Specification.where(result).and(statusAndCreatedAtSortSpecification);
        }


        Page<Order> orders = orderRepository.findAll(Objects.requireNonNull(result), pageable);

        return orders.map(orderMapper.INSTANCE::toOrderResponse);
    }

    private List<SpecSearchCriteria> parseOrderCriteria(String[] order, Pattern pattern) {
        List<SpecSearchCriteria> params = new ArrayList<>();
        for (String o : order) {
            Matcher matcher = pattern.matcher(o);
            if (matcher.find()) {
                SpecSearchCriteria searchCriteria = new SpecSearchCriteria(null, matcher.group(2), matcher.group(4), matcher.group(6), matcher.group(1), matcher.group(3), matcher.group(5));
                if (o.startsWith(OR_PREDICATE_FLAG)) {
                    searchCriteria.setOrPredicate(true);
                }
                params.add(searchCriteria);
            }
        }
        return params;
    }
}
