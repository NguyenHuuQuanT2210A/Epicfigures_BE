package com.example.couponservice.services.impl;

import com.example.couponservice.dto.request.CouponRequest;
import com.example.couponservice.dto.response.CouponResponse;
import com.example.couponservice.entities.Coupon;
import com.example.couponservice.exception.NotFoundException;
import com.example.couponservice.mapper.CouponMapper;
import com.example.couponservice.repository.CouponRepository;
import com.example.couponservice.services.CouponService;
import com.example.couponservice.util.GenerateUniqueCode;
import com.example.couponservice.util.LocalDatetimeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    @Override
    public Page<CouponResponse> getAllCoupons(Pageable pageable) {
        return couponRepository.findByDeletedAtIsNull(pageable).map(couponMapper::toCouponResponse);
    }

    @Override
    public CouponResponse getCouponById(Long id) {
        return couponMapper.toCouponResponse(findCouponById(id));
    }

    @Override
    public Page<CouponResponse> getCouponByName(String name, Pageable pageable) {
        return couponRepository.findByNameLikeAndDeletedAtIsNull(name, pageable).map(couponMapper::toCouponResponse);
    }

    @Override
    public Page<CouponResponse> getCouponByCode(String code, Pageable pageable) {
        return couponRepository.findByCodeLikeAndDeletedAtIsNull(code, pageable).map(couponMapper::toCouponResponse);
    }

    @Override
    public void updateCoupon(Long id, CouponRequest request) {
        Coupon coupon = findCouponById(id);
        if (coupon.getStartDate().isAfter(LocalDateTime.now()) && !coupon.getActive()) {
            if (!isCheckDate(request.getStartDate(), request.getEndDate())) {
                throw new NotFoundException("Start date must be greater than current date and end date must be greater than start date");
            }
            couponMapper.updatedCoupon(coupon, request);
            coupon.setStartDate(LocalDatetimeConverter.toLocalDateTime(request.getStartDate()));
            coupon.setEndDate(LocalDatetimeConverter.toLocalDateTime(request.getEndDate()));
            couponRepository.save(coupon);
        } else {
            throw new NotFoundException("Coupon has been expired or active");
        }

    }

    private boolean isCheckDate(String startDate, String endDate) {
        LocalDateTime startDateTime = LocalDatetimeConverter.toLocalDateTime(startDate);
        LocalDateTime endDateTime = LocalDatetimeConverter.toLocalDateTime(endDate);
        LocalDateTime now = LocalDateTime.now();

        return startDateTime.isBefore(endDateTime)
                && startDateTime.isAfter(now);
    }

    @Override
    public Long addCoupon(CouponRequest request) {
        if (!isCheckDate(request.getStartDate(), request.getEndDate())) {
            throw new NotFoundException("Start date must be greater than current date and end date must be greater than start date");
        }
        var coupon = couponMapper.toCoupon(request);
        while (couponRepository.existsByCode(coupon.getCode())) {
            coupon.setCode(GenerateUniqueCode.generateCouponCode());
        }
        coupon.setStartDate(LocalDatetimeConverter.toLocalDateTime(request.getStartDate()));
        coupon.setEndDate(LocalDatetimeConverter.toLocalDateTime(request.getEndDate()));
        couponRepository.save(coupon);
        return coupon.getId();
    }

    @Override
    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    private Coupon findCouponById(Long id) {
        return couponRepository.findById(id).orElseThrow(() -> new NotFoundException("Coupon not found"));
    }

    @Override
    public void moveToTrash(Long id) {
        Coupon coupon = findCouponById(id);
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(coupon.getStartDate()) && now.isBefore(coupon.getEndDate())) {
            throw new IllegalStateException("Coupon is currently active and cannot be moved to trash.");
        }

        coupon.setDeletedAt(now);
        couponRepository.save(coupon);
    }

    @Override
    public Page<CouponResponse> getInTrash(Pageable pageable) {
        return couponRepository.findByDeletedAtIsNotNull(pageable).map(couponMapper::toCouponResponse);
    }

    @Override
    public void restoreCoupon(Long id) {
        Coupon coupon = findCouponById(id);
        coupon.setDeletedAt(null);
        couponRepository.save(coupon);
    }
}
