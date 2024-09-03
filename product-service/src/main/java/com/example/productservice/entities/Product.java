package com.example.productservice.entities;

import com.example.productservice.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ProductImage> images;

    @Column(nullable = false)
    private Integer stockQuantity;

    private String manufacturer;

    private String size;

    private String weight;

    public Product(long l, String s, int i) {
        this.productId = l;
        this.name = s;
        this.price = BigDecimal.valueOf(i);
    }

//    @OneToMany(mappedBy = "product")
//    private Set<FavoriteProducts> favoriteProducts;
//
//    @OneToMany(mappedBy = "product")
//    private List<StockOut> stockOuts;
//
//    @OneToMany(mappedBy = "product")
//    private List<StockIn> stockIns;
//
//    @OneToMany(mappedBy = "product")
//    private List<InStock> inStocks;
//
//    @OneToMany(mappedBy = "product")
//    private List<Feedback> feedbacks;
//
//    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
//    private ProductDetail productDetail;
//
//    @OneToMany(fetch = FetchType.LAZY)
//    private List<ShoppingCart> shoppingCarts;
//
//    @OneToOne
//    private CartItemResponse cartItem;

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", manufacturer='" + manufacturer + '\'' +
                ", size='" + size + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }
}