package org.coupon.issue.presentation.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CouponCodeResponse(
        String couponCode
) {
    public static CouponCodeResponse from(String couponCode) {
        return new CouponCodeResponse(couponCode);
    }
}
