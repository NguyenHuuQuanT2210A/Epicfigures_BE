package com.example.inventoryservice.controllers;

import com.example.inventoryservice.dto.ApiResponse;
import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.services.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping
    ApiResponse<InventoryResponse> createInventory(@RequestBody @Valid InventoryRequest request) {
        return ApiResponse.<InventoryResponse>builder()
                .message("Create Inventory")
                .data(inventoryService.addInventory(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<InventoryResponse>> getInventories() {
        return ApiResponse.<List<InventoryResponse>>builder()
                .message("Get All Inventories")
                .data(inventoryService.getAllInventories())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<InventoryResponse> getInventoryById(@PathVariable("id") Long id) {
        return ApiResponse.<InventoryResponse>builder()
                .message("Get Inventory By Id")
                .data(inventoryService.getInventoryById(id))
                .build();
    }

    @GetMapping("/product/{productId}")
    ApiResponse<List<InventoryResponse>> getInventoryByProductId(@PathVariable("productId") Long productId) {
        return ApiResponse.<List<InventoryResponse>>builder()
                .message("Get Inventory By Product Id")
                .data(inventoryService.getInventoryByProductId(productId))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ApiResponse.<String>builder()
                .message("Deleted Inventory Successfully!")
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<InventoryResponse> updateInventory(@PathVariable Long id, @RequestBody InventoryRequest request) {
        return ApiResponse.<InventoryResponse>builder()
                .message("Update Inventory")
                .data(inventoryService.updateInventory(id, request))
                .build();
    }
}
