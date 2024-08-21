package com.example.orderservice.repositories;

import com.example.orderservice.entities.Feedback;
import com.example.orderservice.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Feedback findByOrderDetail(OrderDetail orderDetail);

    @Query("SELECT f FROM Feedback f WHERE f.orderDetail.order.userId = :userId")
    List<Feedback> findByUserId(Long userId);

    @Query("SELECT f FROM Feedback f WHERE f.orderDetail.id.productId = :productId")
    List<Feedback> findByProductId(Long productId);


//    using store procedure

    //    @Query(value = "CALL get_feedback_by_user_id(:user_id)", nativeQuery = true)
//    @Procedure(procedureName = "get_feedback_by_user_id")
//    List<Feedback> findByUserId(@Param("user_id") Long userId);

//    @Procedure(procedureName = "get_feedback_by_product_id")
//    @Query(value = "CALL get_feedback_by_product_id(:product_id)", nativeQuery = true)
//    List<Feedback> findByProductId(@Param("product_id") Long productId);

}
