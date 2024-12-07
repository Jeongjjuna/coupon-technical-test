package org.coupon.coupon.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.coupon.common.response.Api;
import org.coupon.coupon.application.CouponService;
import org.coupon.coupon.presentation.request.CouponCreateRequest;
import org.coupon.coupon.presentation.response.CouponSimpleResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coupons")
public class CouponController {
    private final CouponService couponService;

    /**
     * 쿠폰 등록 api
     */
    @PostMapping
    public Api<CouponSimpleResponse> create(
            @Valid @RequestBody CouponCreateRequest couponCreateRequest
    ) {
        var coupon = couponService.create(couponCreateRequest.toDomain());
        return Api.success(CouponSimpleResponse.from(coupon));
    }

    /**
     * 쿠폰 정지 api
     */
    @PostMapping("/{id}/suspend")
    public Api<Void> suspend(
            @PathVariable("id") Long couponId
    ) {
        couponService.suspend(couponId);
        return Api.success();
    }
}
