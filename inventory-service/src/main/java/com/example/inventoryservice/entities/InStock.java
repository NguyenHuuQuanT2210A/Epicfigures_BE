package com.example.inventoryservice.entities;//package com.example.productservice.entities;
//
//import com.example.productservice.statics.enums.InStockStatus;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "InStock")
//public class InStock {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long stockId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ProductId",referencedColumnName = "id")
//    private Product product;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ClinicID",referencedColumnName = "id")
//    private Clinic clinic;
//
//    @Column(name = "StockQuantity")
//    private long stockQuantity;
//
//    @Column(name = "LastUpdated")
//    private LocalDateTime lastUpdated;
//
//    @Column(name = "StockStatus")
//    private InStockStatus stockStatus;
//}
