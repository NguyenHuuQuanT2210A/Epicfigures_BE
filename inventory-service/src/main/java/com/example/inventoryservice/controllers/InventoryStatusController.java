package com.example.inventoryservice.controllers;


import com.example.inventoryservice.dto.request.InventoryStatusRequest;
import com.example.inventoryservice.dto.response.ApiResponse;
import com.example.inventoryservice.dto.response.InventoryStatusResponse;
import com.example.inventoryservice.entities.InventoryStatus;
import com.example.inventoryservice.services.InventoryStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory_status")
public class InventoryStatusController {
    private final InventoryStatusService inventoryStatusService;

    @PostMapping
    ApiResponse<Integer> createInventoryStatus(@RequestBody @Valid InventoryStatusRequest request) {
        return ApiResponse.<Integer>builder()
                .message("Create InventoryStatus")
                .data(inventoryStatusService.addInventoryStatus(request))
                .build();
    }

    @GetMapping
    ApiResponse<Page<InventoryStatusResponse>> getInventoryStatuses(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.<Page<InventoryStatusResponse>>builder()
                .message("Get All Inventories")
                .data(inventoryStatusService.getAllInventoryStatuses(PageRequest.of(page -1, limit, Sort.Direction.DESC, "createdAt")))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<InventoryStatusResponse> getInventoryStatusById(@PathVariable("id") Integer id) {
        return ApiResponse.<InventoryStatusResponse>builder()
                .message("Get InventoryStatus By Id")
                .data(inventoryStatusService.getInventoryStatusById(id))
                .build();
    }

    @GetMapping("/name")
    ApiResponse<Page<InventoryStatusResponse>> getInventoryStatusByName(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit,
            @RequestParam String name) {
        return ApiResponse.<Page<InventoryStatusResponse>>builder()
                .message("Get InventoryStatus By Id")
                .data(inventoryStatusService.getInventoryStatusByName(name, PageRequest.of(page -1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteInventoryStatus(@PathVariable Integer id) {
        inventoryStatusService.deleteInventoryStatus(id);
        return ApiResponse.<String>builder()
                .message("Deleted InventoryStatus Successfully!")
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<?> updateInventoryStatus(@PathVariable Integer id, @RequestBody InventoryStatusRequest request) {
        inventoryStatusService.updateInventoryStatus(id, request);
        return ApiResponse.builder()
                .message("Update InventoryStatus Successfully!")
                .build();
    }

    @PutMapping("/restore/{id}")
    ApiResponse<?> restoreInventoryStatus(@PathVariable Integer id) {
        inventoryStatusService.restoreInventoryStatus(id);
        return ApiResponse.builder()
                .message("Restore inventoryStatus successfully")
                .build();
    }

    @DeleteMapping("/in-trash/{id}")
    ApiResponse<?> moveToTrash(@PathVariable Integer id) {
        inventoryStatusService.moveToTrash(id);
        return ApiResponse.builder()
                .message("Move to trash inventoryStatus successfully")
                .build();
    }

    @GetMapping("/trash")
    ApiResponse<?> getInTrashInventoryStatus(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "limit", defaultValue = "10") int limit){
        return ApiResponse.builder()
                .message("Get in trash inventoryStatus")
                .data(inventoryStatusService.getInTrash(PageRequest.of(page -1, limit)))
                .build();
    }
}
