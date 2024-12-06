package org.coupon.issue.infrastructure.jpa;

import lombok.RequiredArgsConstructor;
import org.coupon.issue.application.port.CouponIssueRepository;
import org.coupon.issue.domain.CouponIssue;
import org.coupon.issue.infrastructure.jpa.entity.CouponIssueEntity;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CouponIssueRepositoryAdapter implements CouponIssueRepository {
    private final CouponIssueJpaRepository couponIssueRepository;

    @Override
    public CouponIssue save(CouponIssue couponIssue) {
        return couponIssueRepository.save(CouponIssueEntity.from(couponIssue)).toDomain();
    }
}
