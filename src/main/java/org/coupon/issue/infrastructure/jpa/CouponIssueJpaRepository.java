package org.coupon.issue.infrastructure.jpa;

import org.coupon.issue.infrastructure.jpa.entity.CouponIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssueEntity, Long> {
}
