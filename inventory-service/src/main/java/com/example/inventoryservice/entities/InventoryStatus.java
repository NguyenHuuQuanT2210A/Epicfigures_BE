package com.example.inventoryservice.entities;

import com.example.inventoryservice.entities.base.BaseEntity;
import com.example.inventoryservice.enums.InventoryActionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory_status")
public class InventoryStatus extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    private String description;
    private InventoryActionType inventoryActionType;
    private boolean isSystemType;
}
