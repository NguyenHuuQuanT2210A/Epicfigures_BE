package com.example.productservice.entities;

import com.example.productservice.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_quantity")
public class ProductQuantity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long stockQuantity;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long reservedQuantity;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long soldQuantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    private Product product;
}
