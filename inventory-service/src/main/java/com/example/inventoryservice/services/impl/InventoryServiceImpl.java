package com.example.inventoryservice.services.impl;

import com.example.inventoryservice.dto.request.ProductQuantityRequest;
import com.example.inventoryservice.dto.response.ApiResponse;
import com.example.inventoryservice.dto.request.InventoryRequest;
import com.example.inventoryservice.dto.response.InventoryResponse;
import com.example.inventoryservice.dto.response.ProductResponse;
import com.example.inventoryservice.entities.Inventory;
import com.example.inventoryservice.exception.NotFoundException;
import com.example.inventoryservice.helper.LocalDatetimeConverter;
import com.example.inventoryservice.mapper.InventoryMapper;
import com.example.inventoryservice.repository.InventoryRepository;
import com.example.inventoryservice.repository.InventoryStatusRepository;
import com.example.inventoryservice.security.JwtTokenUtil;
import com.example.inventoryservice.services.InventoryService;
import com.example.inventoryservice.services.ProductClients;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final ProductClients productClients;
    private final InventoryStatusRepository inventoryStatusRepository;
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    public Page<InventoryResponse> getAllInventories(Pageable pageable) {
        Page<Inventory> inventories = inventoryRepository.findByDeletedAtIsNull(pageable);
        return inventories.map(inventory -> {
            InventoryResponse inventoryResponse = inventoryMapper.toInventoryResponse(inventory);
            inventoryResponse.setProductResponse(getProductById(inventory.getProductId()).getData());
            return inventoryResponse;
        });
    }

    @Override
    public InventoryResponse getInventoryById(long id) {
        Inventory inventory = findInventoryById(id);
        InventoryResponse inventoryResponse = inventoryMapper.toInventoryResponse(inventory);
        inventoryResponse.setProductResponse(getProductById(inventory.getProductId()).getData());
        return inventoryResponse;
    }

    @Override
    public Page<InventoryResponse> getInventoryByProductId(long productId, Pageable pageable) {
        ApiResponse<ProductResponse> product = productClients.getProductById(productId);
        return inventoryRepository.findInventoryByProductIdAndDeletedAtIsNull(product.getData().getProductId(), pageable)
                .map(inventory -> {
                    InventoryResponse inventoryResponse = inventoryMapper.toInventoryResponse(inventory);
                    inventoryResponse.setProductResponse(getProductById(inventory.getProductId()).getData());
                    return inventoryResponse;
                });
    }

    @Override
    public Page<InventoryResponse> getInventoryByStatusId(Integer inventoryStatusId, Pageable pageable) {
        inventoryStatusRepository.findById(inventoryStatusId).orElseThrow(() -> new NotFoundException("Inventory Status not found"));
        return inventoryRepository.findInventoryByInventoryStatusIdAndDeletedAtIsNull(inventoryStatusId, pageable)
                .map(inventory -> {
                    InventoryResponse inventoryResponse = inventoryMapper.toInventoryResponse(inventory);
                    inventoryResponse.setProductResponse(getProductById(inventory.getProductId()).getData());
                    return inventoryResponse;
                });
    }

    @Override
    public void updateInventory(long id, InventoryRequest request, HttpServletRequest httpServletRequest) {
        ApiResponse<ProductResponse> product = getProductById(request.getProductId());
        var inventoryStatus = inventoryStatusRepository.findById(request.getInventoryStatusId()).orElseThrow(() -> new NotFoundException("Inventory Status not found"));
        Inventory inventory = findInventoryById(id);
        inventoryMapper.updatedInventory(inventory, request);
        inventory.setDate(LocalDatetimeConverter.toLocalDateTime(request.getDate()));
        inventory.setUpdatedBy(getUserNameFromToken(httpServletRequest));
        inventory.setInventoryStatus(inventoryStatus);

        int stockQuantity;
        if (inventoryStatus.isAddAction()) {
            stockQuantity = product.getData().getStockQuantity() + request.getQuantity();
        } else {
            stockQuantity = product.getData().getStockQuantity() - request.getQuantity();
        }

        productClients.updateQuantity(product.getData().getProductId(), ProductQuantityRequest.builder().stockQuantity(stockQuantity).build());

        inventoryRepository.save(inventory);
    }

    @Override
    public Long addInventory(InventoryRequest request, HttpServletRequest httpServletRequest) {
        var product = getProductById(request.getProductId());
        var inventoryStatus = inventoryStatusRepository.findById(request.getInventoryStatusId()).orElseThrow(() -> new NotFoundException("Inventory Status not found"));

        Inventory inventory = inventoryMapper.toInventory(request);
//        inventory.setTotalCost(request.getUnitPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        inventory.setDate(LocalDatetimeConverter.toLocalDateTime(request.getDate()));
        inventory.setCreatedBy(getUserNameFromToken(httpServletRequest));
        inventory.setInventoryStatus(inventoryStatus);
        inventoryRepository.save(inventory);

        int stockQuantity;
        if (inventoryStatus.isAddAction()) {
            stockQuantity = product.getData().getStockQuantity() + request.getQuantity();
        } else {
            stockQuantity = product.getData().getStockQuantity() - request.getQuantity();
        }

        if (inventoryStatus.getName().equals("OUT")){
            int reservedQuantity = product.getData().getReservedQuantity() - request.getQuantity();
            int soldQuantity = product.getData().getSoldQuantity() + request.getQuantity();
            productClients.updateQuantity(product.getData().getProductId(), ProductQuantityRequest.builder()
                    .stockQuantity(stockQuantity)
                    .reservedQuantity(reservedQuantity)
                    .soldQuantity(soldQuantity)
                    .build());
        } else {
            productClients.updateQuantity(product.getData().getProductId(), ProductQuantityRequest.builder().stockQuantity(stockQuantity).build());
        }
//        else if (inventoryStatus.getName().equals("IN") && inventoryStatus.isSystemType()) {
//            productClients.updateQuantity(product.getData().getProductId(),
//                    ProductQuantityRequest.builder()
//                            .stockQuantity(stockQuantity)
//                            .purchasePrice(inventory.getUnitPrice())
//                            .build());
//        }

        return inventory.getId();
    }

    @Override
    public void deleteInventory(long id) {
        inventoryRepository.deleteById(id);
    }

    private Inventory findInventoryById(long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Inventory not found"));
    }

    @Override
    public void moveToTrash(Long id) {
        Inventory inventory = findInventoryById(id);

        LocalDateTime now = LocalDateTime.now();
        inventory.setDeletedAt(now);
        inventoryRepository.save(inventory);
    }

    @Override
    public Page<InventoryResponse> getInTrash(Pageable pageable) {
        Page<Inventory> inventories = inventoryRepository.findByDeletedAtIsNotNull(pageable);
        return inventories.map(inventory -> {
            InventoryResponse inventoryResponse = inventoryMapper.toInventoryResponse(inventory);
            inventoryResponse.setProductResponse(getProductById(inventory.getProductId()).getData());
            return inventoryResponse;
        });
    }

    @Override
    public void restoreInventory(Long id) {
        Inventory inventory = findInventoryById(id);
        inventory.setDeletedAt(null);
        inventoryRepository.save(inventory);
    }


    private String getUserNameFromToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        return jwtTokenUtil.getUserNameFromJwtToken(token);
    }

    private ApiResponse<ProductResponse> getProductById(Long productId) {
        return productClients.getProductById(productId);
    }
}
