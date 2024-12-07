package org.coupon.issue.application;

import org.coupon.common.exception.CouponException;
import org.coupon.common.exception.CouponExceptionStatus;
import org.coupon.coupon.domain.Coupon;
import org.coupon.coupon.domain.CouponType;
import org.coupon.coupon.infrastructure.CouponJpaRepository;
import org.coupon.coupon.infrastructure.entity.CouponEntity;
import org.coupon.issue.domain.CouponIssue;
import org.coupon.issue.domain.CouponIssueCommand;
import org.coupon.issue.domain.CouponRedeemCommand;
import org.coupon.issue.infrastructure.jpa.CouponIssueJpaRepository;
import org.coupon.issue.infrastructure.jpa.entity.CouponIssueEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

    @DisplayName("정지된 쿠폰유형의 쿠폰코드를 발급할 수 없다.")
    @Test
    void subtractCouponCountCouponCodeSuspendCoupon() {
        // given
        Coupon coupon = createSuspendCoupon();

        CouponIssueCommand couponIssueCommand = CouponIssueCommand.builder()
                .couponId(coupon.getId())
                .memberId(999L)
                .build();

        // when & then
        assertThatThrownBy(() -> couponIssueService.issueCouponCode(couponIssueCommand))
                .isInstanceOf(CouponException.class)
                .extracting("errorStatus")
                .isEqualTo(CouponExceptionStatus.ALREADY_SUSPENDED_COUPON_CODE);
    }

    @DisplayName("정지된 쿠폰유형의 쿠폰코드는 사용할 수 없다.")
    @Test
    void redeemCouponCodeSuspendCoupon() {
        Coupon coupon = createSuspendCoupon();
        CouponIssue couponIssue = createValidCouponSubtractCouponCount(10L, coupon.getId());

        CouponRedeemCommand command = CouponRedeemCommand.builder()
                .memberId(couponIssue.getMemberId())
                .couponCode(couponIssue.getCouponCode())
                .build();

        // when & then
        assertThatThrownBy(() -> couponIssueService.redeem(command))
                .isInstanceOf(CouponException.class)
                .extracting("errorStatus")
                .isEqualTo(CouponExceptionStatus.ALREADY_SUSPENDED_COUPON_CODE);
    }

    @DisplayName("쿠폰코드를 발급할 수 있다.")
    @Test
    void subtractCouponCountCouponCode() {
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

    @DisplayName("쿠폰 수량 재고가 없다면 발급할 수 없다.")
    @Test
    void subtractCouponCountCouponCodeSoldOut() {
        // given
        Coupon coupon = createSoldOutCoupon();

        CouponIssueCommand couponIssueCommand = CouponIssueCommand.builder()
                .couponId(coupon.getId())
                .memberId(999L)
                .build();

        // when & then
        assertThatThrownBy(() -> couponIssueService.issueCouponCode(couponIssueCommand))
                .isInstanceOf(CouponException.class)
                .extracting("errorStatus")
                .isEqualTo(CouponExceptionStatus.NO_MORE_COUPONS);
    }

    @DisplayName("쿠폰코드를 사용할 수 있다.")
    @Test
    void redeemCouponCode() {
        // given
        Coupon coupon = createValidCoupon();
        CouponIssue couponIssue = createValidCouponSubtractCouponCount(10L, coupon.getId());

        CouponRedeemCommand command = CouponRedeemCommand.builder()
                .memberId(couponIssue.getMemberId())
                .couponCode(couponIssue.getCouponCode())
                .build();

        // when & then
        assertDoesNotThrow(() -> couponIssueService.redeem(command));
    }

    @DisplayName("쿠폰코드 발급자가 아니라면 예외가 발생한다.")
    @Test
    void redeemCouponCodeNotIssuer() {
        // given
        Coupon coupon = createValidCoupon();
        CouponIssue couponIssue = createValidCouponSubtractCouponCount(10L, coupon.getId());

        CouponRedeemCommand command = CouponRedeemCommand.builder()
                .memberId(999L)
                .couponCode(couponIssue.getCouponCode())
                .build();

        // when & then
        assertThatThrownBy(() -> couponIssueService.redeem(command))
                .isInstanceOf(CouponException.class)
                .extracting("errorStatus")
                .isEqualTo(CouponExceptionStatus.NOT_COUPON_CODE_ISSUER);
    }

    @DisplayName("이미 사용된 쿠폰코드를 사용하려고 하면 예외가 발생한다.")
    @Test
    void redeemCouponCodeAlreadyUsed() {
        // given
        Long issuerId = 10L;
        Coupon coupon = createValidCoupon();
        CouponIssue couponIssue = createUsedCouponSubtractCouponCount(issuerId, coupon.getId());

        CouponRedeemCommand command = CouponRedeemCommand.builder()
                .memberId(issuerId)
                .couponCode(couponIssue.getCouponCode())
                .build();

        // when & then
        assertThatThrownBy(() -> couponIssueService.redeem(command))
                .isInstanceOf(CouponException.class)
                .extracting("errorStatus")
                .isEqualTo(CouponExceptionStatus.ALREADY_USED_COUPON_CODE);
    }

    private CouponIssue createValidCouponSubtractCouponCount(Long memberId, Long couponId) {
        CouponIssue couponIssue = CouponIssue.builder()
                .couponCode("1234567891011abc")
                .memberId(memberId)
                .couponId(couponId)
                .isUsed(false)
                .build();
        return couponIssueJpaRepository.save(CouponIssueEntity.from(couponIssue))
                .toDomain();
    }

    private CouponIssue createUsedCouponSubtractCouponCount(Long memberId, Long couponId) {
        CouponIssue couponIssue = CouponIssue.builder()
                .couponCode("1234567891011abc")
                .memberId(memberId)
                .couponId(couponId)
                .isUsed(true)
                .build();
        return couponIssueJpaRepository.save(CouponIssueEntity.from(couponIssue))
                .toDomain();
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

    private Coupon createSuspendCoupon() {
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .issuedCouponCount(0)
                .suspendedAt(LocalDateTime.now())
                .build();
        return couponJpaRepository.save(CouponEntity.from(coupon))
                .toDomain();
    }

    private Coupon createSoldOutCoupon() {
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(100)
                .issuedCouponCount(100)
                .suspendedAt(LocalDateTime.now())
                .build();
        return couponJpaRepository.save(CouponEntity.from(coupon))
                .toDomain();
    }
}