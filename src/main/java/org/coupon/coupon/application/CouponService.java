package org.coupon.coupon.application;

import lombok.RequiredArgsConstructor;
import org.coupon.common.exception.CouponException;
import org.coupon.common.exception.CouponExceptionStatus;
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

    public Coupon subtractCouponCount(Long couponId) {
        Coupon coupon = getCouponWithLock(couponId);
        coupon.subtractCouponCount();
        return couponRepository.save(coupon);
    }

    public void suspend(Long couponId) {
        Coupon coupon = getCoupon(couponId);
        coupon.suspend();
        couponRepository.save(coupon);
    }

    public Coupon getCoupon(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponException(CouponExceptionStatus.NOT_FOUND_COUPON));
    }

    public void checkAlreadySuspendedBy(Long couponId) {
        Coupon coupon = getCoupon(couponId);
        if (coupon.isSuspended()) {
            throw new CouponException(CouponExceptionStatus.ALREADY_SUSPENDED_COUPON_CODE);
        }
    }

    private Coupon getCouponWithLock(Long couponId) {
        return couponRepository.findByIdWithLock(couponId)
                .orElseThrow(() -> new CouponException(CouponExceptionStatus.NOT_FOUND_COUPON));
    }

}
