package org.coupon.issue.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CouponRedeemCommand {
    private Long memberId;
    private String couponCode;

    public static CouponRedeemCommand of(Long memberId, String couponCode) {
        return CouponRedeemCommand.builder()
                .memberId(memberId)
                .couponCode(couponCode)
                .build();
    }
}
