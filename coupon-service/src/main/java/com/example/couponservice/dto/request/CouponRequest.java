package com.example.couponservice.dto.request;

import com.example.couponservice.entities.base.BaseEntity;
import com.example.couponservice.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequest {
    private String name;
    private String description;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private String startDate;
    private String endDate;
    private Integer usageLimit;
    private BigDecimal minAmount;
    private Integer userLimit;
    private Long categoryId;
}
