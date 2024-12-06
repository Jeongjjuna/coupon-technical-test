package org.coupon.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

import static org.coupon.coupon.domain.CouponCreate.MIN_COUPON_COUNT_THRESHOLD;

@Getter
public enum CouponExceptionStatus {
    UNSUPPORTED_COUPON_TYPE(HttpStatus.BAD_REQUEST, "지원되지 않는 쿠폰 타입 입니다."),
    NOT_ENOUGH_COUPONS(HttpStatus.BAD_REQUEST, MessageFormat.format("쿠폰은 최소 {0}개 이상 생성해야 합니다.", MIN_COUPON_COUNT_THRESHOLD)),
    NOT_FOUND(HttpStatus.NOT_FOUND, "쿠폰을 찾을 수 없습니다."),
    NO_MORE_COUPONS(HttpStatus.CONFLICT, "쿠폰 재고가 부족합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    CouponExceptionStatus(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
