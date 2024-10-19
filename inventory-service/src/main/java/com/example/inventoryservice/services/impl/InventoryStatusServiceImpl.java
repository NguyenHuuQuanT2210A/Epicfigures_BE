package com.example.inventoryservice.services.impl;

import com.example.inventoryservice.dto.request.InventoryStatusRequest;
import com.example.inventoryservice.dto.response.InventoryStatusResponse;
import com.example.inventoryservice.entities.InventoryStatus;
import com.example.inventoryservice.exception.CustomException;
import com.example.inventoryservice.exception.NotFoundException;
import com.example.inventoryservice.mapper.InventoryStatusMapper;
import com.example.inventoryservice.repository.InventoryStatusRepository;
import com.example.inventoryservice.services.InventoryStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    public Page<InventoryStatusResponse> getInventoryStatusByNames(String name, Pageable pageable) {
        return inventoryStatusRepository.findByNameLikeAndDeletedAtIsNull(name, pageable).map(inventoryStatusMapper::toInventoryStatusResponse);
    }

    @Override
    public void updateInventoryStatus(Integer id, InventoryStatusRequest request) {
        InventoryStatus inventoryStatus = findInventoryStatusById(id);
        if (inventoryStatus.isSystemType()) {
            throw new CustomException("Cannot update system type inventory", HttpStatus.BAD_REQUEST);
        }
        inventoryStatusMapper.updatedInventoryStatus(inventoryStatus, request);
        inventoryStatus.setAddAction(Boolean.parseBoolean(request.getIsAddAction()));
        inventoryStatusRepository.save(inventoryStatus);
    }

    @Override
    public Integer addInventoryStatus(InventoryStatusRequest request) {
        var inventoryStatus = inventoryStatusMapper.toInventory(request);
        inventoryStatus.setAddAction(Boolean.parseBoolean(request.getIsAddAction()));
        inventoryStatus.setSystemType(false);
        inventoryStatusRepository.save(inventoryStatus);
        return inventoryStatus.getId();
    }

    @Override
    public void deleteInventoryStatus(Integer id) {
        InventoryStatus inventoryStatus = findInventoryStatusById(id);
        if (inventoryStatus.isSystemType()) {
            throw new CustomException("Cannot delete system type inventory", HttpStatus.BAD_REQUEST);
        }
        inventoryStatusRepository.deleteById(id);
    }

    private InventoryStatus findInventoryStatusById(Integer id) {
        return inventoryStatusRepository.findById(id).orElseThrow(() -> new NotFoundException("InventoryStatus not found"));
    }

    @Override
    public void moveToTrash(Integer id) {
        InventoryStatus inventoryStatus = findInventoryStatusById(id);
        if (inventoryStatus.isSystemType()) {
            throw new CustomException("Cannot trash system type inventory", HttpStatus.BAD_REQUEST);
        }
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

    @Override
    public InventoryStatusResponse getInventoryStatusByName(String name) {
        return inventoryStatusMapper.toInventoryStatusResponse(inventoryStatusRepository.findByName(name));
    }
}
