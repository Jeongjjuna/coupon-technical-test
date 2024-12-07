package org.coupon.concurrency;

import org.coupon.coupon.domain.Coupon;
import org.coupon.coupon.domain.CouponType;
import org.coupon.coupon.infrastructure.CouponJpaRepository;
import org.coupon.coupon.infrastructure.entity.CouponEntity;
import org.coupon.issue.application.CouponIssueService;
import org.coupon.issue.domain.CouponIssue;
import org.coupon.issue.domain.CouponRedeemCommand;
import org.coupon.issue.infrastructure.jpa.CouponIssueJpaRepository;
import org.coupon.issue.infrastructure.jpa.entity.CouponIssueEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[동시성 테스트] RedeemService")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedeemConcurrencyTest {

    @Autowired
    private CouponIssueService couponIssueService;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private CouponIssueJpaRepository couponIssueJpaRepository;

    @AfterEach
    void clearAfter() {
        couponJpaRepository.deleteAll();
        couponIssueJpaRepository.deleteAll();
    }

    @DisplayName("단일 쿠폰 코드에 대해 동시 요청에도 중복 사용되지 않는다.")
    @Test
    void couponCodeRedeemConcurrency() throws InterruptedException {
        // given
        Coupon coupon = createValidCoupon();
        Long memberId = 1L;
        CouponIssue couponIssue = createValidCouponIssue(memberId, coupon.getId());

        int numberOfThreads = 300;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        AtomicInteger exceptionCount = new AtomicInteger(0);

        // when
        for (int idx = 1; idx <= numberOfThreads; idx++) {
            executor.submit(() -> {
                try {
                    couponIssueService.redeem(CouponRedeemCommand.builder()
                            .memberId(memberId)
                            .couponCode(couponIssue.getCouponCode())
                            .build()
                    );
                } catch (Exception e) {
                    /**
                     * 1개를 제외하고 전부 예외가 발생한다.
                     * 발생하는 예외 개수를 카운트 한다.
                     */
                    exceptionCount.addAndGet(1);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // 모든 스레드 동시에 시작
        doneLatch.await(); // 모든 스레드 종료 대기
        executor.shutdown();

        // then
        assertThat(exceptionCount.get()).isEqualTo(numberOfThreads - 1); // 한번 빼고 전부 실패해야함.
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

    private CouponIssue createValidCouponIssue(Long memberId, Long couponId) {
        CouponIssue couponIssue = CouponIssue.builder()
                .couponCode("1234567891011abc")
                .memberId(memberId)
                .couponId(couponId)
                .isUsed(false)
                .build();
        return couponIssueJpaRepository.save(CouponIssueEntity.from(couponIssue))
                .toDomain();
    }

}
