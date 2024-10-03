package com.example.inventoryservice.dto.request;

import com.example.inventoryservice.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStatusRequest {
    private String name;
    private String description;
    private String isAddAction;
}
