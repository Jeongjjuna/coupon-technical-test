package org.coupon.issue.application;

import lombok.RequiredArgsConstructor;
import org.coupon.common.exception.CouponException;
import org.coupon.common.exception.CouponExceptionStatus;
import org.coupon.coupon.application.CouponService;
import org.coupon.issue.application.port.CouponCodeGenerator;
import org.coupon.issue.application.port.CouponIssueRepository;
import org.coupon.issue.domain.CouponIssue;
import org.coupon.issue.domain.CouponIssueCommand;
import org.coupon.issue.domain.CouponRedeemCommand;
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

        couponService.subtractCouponCount(command.getCouponId());

        return couponCode;
    }

    public synchronized void redeem(CouponRedeemCommand command) {
        CouponIssue couponIssue = getCouponIssue(command.getCouponCode());

        couponService.checkAlreadySuspendedBy(couponIssue.getCouponId());

        couponIssue.redeem(command.getMemberId());
        couponIssueRepository.save(couponIssue);
    }

    public CouponIssue getCouponIssue(String couponCode) {
        return couponIssueRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> new CouponException(CouponExceptionStatus.NOT_FOUND_COUPON_ISSUE));
    }
}
