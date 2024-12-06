package org.coupon.coupon.presentation.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.coupon.coupon.domain.CouponCreate;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CouponCreateRequest(
        @NotNull(message = "쿠폰타입은 null 일 수 없습니다.")
        String couponType,

        @NotNull(message = "쿠폰 설명은 null 일 수 없습니다.")
        String description,

        @NotNull(message = "쿠폰 개수는 null 일 수 없습니다.")
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
