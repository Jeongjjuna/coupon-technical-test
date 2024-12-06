package org.coupon.issue.infrastructure.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.coupon.issue.domain.CouponIssue;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "coupon_issue")
public class CouponIssueEntity {
    @Id
    private String couponCode;

    private Long memberId;

    private Long couponId;

    private Boolean isUsed;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static CouponIssueEntity from(CouponIssue issue) {
        CouponIssueEntity couponIssueEntity = new CouponIssueEntity();
        couponIssueEntity.couponCode = issue.getCouponCode();
        couponIssueEntity.memberId = issue.getMemberId();
        couponIssueEntity.couponId = issue.getCouponId();
        couponIssueEntity.isUsed = issue.getIsUsed();
        return couponIssueEntity;
    }

    public CouponIssue toDomain() {
        return CouponIssue.builder()
                .couponCode(couponCode)
                .memberId(memberId)
                .couponId(couponId)
                .isUsed(isUsed)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
