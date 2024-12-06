package org.coupon.coupon.domain;

import org.coupon.common.exception.CouponException;
import org.coupon.common.exception.CouponExceptionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("[domain 단위테스트] CouponCreate")
class CouponCreateTest {

    @DisplayName("n개의 쿠폰을 생성할 수 있다.")
    @Test
    void createCouponCreate() {
        // given
        int numberOfCoupon = 100;

        // when
        CouponCreate coupon = CouponCreate.of("BASIC", "description", numberOfCoupon);

        // then
        assertAll(
                () -> assertThat(coupon.getTotalCouponCount()).isEqualTo(100),
                () -> assertThat(coupon.getIssuedCouponCount()).isEqualTo(0)
        );
    }

    @DisplayName("쿠폰은 최소 1개 이상 생성해야 한다.")
    @Test
    void createCouponCreateWithInvalidCouponCount() {
        // given
        int invalidNumberOfCoupon = 0;

        // when & then
        assertThatThrownBy(() -> CouponCreate.of("BASIC", "description", invalidNumberOfCoupon))
                .isInstanceOf(CouponException.class)
                .extracting("errorStatus")
                .isEqualTo(CouponExceptionStatus.NOT_ENOUGH_COUPONS);
    }

    @DisplayName("지원되지 않는 쿠폰타입으로 쿠폰을 생성할 수 없다.")
    @Test
    void createCouponCreateWithInvalidCouponType() {
        // given
        String unsupportedCouponType = "UNSUPPORTED";

        // when & then
        assertThatThrownBy(() -> CouponCreate.of(unsupportedCouponType, "description", 100))
                .isInstanceOf(CouponException.class)
                .extracting("errorStatus")
                .isEqualTo(CouponExceptionStatus.UNSUPPORTED_COUPON_TYPE);
    }
}