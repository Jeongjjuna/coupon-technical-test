package org.coupon.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

import static org.coupon.coupon.domain.CouponCreate.MIN_COUPON_COUNT_THRESHOLD;

@Getter
public enum CouponExceptionStatus {

    // 400
    UNSUPPORTED_COUPON_TYPE(HttpStatus.BAD_REQUEST, "지원되지 않는 쿠폰 타입 입니다."),
    NOT_ENOUGH_COUPONS(HttpStatus.BAD_REQUEST, MessageFormat.format("쿠폰은 최소 {0}개 이상 생성해야 합니다.", MIN_COUPON_COUNT_THRESHOLD)),

    // 404
    NOT_FOUND_COUPON(HttpStatus.NOT_FOUND, "쿠폰을 찾을 수 없습니다."),
    NOT_FOUND_COUPON_ISSUE(HttpStatus.NOT_FOUND, "쿠폰 발행을 찾을 수 없습니다."),

    // 409
    NO_MORE_COUPONS(HttpStatus.CONFLICT, "쿠폰 재고가 부족합니다."),
    ALREADY_USED_COUPON_CODE(HttpStatus.CONFLICT, "이미 사용된 쿠폰코드 입니다."),
    NOT_COUPON_CODE_ISSUER(HttpStatus.CONFLICT, "쿠폰 발급자가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;

    CouponExceptionStatus(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
