package org.coupon.coupon.domain;

import lombok.Builder;
import lombok.Getter;
import org.coupon.common.exception.CouponException;
import org.coupon.common.exception.CouponExceptionStatus;


@Builder
@Getter
public class CouponCreate {
    public static final int MIN_COUPON_COUNT_THRESHOLD = 1;

    private static final int INITIAL_ISSUED_COUPON_COUNT = 0;

    private final CouponType couponType;
    private final String description;
    private final Integer totalCouponCount;
    private final Integer issuedCouponCount;

    private CouponCreate(
            CouponType couponType,
            String description,
            Integer totalCouponCount,
            Integer issuedCouponCount
    ) {
        this.couponType = couponType;
        this.description = description;
        this.totalCouponCount = totalCouponCount;
        this.issuedCouponCount = issuedCouponCount;
    }

    public static CouponCreate of(
            String couponTypeName,
            String description,
            Integer totalCouponCount
    ) {
        validateTotalCouponCount(totalCouponCount);

        CouponType createdCouponType = CouponType.createBy(couponTypeName);
        return new CouponCreate(
                createdCouponType,
                description,
                totalCouponCount,
                INITIAL_ISSUED_COUPON_COUNT
        );
    }

    private static void validateTotalCouponCount(Integer totalCouponCount) {
        if (totalCouponCount < MIN_COUPON_COUNT_THRESHOLD) {
            throw new CouponException(CouponExceptionStatus.NOT_ENOUGH_COUPONS);
        }
    }
}
