package org.coupon.coupon.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.coupon.coupon.domain.Coupon;
import org.coupon.coupon.domain.CouponType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "coupon")
public class CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private CouponType couponType;

    private String description;

    private Integer totalCouponCount;

    private Integer issuedCouponCount;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime suspendedAt;

    public static CouponEntity from(Coupon coupon) {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.id = coupon.getId();
        couponEntity.couponType = coupon.getCouponType();
        couponEntity.description = coupon.getDescription();
        couponEntity.totalCouponCount = coupon.getTotalCouponCount();
        couponEntity.issuedCouponCount = coupon.getIssuedCouponCount();
        couponEntity.createdAt = coupon.getCreatedAt();
        couponEntity.updatedAt = coupon.getUpdatedAt();
        couponEntity.suspendedAt = coupon.getSuspendedAt();
        return couponEntity;
    }

    public Coupon toDomain() {
        return Coupon.builder()
                .id(id)
                .couponType(couponType)
                .description(description)
                .totalCouponCount(totalCouponCount)
                .issuedCouponCount(issuedCouponCount)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .suspendedAt(suspendedAt)
                .build();
    }
}
