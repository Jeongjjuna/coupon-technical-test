package org.coupon.concurrency;

import org.coupon.coupon.domain.Coupon;
import org.coupon.coupon.domain.CouponType;
import org.coupon.coupon.infrastructure.CouponJpaRepository;
import org.coupon.coupon.infrastructure.entity.CouponEntity;
import org.coupon.issue.application.CouponIssueService;
import org.coupon.issue.domain.CouponIssueCommand;
import org.coupon.issue.infrastructure.jpa.CouponIssueJpaRepository;
import org.coupon.issue.infrastructure.jpa.entity.CouponIssueEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("[동시성 테스트] IssueService")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IssueConcurrencyTest {

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

    @DisplayName("쿠폰 코드 발급은 정해진 수량만큼 이루어져야 한다.")
    @Test
    void couponCodeIssueConcurrency() throws InterruptedException {
        // given
        int totalCouponCount = 10;
        Coupon coupon = createValidCoupon(totalCouponCount);
        Long memberId = 1L;

        int numberOfThreads = 300;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        // when
        for (int idx = 1; idx <= numberOfThreads; idx++) {
            executor.submit(() -> {
                try {
                    couponIssueService.issueCouponCode(CouponIssueCommand.builder()
                            .memberId(memberId)
                            .couponId(coupon.getId())
                            .isUsed(false)
                            .build()
                    );
                } catch (Exception e) {

                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // 모든 스레드 동시에 시작
        doneLatch.await(); // 모든 스레드 종료 대기
        executor.shutdown();

        // then
        Coupon findCoupon = couponJpaRepository.findById(coupon.getId())
                .get()
                .toDomain();
        List<CouponIssueEntity> couponIssueEntities = couponIssueJpaRepository.findAll();

        // 전체 수량만큼만 발급되었어야 한다.
        assertAll(
                () -> assertThat(findCoupon.getIssuedCouponCount()).isEqualTo(totalCouponCount),
                () -> assertThat(couponIssueEntities.size()).isEqualTo(totalCouponCount)
        );

    }

    private Coupon createValidCoupon(int totalCouponCount) {
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.BASIC)
                .description("description")
                .totalCouponCount(totalCouponCount)
                .issuedCouponCount(0)
                .build();
        return couponJpaRepository.save(CouponEntity.from(coupon))
                .toDomain();
    }
}
