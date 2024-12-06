package org.coupon.issue.presentation.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.coupon.issue.domain.CouponIssueCommand;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CouponIssueRequest(
        @NotNull(message = "쿠폰 코드를 발급받을 회원 id 는 null 일 수 없습니다.")
        Long memberId,

        @NotNull(message = "쿠폰 코드를 발급받을 쿠폰 id 는 null 일 수 없습니다.")
        Long couponId
) {

    public CouponIssueCommand toDomain() {
        return CouponIssueCommand.of(
                memberId,
                couponId
        );
    }
}
