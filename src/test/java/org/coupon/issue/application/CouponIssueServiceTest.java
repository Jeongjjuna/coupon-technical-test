package org.coupon.issue.application;

import org.coupon.coupon.domain.Coupon;
import org.coupon.coupon.domain.CouponType;
import org.coupon.coupon.infrastructure.CouponJpaRepository;
import org.coupon.coupon.infrastructure.entity.CouponEntity;
import org.coupon.issue.domain.CouponIssueCommand;
import org.coupon.issue.infrastructure.jpa.CouponIssueJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("[service 통합테스트] CouponIssueService")
@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponIssueServiceTest {

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private CouponIssueJpaRepository couponIssueJpaRepository;

    @Autowired
    private CouponIssueService couponIssueService;

    @DisplayName("쿠폰 코드를 발급할 수 있다.")
    @Test
    void issueCouponCode() {
        // given
        Coupon coupon = createValidCoupon();

        CouponIssueCommand couponIssueCommand = CouponIssueCommand.builder()
                .couponId(coupon.getId())
                .memberId(999L)
                .build();

        // when
        String couponCode = couponIssueService.issueCouponCode(couponIssueCommand);

        // then
        assertAll(
                () -> assertThat(couponCode.length()).isEqualTo(16),
                () -> assertThat(couponCode).matches(".*[A-Za-z].*"),
                () -> assertThat(couponCode).matches(".*[0-9].*")
        );
    }

    private Coupon createValidCoupon() {
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .issuedCouponCount(0)
                .build();
        return couponJpaRepository.save(CouponEntity.from(coupon))
                .toDomain();
    }
}