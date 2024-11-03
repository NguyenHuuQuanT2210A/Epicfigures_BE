package com.example.orderservice.repositories;

import com.example.orderservice.entities.Order;
import com.example.orderservice.entities.ReturnItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ReturnItemRepository extends JpaRepository<ReturnItem, Long>, JpaSpecificationExecutor<ReturnItem> {
    Page<ReturnItem> findAll(Pageable pageable);
    Page<ReturnItem> findByDeletedAtIsNotNull(Pageable pageable);
    Page<ReturnItem> findByDeletedAtIsNull(Pageable pageable);
    List<ReturnItem> findByIdInAndDeletedAtIsNull(Set<Long> returnItemIds);

    @Query("SELECT r FROM ReturnItem r WHERE r.orderDetail.order.userId = ?1 AND r.deletedAt IS NULL")
    Page<ReturnItem> findByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);
}
