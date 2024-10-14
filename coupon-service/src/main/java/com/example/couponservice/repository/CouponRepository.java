package com.example.couponservice.repository;

import com.example.couponservice.entities.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Page<Coupon> findByDeletedAtIsNull(Pageable pageable);
    Page<Coupon> findByDeletedAtIsNotNull(Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.name LIKE %?1% AND c.deletedAt IS NULL")
    Page<Coupon> findByNameLikeAndDeletedAtIsNull(String name, Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.code LIKE %?1% AND c.deletedAt IS NULL")
    Page<Coupon> findByCodeLikeAndDeletedAtIsNull(String code, Pageable pageable);
    boolean existsByCode(String code);
}
