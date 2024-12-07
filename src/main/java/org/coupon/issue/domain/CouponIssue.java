package org.coupon.issue.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CouponIssue {
    private String couponCode;
    private Long memberId;
    private Long couponId;
    private Boolean isUsed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CouponIssue create(CouponIssueCommand command, String couponCode) {
        return CouponIssue.builder()
                .couponCode(couponCode)
                .memberId(command.getMemberId())
                .couponId(command.getCouponId())
                .isUsed(command.getIsUsed())
                .build();
    }

    public void redeem() {
        isUsed = true;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public boolean isNotIssuer(Long memberId) {
        return !this.memberId.equals(memberId);
    }
}