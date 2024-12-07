package org.coupon.issue.application;

import lombok.RequiredArgsConstructor;
import org.coupon.common.exception.CouponException;
import org.coupon.common.exception.CouponExceptionStatus;
import org.coupon.coupon.application.CouponService;
import org.coupon.coupon.domain.Coupon;
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
        Coupon coupon = couponService.getCoupon(command.getCouponId());

        checkAlreadySuspended(coupon);

        String couponCode = couponCodeGenerator.generate();
        CouponIssue couponIssue = CouponIssue.create(command, couponCode);
        couponIssueRepository.save(couponIssue);

        couponService.issue(command.getCouponId());

        return couponCode;
    }

    @Transactional
    public void redeem(CouponRedeemCommand command) {
        CouponIssue couponIssue = getCouponIssue(command.getCouponCode());
        Coupon coupon = couponService.getCoupon(couponIssue.getCouponId());

        checkAlreadySuspended(coupon);
        checkCouponCodeIssuer(couponIssue, command);
        checkAlreadyUsed(couponIssue);

        couponIssue.redeem();
        couponIssueRepository.save(couponIssue);
    }

    public CouponIssue getCouponIssue(String couponCode) {
        return couponIssueRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> new CouponException(CouponExceptionStatus.NOT_FOUND_COUPON_ISSUE));
    }

    private void checkAlreadySuspended(Coupon coupon) {
        if (coupon.isSuspended()) {
            throw new CouponException(CouponExceptionStatus.ALREADY_SUSPENDED_COUPON_CODE);
        }
    }

    private void checkAlreadyUsed(CouponIssue couponIssue) {
        if (couponIssue.isUsed()) {
            throw new CouponException(CouponExceptionStatus.ALREADY_USED_COUPON_CODE);
        }
    }

    private void checkCouponCodeIssuer(CouponIssue couponIssue, CouponRedeemCommand command) {
        if (couponIssue.isNotIssuer(command.getMemberId())) {
            throw new CouponException(CouponExceptionStatus.NOT_COUPON_CODE_ISSUER);
        }
    }
}
