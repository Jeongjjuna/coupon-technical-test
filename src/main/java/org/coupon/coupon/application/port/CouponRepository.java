package org.coupon.coupon.application.port;

import org.coupon.coupon.domain.Coupon;

public interface CouponRepository {
    Coupon save(Coupon coupon);
}
