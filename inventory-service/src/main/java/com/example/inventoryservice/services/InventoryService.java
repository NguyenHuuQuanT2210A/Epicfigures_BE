package com.example.inventoryservice.services;

import com.example.inventoryservice.dto.request.InventoryRequest;
import com.example.inventoryservice.dto.response.InventoryResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryService {
    Page<InventoryResponse> getAllInventories(Pageable pageable);
    InventoryResponse getInventoryById(long id);
    Page<InventoryResponse> getInventoryByProductId(long productId, Pageable pageable);
    void updateInventory(long id, InventoryRequest request, HttpServletRequest httpServletRequest);
    Long addInventory(InventoryRequest request, HttpServletRequest httpServletRequest);
    void deleteInventory(long id);
    void moveToTrash(Long id);
    Page<InventoryResponse> getInTrash(Pageable pageable);
    void restoreInventory(Long id);

    Page<InventoryResponse> getInventoryByStatusId(Integer inventoryStatusId, Pageable pageable);
}
