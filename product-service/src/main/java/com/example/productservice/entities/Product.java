package com.example.productservice.entities;

import com.example.productservice.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false ,unique = true)
    private String codeProduct;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

//    @Column(nullable = false)
//    private BigDecimal purchasePrice;
//
//    @Column(nullable = false)
//    private BigDecimal listPrice;
//
//    @Column(nullable = false)
//    private BigDecimal sellingPrice;

    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ProductImage> images;

    private String manufacturer;

    private String size;

//    private String material;
//
//    private Integer height;
//
//    private Integer width;
//
//    private String figure;

    private String weight;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer stockQuantity;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer reservedQuantity;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer soldQuantity;

    private Integer returnPeriodDays;
}