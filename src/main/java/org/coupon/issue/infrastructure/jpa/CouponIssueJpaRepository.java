package org.coupon.issue.infrastructure.jpa;

import org.coupon.issue.infrastructure.jpa.entity.CouponIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssueEntity, Long> {

    Optional<CouponIssueEntity> findByCouponCode(String couponCode);
}
