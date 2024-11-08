package com.example.orderservice.service.impl;

import com.example.orderservice.config.FirebaseStorageProperties;
import com.example.orderservice.config.KafkaProducer;
import com.example.orderservice.dto.request.*;
import com.example.orderservice.dto.response.OrderDetailResponse;
import com.example.orderservice.dto.response.ReturnItemResponse;
import com.example.orderservice.entities.ReturnItem;
import com.example.orderservice.enums.ReturnItemStatus;
import com.example.orderservice.exception.CustomException;
import com.example.orderservice.helper.LocalDatetimeConverter;
import com.example.orderservice.mapper.OrderDetailMapper;
import com.example.orderservice.mapper.ReturnItemMapper;
import com.example.orderservice.repositories.ReturnItemRepository;
import com.example.orderservice.repositories.specification.SearchOperation;
import com.example.orderservice.repositories.specification.SpecSearchCriteria;
import com.example.orderservice.repositories.specification.SpecificationBuilder;
import com.example.orderservice.repositories.specification.returnItem.ReturnItemSpecification;
import com.example.orderservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.orderservice.util.AppConst.SEARCH_SPEC_OPERATOR;
import static com.example.orderservice.util.AppConst.SORT_BY;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReturnItemServiceImpl implements ReturnItemService {
    private final ReturnItemRepository returnItemRepository;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ReturnItemMapper returnItemMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final FirebaseService firebaseService;
    private final FirebaseStorageProperties firebaseStorageProperties;
    private final KafkaProducer kafkaProducer;
    private final ProductServiceClientImpl productService;
    private final InventoryServiceClient inventoryServiceClient;
    private final InventoryStatusServiceClient inventoryStatusServiceClient;
    private final NotificationClient notificationClient;
    @Value("${redis.pubsub.topic.user}")
    private String topicNotificationUser;

    @Override
    public Page<ReturnItemResponse> getAllReturnItems(Pageable pageable) {
        return returnItemRepository.findByDeletedAtIsNull(pageable).map(returnItemMapper::toReturnItemResponse);
    }

    @Override
    public ReturnItemResponse getReturnItemById(Long id) {
        return returnItemMapper.toReturnItemResponse(findReturnItemById(id));
    }

    @Override
    public Page<ReturnItemResponse> getReturnItemByUserId(Long userId, Pageable pageable) {
        return returnItemRepository.findByUserIdAndDeletedAtIsNull(userId, pageable).map(returnItemMapper::toReturnItemResponse);
    }

    @Override
    public Long addReturnItem(ReturnItemRequest request, List<MultipartFile> imageFiles) throws IOException {
        OrderDetailResponse orderDetailResponse = orderDetailService.findOrderDetailById(request.getOrderDetailId());
        if (orderDetailResponse == null) {
            throw new CustomException("Can not find orderDetail with id " + request.getOrderDetailId(), HttpStatus.NOT_FOUND);
        }

        var returnPeriodDays = productService.getProductById(orderDetailResponse.getProductId()).getData().getReturnPeriodDays();
        if (orderService.findById(orderDetailResponse.getOrderId()).getDeliveredAt()
                .isBefore(LocalDateTime.now().minusDays(returnPeriodDays))) {
            throw new CustomException(String.format("Cannot return item after %d days", returnPeriodDays), HttpStatus.BAD_REQUEST);
        }
        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new CustomException("Image files are required", HttpStatus.BAD_REQUEST);
        }

        ReturnItem returnItem = returnItemMapper.ReturnItemRequesttoReturnItem(request);
        returnItem.setStatus(ReturnItemStatus.PENDING);
        returnItem.setOrderDetail(orderDetailMapper.INSTANCE.orderDetailResponsetoOrderDetail(orderDetailResponse));

        List<String> returnItemImageUrls = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            returnItemImageUrls.add(firebaseService.upload(imageFile, firebaseStorageProperties.getUploadReturnItemImage()));
        }
        returnItem.setImages(String.join(",", returnItemImageUrls));

        return returnItemRepository.save(returnItem).getId();
    }

    @Override
    public void updateReturnItem(long id, RefundReturnItemRequest request) {
        ReturnItem existingReturnItem = findReturnItemById(id);
        returnItemMapper.updateReturnItem(existingReturnItem, request);
        var orderDetail = orderDetailService.findOrderDetailById(existingReturnItem.getOrderDetail().getId());
        existingReturnItem.setRefundAmount((orderDetail.getUnitPrice()
                .multiply(BigDecimal.valueOf(existingReturnItem.getQuantityReturned())))
                .multiply(BigDecimal.valueOf(request.getRefundPercentage()))
                .divide(BigDecimal.valueOf(100)));
        returnItemRepository.save(existingReturnItem);

        kafkaProducer.sendMessageReturnItem(ReturnItemMail.builder()
                .email(existingReturnItem.getOrderDetail().getOrder().getEmail())
                .username(existingReturnItem.getOrderDetail().getOrder().getLastName())
                .orderCode(existingReturnItem.getOrderDetail().getOrder().getCodeOrder())
                .status(existingReturnItem.getStatus())
                .statusNote(existingReturnItem.getStatusNote())
                .conditionItem(request.getConditionItem())
                .conditionNote(request.getConditionNote())
                .refundPercentage(request.getRefundPercentage())
                .refundAmount(existingReturnItem.getRefundAmount())
                .build());

        var product = productService.getProductById(orderDetail.getProductId()).getData();
        if (request.getIsAddStockQty().equals("true")) {
            inventoryServiceClient.createInventory(InventoryRequest.builder()
                            .productId(product.getProductId())
                            .quantity(orderDetail.getQuantity())
                            .inventoryStatusId(inventoryStatusServiceClient.getInventoryStatusByName("RETURN_RESALEABLE").getData().getId())
                            .note("Return item")
                            .date(LocalDatetimeConverter.convertLocalDateTimeToString(LocalDateTime.now()))
                            .build()
            );
        }else {
            inventoryServiceClient.createInventory(InventoryRequest.builder()
                            .productId(product.getProductId())
                            .quantity(orderDetail.getQuantity())
                            .inventoryStatusId(inventoryStatusServiceClient.getInventoryStatusByName("RETURN_NON_RESALEABLE").getData().getId())
                            .note("Return item non-resaleable")
                            .date(LocalDatetimeConverter.convertLocalDateTimeToString(LocalDateTime.now()))
                            .build()
            );
        }
    }

    @Override
    public void updateStatusReturnItem(Long id, ReturnItemStatusRequest returnItemStatusRequest) {
        ReturnItem returnItem = findReturnItemById(id);
        ReturnItemStatus currentStatus = returnItem.getStatus();
        ReturnItemStatus newStatus = returnItemStatusRequest.getStatus();

        if (currentStatus.equals(ReturnItemStatus.COMPLETED) || currentStatus.equals(ReturnItemStatus.REJECTED)) {
            throw new CustomException("Cannot change status from " + currentStatus + " to " + newStatus, HttpStatus.BAD_REQUEST);
        }

        ReturnItemStatus[] statusOrder = {
                ReturnItemStatus.PENDING,
                ReturnItemStatus.APPROVED,
                ReturnItemStatus.REJECTED,
                ReturnItemStatus.REFUNDED,
                ReturnItemStatus.REPLACEMENT_SHIPPED,
                ReturnItemStatus.COMPLETED
        };

        int currentIndex = Arrays.asList(statusOrder).indexOf(currentStatus);
        int newIndex = Arrays.asList(statusOrder).indexOf(newStatus);
        if (currentIndex != -1 && newIndex != -1 && currentIndex > newIndex) {
            throw new CustomException("Cannot change status from " + currentStatus + " to " + newStatus + " because it is not a valid transition.", HttpStatus.BAD_REQUEST);
        }

        boolean canChangeStatus = (currentStatus.equals(ReturnItemStatus.PENDING) &&
                (newStatus.equals(ReturnItemStatus.APPROVED) || newStatus.equals(ReturnItemStatus.REJECTED))) ||
                (currentStatus.equals(ReturnItemStatus.APPROVED) &&
                        (newStatus.equals(ReturnItemStatus.REFUNDED) || newStatus.equals(ReturnItemStatus.REPLACEMENT_SHIPPED))) ||
                (currentStatus.equals(ReturnItemStatus.REFUNDED) && !newStatus.equals(ReturnItemStatus.REPLACEMENT_SHIPPED) && !newStatus.equals(ReturnItemStatus.REFUNDED)) ||
                (currentStatus.equals(ReturnItemStatus.REPLACEMENT_SHIPPED) && !newStatus.equals(ReturnItemStatus.REFUNDED) && !newStatus.equals(ReturnItemStatus.REPLACEMENT_SHIPPED));

        if (!canChangeStatus) {
            throw new CustomException("Cannot change status from " + currentStatus + " to " + newStatus, HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra ghi chú trạng thái
        if (newStatus.equals(ReturnItemStatus.REJECTED) && returnItemStatusRequest.getStatusNote().isEmpty()) {
            throw new CustomException("Status note is required", HttpStatus.BAD_REQUEST);
        }

        returnItem.setStatus(returnItemStatusRequest.getStatus());
        returnItem.setStatusNote(returnItemStatusRequest.getStatusNote());
        returnItemRepository.save(returnItem);

        if (!returnItemStatusRequest.getStatus().equals(ReturnItemStatus.COMPLETED)) {
            kafkaProducer.sendMessageReturnItem(ReturnItemMail.builder()
                    .email(returnItem.getOrderDetail().getOrder().getEmail())
                    .username(returnItem.getOrderDetail().getOrder().getLastName())
                    .orderCode(returnItem.getOrderDetail().getOrder().getCodeOrder())
                    .status(returnItem.getStatus())
                    .statusNote(returnItem.getStatusNote())
                    .build());
        }

        notificationClient.sendNotification(NotificationRequest.builder()
                .userId(returnItem.getOrderDetail().getOrder().getUserId())
                .title("Return item status")
                .message("Your return item status has been updated to " + returnItem.getStatus())
                .topicRedis(topicNotificationUser + ":" + returnItem.getOrderDetail().getOrder().getUserId())
                .type("info")
                .build());
    }

    @Override
    public void deleteReturnItem(long id) {
        returnItemRepository.delete(findReturnItemById(id));
    }

    @Override
    public void moveToTrash(Long id) {
        ReturnItem returnItem = findReturnItemById(id);
        LocalDateTime now = LocalDateTime.now();
        returnItem.setDeletedAt(now);
        returnItemRepository.save(returnItem);
    }

    @Override
    public Page<ReturnItemResponse> getInTrash(Pageable pageable) {
        return returnItemRepository.findByDeletedAtIsNotNull(pageable).map(returnItemMapper::toReturnItemResponse);
    }

    private ReturnItem findReturnItemById(long id) {
        //có thể sử dụng Distributed Lock
        return returnItemRepository.findById(id).orElseThrow(() -> new CustomException("ReturnItem not found with id: " + id, HttpStatus.BAD_REQUEST));
    }

    @Override
    public void restoreReturnItem(Long id) {
        ReturnItem returnItem = findReturnItemById(id);
        returnItem.setDeletedAt(null);
        returnItemRepository.save(returnItem);
    }

    private List<String> getReturnItemImageResponses(Long id) {
        var returnItem = findReturnItemById(id);
        return new ArrayList<>(Arrays.asList(returnItem.getImages().split(",")));
    }

    @Override
    public Page<ReturnItemResponse> searchBySpecification(Pageable pageable, String sort, String[] returnItem, String[] orderDetail) {
        log.info("getReturnItemsBySpecifications");
        Pageable pageableSorted = sortData(sort, pageable);

        SpecificationBuilder builder = new SpecificationBuilder();
        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
        if (returnItem != null) {
            parseCriteriaBuilder(builder, returnItem, pattern, false, null);
        }
        if (orderDetail != null) {
            parseCriteriaBuilder(builder, orderDetail, pattern, true, "order_detail");
        }

        if (builder.params.isEmpty()) {
            return returnItemRepository.findAll(pageableSorted).map(returnItemMapper::toReturnItemResponse);
        }

        Page<ReturnItem> users = returnItemRepository.findAll(build(builder.params), pageableSorted);
        return users.map(returnItemMapper::toReturnItemResponse);
    }

    private Pageable sortData(String sort, Pageable pageable) {
        Sort sortByStatus = Sort.by("status");
        Sort finalSort;

        if (StringUtils.hasText(sort)) {
            Pattern patternSort = Pattern.compile(SORT_BY);
            Matcher matcher = patternSort.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                Sort sortByOtherField = matcher.group(3).equalsIgnoreCase("desc")
                        ? Sort.by(columnName).descending()
                        : Sort.by(columnName).ascending();
                finalSort = sortByStatus.and(sortByOtherField);
            } else {
                finalSort = sortByStatus;
            }
        } else {
            finalSort = sortByStatus;
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), finalSort);
    }

    private void parseCriteriaBuilder(SpecificationBuilder builder, String[] entities, Pattern pattern, boolean isJoinQuery, String joinEntity) {
        for (String e : entities) {
            Matcher matcher = pattern.matcher(e);
            if (matcher.find()) {
                if (e.startsWith(SearchOperation.OR_PREDICATE_FLAG)) {
                    builder.with(SearchOperation.OR_PREDICATE_FLAG, matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6), isJoinQuery, joinEntity);
                }else {
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6), isJoinQuery, joinEntity);
                }
            }
        }
    }

    private Specification<ReturnItem> build(List<SpecSearchCriteria> params){
        Specification<ReturnItem> specification = new ReturnItemSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            specification = params.get(i).getOrPredicate()
                    ? Specification.where(specification).or(new ReturnItemSpecification(params.get(i)))
                    : Specification.where(specification).and(new ReturnItemSpecification(params.get(i)));
        }
        return specification;
    }
}