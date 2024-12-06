package org.coupon.coupon.presentation.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import org.coupon.coupon.domain.Coupon;
import org.coupon.coupon.domain.CouponType;

import java.time.LocalDateTime;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CouponSimpleResponse(
        Long id,
        CouponType couponType,
        String description,
        Integer totalCouponCount,
        Integer issuedCouponCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime suspendedAt
) {
    public static CouponSimpleResponse from(Coupon coupon) {
        return CouponSimpleResponse.builder()
                .id(coupon.getId())
                .couponType(coupon.getCouponType())
                .description(coupon.getDescription())
                .totalCouponCount(coupon.getTotalCouponCount())
                .issuedCouponCount(coupon.getIssuedCouponCount())
                .createdAt(coupon.getCreatedAt())
                .updatedAt(coupon.getUpdatedAt())
                .suspendedAt(coupon.getSuspendedAt())
                .build();
    }
}
