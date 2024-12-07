package org.coupon.coupon.infrastructure;

import jakarta.persistence.LockModeType;
import org.coupon.coupon.infrastructure.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM coupon c where c.id = :couponId")
    Optional<CouponEntity> findByIdWithLock(Long couponId);
}
