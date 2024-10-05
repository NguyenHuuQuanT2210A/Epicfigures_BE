package com.example.inventoryservice.services;

import com.example.inventoryservice.dto.request.InventoryStatusRequest;
import com.example.inventoryservice.dto.response.InventoryStatusResponse;
import com.example.inventoryservice.entities.InventoryStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryStatusService {
    Page<InventoryStatusResponse> getAllInventoryStatuses(Pageable pageable);
    InventoryStatusResponse getInventoryStatusById(Integer id);

    Page<InventoryStatusResponse> getInventoryStatusByName(String name, Pageable pageable);
    void updateInventoryStatus(Integer id, InventoryStatusRequest request);
    Integer addInventoryStatus(InventoryStatusRequest request);
    void deleteInventoryStatus(Integer id);
    void moveToTrash(Integer id);
    Page<InventoryStatusResponse> getInTrash(Pageable pageable);
    void restoreInventoryStatus(Integer id);
}
