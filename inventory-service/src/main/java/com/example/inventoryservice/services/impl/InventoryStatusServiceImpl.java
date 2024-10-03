package com.example.inventoryservice.services.impl;

import com.example.inventoryservice.dto.request.InventoryStatusRequest;
import com.example.inventoryservice.dto.response.InventoryStatusResponse;
import com.example.inventoryservice.entities.InventoryStatus;
import com.example.inventoryservice.exception.NotFoundException;
import com.example.inventoryservice.mapper.InventoryStatusMapper;
import com.example.inventoryservice.repository.InventoryStatusRepository;
import com.example.inventoryservice.services.InventoryStatusService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryStatusServiceImpl implements InventoryStatusService {
    private final InventoryStatusRepository inventoryStatusRepository;
    private final InventoryStatusMapper inventoryStatusMapper;

    @Override
    public Page<InventoryStatusResponse> getAllInventoryStatuses(Pageable pageable) {
        return inventoryStatusRepository.findByDeletedAtIsNull(pageable).map(inventoryStatusMapper::toInventoryStatusResponse);
    }

    @Override
    public InventoryStatusResponse getInventoryStatusById(Integer id) {
        return inventoryStatusMapper.toInventoryStatusResponse(findInventoryStatusById(id));
    }

    @Override
    public void updateInventoryStatus(Integer id, InventoryStatusRequest request) {
        InventoryStatus inventoryStatus = findInventoryStatusById(id);
        inventoryStatusMapper.updatedInventoryStatus(inventoryStatus, request);
        inventoryStatus.setAddAction(Boolean.parseBoolean(request.getIsAddAction()));
        inventoryStatusRepository.save(inventoryStatus);
    }

    @Override
    public Integer addInventoryStatus(InventoryStatusRequest request) {
        var inventoryStatus = inventoryStatusMapper.toInventory(request);
        inventoryStatus.setAddAction(Boolean.parseBoolean(request.getIsAddAction()));
        inventoryStatusRepository.save(inventoryStatus);
        return inventoryStatus.getId();
    }

    @Override
    public void deleteInventoryStatus(Integer id) {
        inventoryStatusRepository.deleteById(id);
    }

    private InventoryStatus findInventoryStatusById(Integer id) {
        return inventoryStatusRepository.findById(id).orElseThrow(() -> new NotFoundException("InventoryStatus not found"));
    }

    @Override
    public void moveToTrash(Integer id) {
        InventoryStatus inventoryStatus = findInventoryStatusById(id);

        LocalDateTime now = LocalDateTime.now();
        inventoryStatus.setDeletedAt(now);
        inventoryStatusRepository.save(inventoryStatus);
    }

    @Override
    public Page<InventoryStatusResponse> getInTrash(Pageable pageable) {
        return inventoryStatusRepository.findByDeletedAtIsNotNull(pageable).map(inventoryStatusMapper::toInventoryStatusResponse);
    }

    @Override
    public void restoreInventoryStatus(Integer id) {
        InventoryStatus inventoryStatus = findInventoryStatusById(id);
        inventoryStatus.setDeletedAt(null);
        inventoryStatusRepository.save(inventoryStatus);
    }
}
