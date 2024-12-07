package org.coupon.coupon.domain;

import lombok.Builder;
import lombok.Getter;
import org.coupon.common.exception.CouponException;
import org.coupon.common.exception.CouponExceptionStatus;

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

    public void issue() {
        if (totalCouponCount < issuedCouponCount + 1) {
            throw new CouponException(CouponExceptionStatus.NO_MORE_COUPONS);
        }
        issuedCouponCount++;
    }

    public void suspend() {
        if (isSuspended()) {
            throw new CouponException(CouponExceptionStatus.ALREADY_SUSPENDED_COUPON_CODE);
        }
        suspendedAt = LocalDateTime.now();
    }

    public boolean isSuspended() {
        return suspendedAt != null;
    }
}
