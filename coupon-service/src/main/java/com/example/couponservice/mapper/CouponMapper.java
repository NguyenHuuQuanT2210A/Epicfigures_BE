package com.example.couponservice.mapper;

import com.example.couponservice.dto.request.CouponRequest;
import com.example.couponservice.dto.response.CouponResponse;
import com.example.couponservice.entities.Coupon;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    CouponResponse toCouponResponse(Coupon coupon);
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    Coupon toCoupon(CouponRequest request);

    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatedCoupon(@MappingTarget Coupon coupon, CouponRequest request);
}
