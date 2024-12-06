package org.coupon.coupon.application;

import org.coupon.coupon.domain.Coupon;
import org.coupon.coupon.domain.CouponCreate;
import org.coupon.coupon.domain.CouponType;
import org.coupon.coupon.infrastructure.CouponJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[service 통합테스트] CouponService")
@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponServiceTest {

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private CouponService couponService;

    @DisplayName("쿠폰을 생성할 수 있다.")
    @Test
    void createCoupon() {
        // given
        CouponCreate couponCreate = CouponCreate.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .issuedCouponCount(0)
                .build();

        // when
        Coupon coupon = couponService.create(couponCreate);

        // then
        assertThat(coupon.getId()).isNotNull();
    }
}