package org.coupon.issue.application;

import lombok.RequiredArgsConstructor;
import org.coupon.coupon.application.CouponService;
import org.coupon.issue.application.port.CouponCodeGenerator;
import org.coupon.issue.application.port.CouponIssueRepository;
import org.coupon.issue.domain.CouponIssue;
import org.coupon.issue.domain.CouponIssueCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CouponIssueService {
    private final CouponService couponService;
    private final CouponIssueRepository couponIssueRepository;
    private final CouponCodeGenerator couponCodeGenerator;

    @Transactional
    public String issueCouponCode(CouponIssueCommand command) {
        String couponCode = couponCodeGenerator.generate();
        CouponIssue couponIssue = CouponIssue.create(command, couponCode);

        couponIssueRepository.save(couponIssue);
        couponService.issue(command.getCouponId());

        return couponCode;
    }
}
