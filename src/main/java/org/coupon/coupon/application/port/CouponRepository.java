package org.coupon.coupon.application.port;

import org.coupon.coupon.domain.Coupon;

import java.util.Optional;

public interface CouponRepository {
    Coupon save(Coupon coupon);

    Optional<Coupon> findById(Long couponId);

    Optional<Coupon> findByIdWithLock(Long couponId);
}
