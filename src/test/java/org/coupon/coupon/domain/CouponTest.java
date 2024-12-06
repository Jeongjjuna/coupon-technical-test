package org.coupon.coupon.domain;

import org.coupon.common.exception.CouponException;
import org.coupon.common.exception.CouponExceptionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("[domain 단위테스트] Coupon")
class CouponTest {

    @DisplayName("쿠폰을 생성할 수 있다.")
    @Test
    void createCoupon() {
        // given
        CouponCreate couponCreate = CouponCreate.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .build();

        // when & then
        assertThatCode(() -> Coupon.create(couponCreate))
                .doesNotThrowAnyException();
    }

    @DisplayName("쿠폰 발행으로 발급 수량이 증가한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 99})
    void issueCoupon(int issuedCoupon) {
        // given
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .issuedCouponCount(issuedCoupon)
                .build();

        // when
        coupon.issue();

        // then
        assertThat(coupon.getIssuedCouponCount()).isEqualTo(issuedCoupon + 1);
    }

    @DisplayName("잔여 수량이 없으면 쿠폰을 발급할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {100, 101, 999})
    void issueCouponWhenNoMoreCoupon(int issuedCoupon) {
        // given
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .issuedCouponCount(issuedCoupon)
                .build();

        // when & then
        assertThatThrownBy(coupon::issue)
                .isInstanceOf(CouponException.class)
                .extracting("errorStatus")
                .isEqualTo(CouponExceptionStatus.NO_MORE_COUPONS);
    }
}