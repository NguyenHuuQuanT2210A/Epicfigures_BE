package com.example.inventoryservice.services;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryService {
    Page<InventoryResponse> getAllInventories(Pageable pageable);
    InventoryResponse getInventoryById(long id);
    List<InventoryResponse> getInventoryByProductId(long productId);
    InventoryResponse updateInventory(long id, InventoryRequest inventoryRequest);
    InventoryResponse addInventory(InventoryRequest inventoryRequest);
    void deleteInventory(long id);
    void moveToTrash(Long id);
    Page<InventoryResponse> getInTrash(Pageable pageable);
    void restoreInventory(Long id);
}
