//package com.example.productservice.repositories;
//
//
//import com.example.productservice.entities.Clinic;
//import com.example.productservice.entities.Feedback;
//import com.example.productservice.entities.Product;
//import com.example.productservice.entities.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface FeedbackRepository extends JpaRepository<Feedback, Long>, JpaSpecificationExecutor<Feedback> {
//
//    List<Feedback> findByProduct(Product product);
//    List<Feedback> findByClinic(Clinic clinic);
//    List<Feedback> findByRating(int rating);
//    List<Feedback> findByUser(User user);
//
//
//}
