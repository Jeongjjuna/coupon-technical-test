package org.coupon.common.exception;

import lombok.Getter;

@Getter
public class CouponException extends RuntimeException {
    private final CouponExceptionStatus errorStatus;

    public CouponException(CouponExceptionStatus status) {
        this.errorStatus = status;
    }

    public CouponException(CouponExceptionStatus status, Throwable cause) {
        super(cause);
        this.errorStatus = status;
    }
}
