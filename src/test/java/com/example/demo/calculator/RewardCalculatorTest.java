package com.example.demo.calculator;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardCalculatorTest {

    private final RewardCalculator calculator = new RewardCalculator();

    @Test
    void calculatePoints_amountLessThanOrEqual50_shouldReturnZero() {
        assertEquals(0, calculator.calculatePoints(new BigDecimal("50")));
        assertEquals(0, calculator.calculatePoints(new BigDecimal("30")));
        assertEquals(0, calculator.calculatePoints(new BigDecimal("0")));
    }

    @Test
    void calculatePoints_amountBetween50And100_shouldReturnCorrectPoints() {
        assertEquals(25, calculator.calculatePoints(new BigDecimal("75")));
        assertEquals(1, calculator.calculatePoints(new BigDecimal("51")));
        assertEquals(49, calculator.calculatePoints(new BigDecimal("99")));
    }

    @Test
    void calculatePoints_amountGreaterThan100_shouldReturnCorrectPoints() {
        assertEquals(150, calculator.calculatePoints(new BigDecimal("150")));
        assertEquals(90, calculator.calculatePoints(new BigDecimal("120")));
        assertEquals(350, calculator.calculatePoints(new BigDecimal("250")));
    }

    @Test
    void calculatePoints_amountEquals100_shouldReturn50() {
        assertEquals(50, calculator.calculatePoints(new BigDecimal("100")));
    }

    @Test
    void calculatePoints_amountJustOver100_shouldReturnCorrectPoints() {
        assertEquals(52, calculator.calculatePoints(new BigDecimal("101")));
    }

    @Test
    void calculatePoints_fractionalAmount_shouldHandleCorrectly() {
        assertEquals(51, calculator.calculatePoints(new BigDecimal("100.50")));
        assertEquals(51, calculator.calculatePoints(new BigDecimal("100.99")));
        assertEquals(25, calculator.calculatePoints(new BigDecimal("75.50")));
    }

    @Test
    void calculatePoints_negativeAmount_shouldReturnZero() {
        assertEquals(0, calculator.calculatePoints(new BigDecimal("-50")));
    }

    @Test
    void calculatePoints_nullAmount_shouldReturnZero() {
        assertEquals(0, calculator.calculatePoints(null));
    }
}
