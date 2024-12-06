package org.coupon.coupon.infrastructure;

import lombok.RequiredArgsConstructor;
import org.coupon.coupon.application.port.CouponRepository;
import org.coupon.coupon.domain.Coupon;
import org.coupon.coupon.infrastructure.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CouponRepositoryAdapter implements CouponRepository {
    private final CouponJpaRepository couponRepository;

    @Override
    public Coupon save(Coupon coupon) {
        return couponRepository.save(CouponEntity.from(coupon))
                .toDomain();
    }

    @Override
    public Optional<Coupon> findById(Long couponId) {
        return couponRepository.findById(couponId)
                .map(CouponEntity::toDomain);
    }
}
