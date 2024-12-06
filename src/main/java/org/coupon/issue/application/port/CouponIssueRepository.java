package org.coupon.issue.application.port;

import org.coupon.issue.domain.CouponIssue;

public interface CouponIssueRepository {
    CouponIssue save(CouponIssue issue);
}
