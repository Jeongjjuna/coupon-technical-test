package org.coupon.issue.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.coupon.common.response.Api;
import org.coupon.issue.application.CouponIssueService;
import org.coupon.issue.presentation.request.CouponIssueRequest;
import org.coupon.issue.presentation.request.CouponRedeemRequest;
import org.coupon.issue.presentation.response.CouponCodeResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/issues")
public class CouponIssueController {
    private final CouponIssueService couponIssueService;

    /**
     * 쿠폰 코드 발급 api
     */
    @PostMapping
    public Api<CouponCodeResponse> create(
            @Valid @RequestBody CouponIssueRequest couponIssueRequest
    ) {
        var couponCode = couponIssueService.issueCouponCode(couponIssueRequest.toDomain());
        return Api.success(CouponCodeResponse.from(couponCode));
    }

    /**
     * 쿠폰 코드를 통한 쿠폰 사용 api
     */
    @PostMapping("/redeem")
    public Api<Void> redeem(
            @Valid @RequestBody CouponRedeemRequest couponRedeemRequest
    ) {
        couponIssueService.redeem(couponRedeemRequest.toDomain());
        return Api.success();
    }
}
