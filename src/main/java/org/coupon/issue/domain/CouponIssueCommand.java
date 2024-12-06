package org.coupon.issue.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CouponIssueCommand {
    private static final boolean INIT_USED_VALUE = false;

    private Long memberId;
    private Long couponId;
    private Boolean isUsed;

    public static CouponIssueCommand of(Long memberId, Long couponId) {
        return CouponIssueCommand.builder()
                .memberId(memberId)
                .couponId(couponId)
                .isUsed(INIT_USED_VALUE)
                .build();
    }
}
