package org.coupon.issue.application.port;

import org.coupon.issue.domain.CouponIssue;

import java.util.Optional;

public interface CouponIssueRepository {
    CouponIssue save(CouponIssue issue);

    Optional<CouponIssue> findByCouponCode(String couponCode);
}