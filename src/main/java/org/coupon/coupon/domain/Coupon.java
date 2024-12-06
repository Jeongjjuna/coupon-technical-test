package org.coupon.coupon.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class Coupon {
    private Long id;
    private CouponType couponType;
    private String description;
    private Integer totalCouponCount;
    private Integer issuedCouponCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime suspendedAt;

    public static Coupon create(CouponCreate couponCreate) {
        return Coupon.builder()
                .couponType(couponCreate.getCouponType())
                .description(couponCreate.getDescription())
                .totalCouponCount(couponCreate.getTotalCouponCount())
                .issuedCouponCount(couponCreate.getIssuedCouponCount())
                .build();
    }
}
