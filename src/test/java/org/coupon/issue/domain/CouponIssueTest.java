package org.coupon.issue.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[domain 단위테스트] CouponIssue")
class CouponIssueTest {

    @DisplayName("쿠폰을 사용할 수 있다.")
    @Test
    void redeemCouponCode() {
        // given
        CouponIssue couponIssue = CouponIssue.builder()
                .couponCode("1234567891011abc")
                .memberId(1L)
                .couponId(1L)
                .isUsed(false)
                .createdAt(LocalDateTime.of(2024, 11, 11, 0, 0))
                .updatedAt(LocalDateTime.of(2024, 11, 11, 0, 0))
                .build();

        // when
        couponIssue.redeem();

        // then
        assertThat(couponIssue.getIsUsed()).isEqualTo(true);
    }

    @DisplayName("이미 사용된 쿠폰인지 알 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"false, false", "true, true"}, delimiter = ',')
    void isUsedCouponCode(boolean used, boolean expected) {
        // given
        CouponIssue couponIssue = CouponIssue.builder()
                .couponCode("1234567891011abc")
                .memberId(1L)
                .couponId(1L)
                .isUsed(used)
                .createdAt(LocalDateTime.of(2024, 11, 11, 0, 0))
                .updatedAt(LocalDateTime.of(2024, 11, 11, 0, 0))
                .build();

        // when
        couponIssue.isUsed();

        // then
        assertThat(couponIssue.getIsUsed()).isEqualTo(expected);
    }

    @DisplayName("발급받은사용자가 아닌지 알 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"999, false", "888, true"}, delimiter = ',')
    void isNotIssuer(Long inputMemberId, boolean expected) {
        // given
        CouponIssue couponIssue = CouponIssue.builder()
                .couponCode("1234567891011abc")
                .memberId(999L)
                .couponId(1L)
                .isUsed(false)
                .createdAt(LocalDateTime.of(2024, 11, 11, 0, 0))
                .updatedAt(LocalDateTime.of(2024, 11, 11, 0, 0))
                .build();

        // when
        var result = couponIssue.isNotIssuer(inputMemberId);

        // then
        assertThat(result).isEqualTo(expected);
    }
}