package com.example.inventoryservice.mapper;

import com.example.inventoryservice.dto.request.InventoryRequest;
import com.example.inventoryservice.dto.response.InventoryResponse;
import com.example.inventoryservice.entities.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
//    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);
    InventoryResponse toInventoryResponse(Inventory inventory);

    @Mapping(target = "date", ignore = true)
    Inventory toInventory(InventoryRequest request);
    @Mapping(target = "date", ignore = true)
    void updatedInventory(@MappingTarget Inventory inventory, InventoryRequest request);
}
