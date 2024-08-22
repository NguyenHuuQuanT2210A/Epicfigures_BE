package com.example.inventoryservice.services;

import com.example.inventoryservice.dto.ApiResponse;
import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.dto.ProductDTO;
import com.example.inventoryservice.entities.Inventory;
import com.example.inventoryservice.exception.NotFoundException;
import com.example.inventoryservice.helper.LocalDatetimeConverter;
import com.example.inventoryservice.mapper.InventoryMapper;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final ProductClients productClients;

    @Override
    public List<InventoryResponse> getAllInventories() {
        return inventoryRepository.findAll().stream().map(inventoryMapper::toInventoryResponse).toList();
    }

    @Override
    public InventoryResponse getInventoryById(long id) {
        return inventoryMapper.toInventoryResponse(findInventoryById(id));
    }

    @Override
    public List<InventoryResponse> getInventoryByProductId(long productId) {
        ApiResponse<ProductDTO> product = productClients.getProductById(productId);
        return inventoryRepository.findInventoryByProductId(product.getData().getProductId()).stream().map(inventoryMapper::toInventoryResponse).toList();
    }

    @Override
    public InventoryResponse updateInventory(long id, InventoryRequest inventoryRequest) {
        Inventory inventory = findInventoryById(id);
        inventoryMapper.updatedInventory(inventory, inventoryRequest);
        inventory.setDate(LocalDatetimeConverter.toLocalDateTime(inventoryRequest.getDate(), true));
        return inventoryMapper.toInventoryResponse(inventoryRepository.save(inventory));
    }

    @Override
    public InventoryResponse addInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = inventoryMapper.toInventory(inventoryRequest);
        inventory.setDate(LocalDatetimeConverter.toLocalDateTime(inventoryRequest.getDate(), true));
        return inventoryMapper.toInventoryResponse(inventoryRepository.save(inventory));
    }

    @Override
    public void deleteInventory(long id) {
        inventoryRepository.deleteById(id);
    }

    private Inventory findInventoryById(long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Inventory not found"));
    }
}
