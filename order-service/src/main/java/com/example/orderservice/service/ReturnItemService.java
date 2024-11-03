package com.example.orderservice.service;


import com.example.orderservice.dto.request.RefundReturnItemRequest;
import com.example.orderservice.dto.request.ReturnItemRequest;
import com.example.orderservice.dto.request.ReturnItemStatusRequest;
import com.example.orderservice.dto.response.ReturnItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public interface ReturnItemService {
    Page<ReturnItemResponse> getAllReturnItems(Pageable pageable);
    ReturnItemResponse getReturnItemById(Long id);
    Page<ReturnItemResponse> getReturnItemByUserId(Long userId, Pageable pageable);
    Long addReturnItem(ReturnItemRequest request, List<MultipartFile> imageFiles) throws IOException;
    void updateReturnItem(long id, RefundReturnItemRequest request);
    void updateStatusReturnItem(Long id, ReturnItemStatusRequest returnItemStatusRequest);
    void deleteReturnItem(long id);
    void moveToTrash(Long id);
    Page<ReturnItemResponse> getInTrash(Pageable pageable);
    void restoreReturnItem(Long id);
    Page<ReturnItemResponse> searchBySpecification(Pageable pageable, String sort, String[] returnItem, String[] category);

}
