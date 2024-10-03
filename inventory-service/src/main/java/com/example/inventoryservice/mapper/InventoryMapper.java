package com.example.inventoryservice.mapper;

import com.example.inventoryservice.dto.request.InventoryRequest;
import com.example.inventoryservice.dto.response.InventoryResponse;
import com.example.inventoryservice.entities.Inventory;
import com.example.inventoryservice.helper.LocalDatetimeConverter;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = LocalDatetimeConverter.class)
public interface InventoryMapper {
//    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    @Mapping(target = "status", source = "inventoryStatus.name")
    InventoryResponse toInventoryResponse(Inventory inventory);

    @Mapping(target = "date", ignore = true)
    Inventory toInventory(InventoryRequest request);
    @Mapping(target = "date", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatedInventory(@MappingTarget Inventory inventory, InventoryRequest request);
}
