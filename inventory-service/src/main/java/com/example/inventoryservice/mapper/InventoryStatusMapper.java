package com.example.inventoryservice.mapper;

import com.example.inventoryservice.dto.request.InventoryStatusRequest;
import com.example.inventoryservice.dto.response.InventoryStatusResponse;
import com.example.inventoryservice.entities.InventoryStatus;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InventoryStatusMapper {
    InventoryStatusResponse toInventoryStatusResponse(InventoryStatus inventory);
    InventoryStatus toInventory(InventoryStatusRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatedInventoryStatus(@MappingTarget InventoryStatus inventory, InventoryStatusRequest request);
}
