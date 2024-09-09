package com.example.inventoryservice.entities;

import com.example.inventoryservice.entities.base.BaseEntity;
import com.example.inventoryservice.enums.InventoryStatus;
import com.example.inventoryservice.enums.InventoryType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory")
public class Inventory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private InventoryStatus status;
    @Enumerated(EnumType.STRING)
    private InventoryType type;

    private LocalDateTime date;
}
