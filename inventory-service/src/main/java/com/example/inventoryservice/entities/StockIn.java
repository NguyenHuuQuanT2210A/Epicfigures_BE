package com.example.inventoryservice.entities;//package com.example.productservice.entities;
//
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
//@Table(name = "StockIn")
//public class StockIn {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "Id")
//    private long id;
//
//    @ManyToOne
//    @JoinColumn(name = "Product_id")
//    private Product product;
//
//    @ManyToOne
//    @JoinColumn(name = "Clinic_id")
//    private Clinic clinic;
//
//    @Column(name = "Quantity")
//    private long quantity;
//
//    @Column(name = "DateIn")
//    private LocalDateTime dateIn;
//
//    @Column(name = "Supplier")
//    private String supplier;
//
//    @Column(name = "ManufactureDate")
//    private LocalDateTime manufactureDate;
//
//    @Column(name = "ExpiryDate")
//    private LocalDateTime expiryDate;
//
//    @Column(name = "Status")
//    private int status;
//
//
//}
