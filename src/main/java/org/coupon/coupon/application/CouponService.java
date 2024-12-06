package org.coupon.coupon.application;

import lombok.RequiredArgsConstructor;
import org.coupon.coupon.application.port.CouponRepository;
import org.coupon.coupon.domain.Coupon;
import org.coupon.coupon.domain.CouponCreate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CouponService {
    private final CouponRepository couponRepository;

    public Coupon create(CouponCreate couponCreate) {
        Coupon coupon = Coupon.create(couponCreate);

        return couponRepository.save(coupon);
    }
}
