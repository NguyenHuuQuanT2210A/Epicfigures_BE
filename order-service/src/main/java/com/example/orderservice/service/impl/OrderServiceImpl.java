package com.example.orderservice.service.impl;

import com.example.orderservice.dto.request.*;
import com.example.orderservice.dto.response.*;
import com.example.orderservice.enums.OrderSimpleStatus;
import com.example.orderservice.entities.Order;
import com.example.orderservice.enums.PaymentType;
import com.example.orderservice.enums.Platform;
import com.example.orderservice.exception.CustomException;
import com.example.orderservice.helper.LocalDatetimeConverter;
import com.example.orderservice.mapper.FeedbackMapper;
import com.example.orderservice.mapper.OrderDetailMapper;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.repositories.FeedbackRepository;
import com.example.orderservice.repositories.OrderRepository;
import com.example.orderservice.repositories.ReturnItemRepository;
import com.example.orderservice.repositories.specification.SearchOperation;
import com.example.orderservice.repositories.specification.SpecSearchCriteria;
import com.example.orderservice.repositories.specification.SpecificationBuilder;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.orderservice.util.AppConst.SEARCH_SPEC_OPERATOR;
import static com.example.orderservice.util.AppConst.SORT_BY;

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
    private final CartClient cartClient;
    private final CartRedisClient cartRedisClient;
    private final JwtTokenUtil jwtTokenUtil;
    private final AddressOrderClient addressOrderClient;
    private final InventoryServiceClient inventoryServiceClient;
    private final InventoryStatusServiceClient inventoryStatusServiceClient;
    private final ReturnItemRepository returnItemRepository;
//    private final NotificationService notificationService;
    private final NotificationClient notificationClient;
    @Value("${redis.pubsub.topic.user}")
    private String topicNotificationUser;

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
                var productResponse = productService.getProductById(orderDetailResponse.getProductId()).getData();
                if (productResponse != null) {
                    orderDetailResponse.setProduct(productResponse);
                    if (productResponse.getImages() != null) {
                        orderDetailResponse.getProduct().setImages(productResponse.getImages());
                    }
                }
                orderDetailResponse.setOrderId(id);
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

            UserResponse user = userService.getUserById(request.getUserId()).getData();
            if (user == null) {
                throw new CustomException("User not found", HttpStatus.BAD_REQUEST);
            }
            if (jwtTokenUtil.getPlatform(token).equals(Platform.MOBILE.name())){
                var addressOrderResponse = addressOrderClient.getAddressOrderById(request.getAddressOrderId()).getData();
                if (addressOrderResponse == null) {
                    throw new CustomException("Address not found", HttpStatus.BAD_REQUEST);
                }else {
                    newOrder.setAddressOrderId(request.getAddressOrderId());
                    newOrder.setAddress(addressOrderResponse.getAddressRegion() + " " + addressOrderResponse.getAddressDetail());
                    newOrder.setFirstName(addressOrderResponse.getUsername());
                    newOrder.setPhone(addressOrderResponse.getPhone());
                    newOrder.setEmail(user.getEmail());
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
                            .order(finalNewOrder)
                            .productId(cartItem.getProductId())
                            .quantity(cartItem.getQuantity())
                            .unitPrice(cartItem.getUnitPrice())
                            .totalPrice(cartItem.getTotalPrice())
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
                    var data = productService.getProductById(orderDetailResponse.getProductId()).getData();
                    orderDetailResponse.setProduct(data);
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
            addInventory(order);
        }else if (newStatus == OrderSimpleStatus.DELIVERED){
            order.setDeliveredAt(LocalDateTime.now());
        }

        orderRepository.save(order);

        notificationClient.sendNotification(NotificationRequest.builder()
                .userId(order.getUserId())
                .title("order status")
                .message("Your order status has been changed to " + newStatus)
                .topicRedis(topicNotificationUser + ":" + order.getUserId())
                .type("info")
                .build());

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

    private void addInventory(Order order) {
        order.getOrderDetails().forEach(orderDetail -> {
            var product = productService.getProductById(orderDetail.getProductId()).getData();
            inventoryServiceClient.createInventory(InventoryRequest.builder()
                    .productId(product.getProductId())
                    .quantity(orderDetail.getQuantity())
//                    .unitPrice(orderDetail.getUnitPrice())
                    .inventoryStatusId(inventoryStatusServiceClient.getInventoryStatusByName("OUT").getData().getId())
                    .note("Order code :" + order.getCodeOrder() + ", Product " + product.getName())
                    .date(LocalDatetimeConverter.convertLocalDateTimeToString(LocalDateTime.now()))
                    .build()
            );
        });
    }

    @Override
    public OrderResponse changeStatusCancel(String id, String reasonCancel) {
        var order = findOrderById(id);
        if (reasonCancel == null || reasonCancel.isEmpty()) {
            throw new CustomException("Reason cancel is required", HttpStatus.BAD_REQUEST);
        }
        if (order.getStatus().ordinal() < OrderSimpleStatus.ONDELIVERY.ordinal()) {
            order.setStatus(OrderSimpleStatus.CANCEL);
            order.setReasonCancel(reasonCancel);
            orderRepository.save(order);
            return orderMapper.toOrderResponse(order);
        }else {
            throw new CustomException("Cannot cancel order", HttpStatus.BAD_REQUEST);
        }
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
            var product = productService.getProductById(orderDetail.getProductId()).getData();
            products.add(product);
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
        BigDecimal revenue = orderRepository.sumTotalPriceByStatus(OrderSimpleStatus.COMPLETE);
        BigDecimal totalReturnItem = returnItemRepository.sumTotalRefund() == null ? BigDecimal.ZERO : returnItemRepository.sumTotalRefund();
        BigDecimal totalPriceCancel = orderRepository.sumTotalPriceByStatusCancelAndPaymentMethod() == null ? BigDecimal.ZERO : orderRepository.sumTotalPriceByStatusCancelAndPaymentMethod();
        revenue = (revenue.subtract(totalReturnItem)).subtract(totalPriceCancel);

        return CountOrderByStatus.builder()
                .created(orderRepository.countOrdersByStatus(OrderSimpleStatus.CREATED))
                .pending(orderRepository.countOrdersByStatus(OrderSimpleStatus.PENDING))
                .processing(orderRepository.countOrdersByStatus(OrderSimpleStatus.PROCESSING))
                .onDelivery(orderRepository.countOrdersByStatus(OrderSimpleStatus.ONDELIVERY))
                .delivered(orderRepository.countOrdersByStatus(OrderSimpleStatus.DELIVERED))
                .complete(orderRepository.countOrdersByStatus(OrderSimpleStatus.COMPLETE))
                .cancel(orderRepository.countOrdersByStatus(OrderSimpleStatus.CANCEL))
                .paymentFailed(orderRepository.countOrdersByStatus(OrderSimpleStatus.PAYMENT_FAILED))
                .revenue(revenue)
                .build();
    }

    @Override
    public Page<OrderResponse> searchBySpecification(Pageable pageable, String sort, String[] order) {
        SpecificationBuilder builder = new SpecificationBuilder();
        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
        if (order != null) {
            parseCriteriaBuilder(builder, order, pattern, false, null);
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
                    "status", "createdAt", "updatedAt", "deletedAt", "deliveredAt", "createdBy", "updatedBy", "deletedBy"
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

        if (builder.params.isEmpty()) {
            return orderRepository.findAll(statusAndCreatedAtSortSpecification, pageable).map(orderMapper.INSTANCE::toOrderResponse);
        } else {
            result = Specification.where(build(builder.params)).and(statusAndCreatedAtSortSpecification);
        }

        Page<Order> orders = orderRepository.findAll(Objects.requireNonNull(result), pageable);

        return orders.map(orderMapper.INSTANCE::toOrderResponse);
    }

    private void parseCriteriaBuilder(SpecificationBuilder builder, String[] entities, Pattern pattern, boolean isJoinQuery, String joinEntity) {
        for (String e : entities) {
            Matcher matcher = pattern.matcher(e);
            if (matcher.find()) {
                if (e.startsWith(SearchOperation.OR_PREDICATE_FLAG)) {
                    builder.withOrder(SearchOperation.OR_PREDICATE_FLAG, matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6), isJoinQuery, joinEntity);
                }else {
                    builder.withOrder(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6), isJoinQuery, joinEntity);
                }
            }
        }
    }

    private Specification<Order> build(List<SpecSearchCriteria> params){
        Specification<Order> specification = new com.example.orderservice.repositories.specification.order.OrderSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            specification = params.get(i).getOrPredicate()
                    ? Specification.where(specification).or(new com.example.orderservice.repositories.specification.order.OrderSpecification(params.get(i)))
                    : Specification.where(specification).and(new com.example.orderservice.repositories.specification.order.OrderSpecification(params.get(i)));
        }
        return specification;
    }
}
