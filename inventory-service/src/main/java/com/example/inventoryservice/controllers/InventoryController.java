package com.example.inventoryservice.controllers;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.services.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping
    ResponseEntity<InventoryResponse> createInventory(@RequestBody @Valid InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.addInventory(request));
    }

    @GetMapping
    ResponseEntity<List<InventoryResponse>> getInventories() {
        return ResponseEntity.ok(inventoryService.getAllInventories());
    }

    @GetMapping("/{id}")
    ResponseEntity<InventoryResponse> getInventoryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @GetMapping("/product/{productId}")
    ResponseEntity<List<InventoryResponse>> getInventoryByProductId(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok().body("Deleted");
    }

    @PutMapping("/{id}")
    ResponseEntity<InventoryResponse> updateInventory(@PathVariable Long id, @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, request));
    }
}
