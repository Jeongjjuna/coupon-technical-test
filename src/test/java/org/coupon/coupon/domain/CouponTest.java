package org.coupon.coupon.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

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
}