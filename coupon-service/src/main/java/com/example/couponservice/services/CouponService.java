package com.example.couponservice.services;

import com.example.couponservice.dto.request.CouponRequest;
import com.example.couponservice.dto.response.CouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponService {
    Page<CouponResponse> getAllCoupons(Pageable pageable);
    CouponResponse getCouponById(Long id);

    Page<CouponResponse> getCouponByName(String name, Pageable pageable);
    Page<CouponResponse> getCouponByCode(String code, Pageable pageable);
    void updateCoupon(Long id, CouponRequest request);
    Long addCoupon(CouponRequest request);
    void deleteCoupon(Long id);
    void moveToTrash(Long id);
    Page<CouponResponse> getInTrash(Pageable pageable);
    void restoreCoupon(Long id);
}
