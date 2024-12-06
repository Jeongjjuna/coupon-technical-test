package org.coupon.coupon.domain;

import org.coupon.common.exception.CouponException;
import org.coupon.common.exception.CouponExceptionStatus;

import java.util.Arrays;

public enum CouponType {
    BASIC("기본타입");

    private final String description;

    CouponType(String description) {
        this.description = description;
    }

    public static CouponType createBy(String couponTypeName) {
        return Arrays.stream(CouponType.values())
                .filter(couponType -> couponTypeName.equals(couponType.name()))
                .findFirst()
                .orElseThrow(() -> new CouponException(CouponExceptionStatus.UNSUPPORTED_COUPON_TYPE));
    }
}
