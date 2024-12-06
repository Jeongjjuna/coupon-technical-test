package org.coupon.coupon.application;

import org.coupon.common.exception.CouponException;
import org.coupon.common.exception.CouponExceptionStatus;
import org.coupon.coupon.domain.Coupon;
import org.coupon.coupon.domain.CouponCreate;
import org.coupon.coupon.domain.CouponType;
import org.coupon.coupon.infrastructure.CouponJpaRepository;
import org.coupon.coupon.infrastructure.entity.CouponEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @DisplayName("쿠폰을 조회할 수 있다.")
    @Test
    void getCoupon() {
        // given
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .issuedCouponCount(0)
                .build();
        Long couponId = couponJpaRepository.save(CouponEntity.from(coupon))
                .toDomain()
                .getId();

        // when
        Coupon findCoupon = couponService.getCoupon(couponId);

        // then
        assertThat(findCoupon.getId()).isEqualTo(couponId);
    }

    @DisplayName("쿠폰이 존재하지 않으면 찾을 수 없다.")
    @Test
    void getCouponNotExists() {
        assertThatThrownBy(() -> couponService.getCoupon(99999L))
                .isInstanceOf(CouponException.class)
                .extracting("errorStatus")
                .isEqualTo(CouponExceptionStatus.NOT_FOUND);
    }

    @DisplayName("쿠폰을 발행해서 발급 수량을 증가시킨다.")
    @Test
    void issueCoupon() {
        // given
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .issuedCouponCount(0)
                .build();
        Long couponId = couponJpaRepository.save(CouponEntity.from(coupon))
                .toDomain()
                .getId();

        // when
        Coupon issuedCoupon = couponService.issue(couponId);

        // then
        assertThat(issuedCoupon.getIssuedCouponCount()).isEqualTo(1);
    }
}