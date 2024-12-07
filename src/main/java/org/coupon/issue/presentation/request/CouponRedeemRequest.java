package org.coupon.issue.presentation.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.coupon.issue.domain.CouponRedeemCommand;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CouponRedeemRequest(
        @NotNull(message = "쿠폰 코드를 사용할 회원 id 는 null 일 수 없습니다.")
        Long memberId,

        @NotNull(message = "쿠폰 코드는 null 일 수 없습니다.")
        String couponCode
) {

    public CouponRedeemCommand toDomain() {
        return CouponRedeemCommand.of(
                memberId,
                couponCode
        );
    }
}
