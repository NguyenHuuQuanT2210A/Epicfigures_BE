//package com.example.productservice.entities;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//public class Feedback {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "Rating")
//    private int rating;
//
//    @Column(name = "Comment")
//    private String comment;
//
//    @Column(name = "CreateAt")
//    private LocalDateTime createAt;
//
//    @ManyToOne
//    @JoinColumn(name = "Product_ID")
//    private Product product;
//
//    @ManyToOne
//    @JoinColumn(name = "Clinic_ID")
//    private Clinic clinic;
//
//    @ManyToOne
//    @JoinColumn(name = "Customer_ID")
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "Parent_id")
//    private Feedback parent;
//
//    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Feedback> children;
//
//}
