package org.coupon.coupon.domain;

import java.time.LocalDateTime;

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
        Coupon coupon = new Coupon();
        coupon.couponType = couponCreate.getCouponType();
        coupon.description = couponCreate.getDescription();
        coupon.totalCouponCount = couponCreate.getTotalCouponCount();
        coupon.issuedCouponCount = couponCreate.getIssuedCouponCount();
        return coupon;
    }
}
