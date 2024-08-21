package com.example.inventoryservice.entities;//package com.example.productservice.entities;
//
//import com.example.productservice.statics.enums.Reason;
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
//@Table(name = "StockOut")
//public class StockOut {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
//    @Column(name = "DateOut")
//    private LocalDateTime dateOut;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "Reason")
//    private Reason reason;
//
//}
