package com.example.inventoryservice.services;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> getAllInventories();
    InventoryResponse getInventoryById(long id);
    List<InventoryResponse> getInventoryByProductId(long productId);
    InventoryResponse updateInventory(long id, InventoryRequest inventoryRequest);
    InventoryResponse addInventory(InventoryRequest inventoryRequest);
    void deleteInventory(long id);
}
