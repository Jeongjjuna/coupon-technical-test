package org.coupon.issue.infrastructure;

import org.coupon.issue.application.port.CouponCodeGenerator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCouponCodeGenerator implements CouponCodeGenerator {
    private static final String RANDOM_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private final Random random = new Random();

    @Override
    public String generate() {
        String timeNumber = createTimeNumber(); // 현재 시간 기준 13자리
        String randomChar = createRandomChar();  // 랜덤 문자 3자리

        return timeNumber + randomChar;
    }

    private String createTimeNumber() {
        long currentTimeMillis = System.currentTimeMillis();
        return Long.toString(currentTimeMillis);
    }

    private String createRandomChar() {
        StringBuilder randomChar = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int index = random.nextInt(RANDOM_CHARACTERS.length());
            randomChar.append(RANDOM_CHARACTERS.charAt(index));
        }
        return randomChar.toString();
    }

}
