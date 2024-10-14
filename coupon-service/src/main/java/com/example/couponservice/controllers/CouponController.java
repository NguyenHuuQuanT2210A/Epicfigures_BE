package com.example.couponservice.controllers;

import com.example.couponservice.dto.request.CouponRequest;
import com.example.couponservice.dto.response.ApiResponse;
import com.example.couponservice.dto.response.CouponResponse;
import com.example.couponservice.services.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    ApiResponse<Long> createCoupon(@RequestBody @Valid CouponRequest request) {
        return ApiResponse.<Long>builder()
                .message("Create Coupon")
                .data(couponService.addCoupon(request))
                .build();
    }

    @GetMapping
    ApiResponse<Page<CouponResponse>> getCoupons(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.<Page<CouponResponse>>builder()
                .message("Get All Inventories")
                .data(couponService.getAllCoupons(PageRequest.of(page -1, limit, Sort.Direction.DESC, "createdAt")))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<CouponResponse> getCouponById(@PathVariable("id") Long id) {
        return ApiResponse.<CouponResponse>builder()
                .message("Get Coupon By Id")
                .data(couponService.getCouponById(id))
                .build();
    }

    @GetMapping("/name")
    ApiResponse<Page<CouponResponse>> getCouponByName(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit,
            @RequestParam String name) {
        return ApiResponse.<Page<CouponResponse>>builder()
                .message("Get Coupon By Id")
                .data(couponService.getCouponByName(name, PageRequest.of(page -1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @GetMapping("/code")
    ApiResponse<Page<CouponResponse>> getCouponByCode(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit,
            @RequestParam String code) {
        return ApiResponse.<Page<CouponResponse>>builder()
                .message("Get Coupon By Id")
                .data(couponService.getCouponByCode(code, PageRequest.of(page -1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ApiResponse.<String>builder()
                .message("Deleted Coupon Successfully!")
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<?> updateCoupon(@PathVariable Long id, @RequestBody CouponRequest request) {
        couponService.updateCoupon(id, request);
        return ApiResponse.builder()
                .message("Update Coupon Successfully!")
                .build();
    }

    @PutMapping("/restore/{id}")
    ApiResponse<?> restoreCoupon(@PathVariable Long id) {
        couponService.restoreCoupon(id);
        return ApiResponse.builder()
                .message("Restore coupon successfully")
                .build();
    }

    @DeleteMapping("/in-trash/{id}")
    ApiResponse<?> moveToTrash(@PathVariable Long id) {
        couponService.moveToTrash(id);
        return ApiResponse.builder()
                .message("Move to trash coupon successfully")
                .build();
    }

    @GetMapping("/trash")
    ApiResponse<?> getInTrashCoupon(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "limit", defaultValue = "10") int limit){
        return ApiResponse.builder()
                .message("Get in trash coupon")
                .data(couponService.getInTrash(PageRequest.of(page -1, limit)))
                .build();
    }
}
