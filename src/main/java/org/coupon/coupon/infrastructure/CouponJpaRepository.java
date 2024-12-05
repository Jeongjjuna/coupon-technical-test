package org.coupon.coupon.infrastructure;

import org.coupon.coupon.infrastructure.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long> {
}
