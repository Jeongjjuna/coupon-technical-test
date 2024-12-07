package org.coupon.coupon.domain;

import org.coupon.common.exception.CouponException;
import org.coupon.common.exception.CouponExceptionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("[domain 단위테스트] Coupon")
class CouponTest {

    @DisplayName("쿠폰을 정지할 수 있다.")
    @Test
    void suspendCoupon() {
        // given
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .issuedCouponCount(0)
                .build();

        // when
        coupon.suspend();

        // then
        assertThat(coupon.getSuspendedAt()).isNotNull();
    }

    @DisplayName("쿠폰이 정지되었는지 알 수 있다.")
    @Test
    void suspendCouponIsSuspended() {
        // given
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .issuedCouponCount(0)
                .suspendedAt(LocalDateTime.now())
                .build();

        // when
        var result = coupon.isSuspended();

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("이미 정지된 쿠폰은 정지할 수 없다.")
    @Test
    void suspendCouponAlreadySuspended() {
        // given
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .issuedCouponCount(0)
                .suspendedAt(LocalDateTime.now())
                .build();

        // when & then
        assertThatThrownBy(coupon::suspend)
                .isInstanceOf(CouponException.class)
                .extracting("errorStatus")
                .isEqualTo(CouponExceptionStatus.ALREADY_SUSPENDED_COUPON_CODE);
    }

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