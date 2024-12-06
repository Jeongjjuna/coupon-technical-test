package org.coupon.coupon.presentation.request;

import lombok.Builder;
import lombok.NonNull;
import org.coupon.coupon.domain.CouponCreate;

@Builder
public record CouponCreateRequest(
        @NonNull
        String couponType,
        @NonNull
        String description,
        @NonNull
        Integer totalCouponCount
) {
    public CouponCreate toDomain() {
        return CouponCreate.of(
                couponType,
                description,
                totalCouponCount
        );
    }
}
