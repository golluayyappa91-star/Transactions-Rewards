package com.example.demo.calculator;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RewardCalculator {
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal FIFTY = new BigDecimal("50");

    public int calculatePoints(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        int points = 0;

        if (amount.compareTo(HUNDRED) > 0) {
            BigDecimal overHundred = amount.subtract(HUNDRED);
            points += overHundred.multiply(new BigDecimal("2")).intValue();
            points += 50;
        } else if (amount.compareTo(FIFTY) > 0) {
            BigDecimal overFifty = amount.subtract(FIFTY);
            points += overFifty.intValue();
        }

        return points;
    }
}
