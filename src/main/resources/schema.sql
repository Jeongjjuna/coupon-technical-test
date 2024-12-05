DROP TABLE IF EXISTS coupon;

CREATE TABLE coupon
(
    `id`                  BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 id',
    `coupon_type`         VARCHAR(128) NOT NULL COMMENT '쿠폰 타입',
    `description`         VARCHAR(512) NOT NULL COMMENT '쿠폰 설명',
    `total_coupon_count`  INT          NOT NULL COMMENT '최초 쿠폰 등록 개수',
    `issued_coupon_count` INT          NOT NULL COMMENT '발급한 쿠폰 개수', -- 수량 감소 예정
    `created_at`          TIMESTAMP    NOT NULL COMMENT '쿠폰 생성 시간',
    `updated_at`          TIMESTAMP    NOT NULL COMMENT '쿠폰 수정 시간',
    `suspended_at`        TIMESTAMP NULL COMMENT '쿠폰 정지 시간'
);

DROP TABLE IF EXISTS coupon_issue;

CREATE TABLE coupon_issue
(
    `coupon_code` VARCHAR(64) NOT NULL PRIMARY KEY COMMENT '발급 쿠폰 코드',
    `member_id`   BIGINT      NOT NULL COMMENT '발급받은 회원 id',
    `coupon_id`   BIGINT      NOT NULL COMMENT '쿠폰 id',
    `is_used`     BOOLEAN     NOT NULL COMMENT '쿠폰 사용 여부', -- 사용되면 true
    `created_at`  TIMESTAMP   NOT NULL COMMENT '쿠폰 발급 시간', -- 쿠폰 발급 시간
    `updated_at`  TIMESTAMP   NOT NULL COMMENT '쿠폰 사용 시간'  -- 쿠폰 사용 시간
)
