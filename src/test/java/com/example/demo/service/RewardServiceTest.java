package com.example.demo.service;

import com.example.demo.calculator.RewardCalculator;
import com.example.demo.dto.CustomerReward;
import com.example.demo.entity.TransactionEntity;
import com.example.demo.exception.InvalidDateRangeException;
import com.example.demo.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    TransactionRepository repo;

    @Mock
    RewardCalculator calculator;

    @InjectMocks
    RewardService service;



    @Test
    void calculateRewards_whenNoTransactions_shouldReturnEmptyList() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 3, 31);
        when(repo.findByDateBetween(start, end)).thenReturn(Collections.emptyList());
        List<CustomerReward> result = service.calculateRewards(start, end);
        assertEquals(0, result.size());
    }

    @Test
    void calculateRewards_startDateAfterEndDate_shouldThrowException() {
        LocalDate start = LocalDate.of(2026, 3, 1);
        LocalDate end = LocalDate.of(2026, 1, 1);
        assertThrows(InvalidDateRangeException.class, () -> service.calculateRewards(start, end));
    }

    @Test
    void calculateRewards_dateRangeExceeds3Months_shouldThrowException() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 5, 1);
        assertThrows(InvalidDateRangeException.class, () -> service.calculateRewards(start, end));
    }

    @Test
    void calculateRewards_nullDates_shouldThrowException() {
        assertThrows(InvalidDateRangeException.class, () -> service.calculateRewards(null, LocalDate.now()));
        assertThrows(InvalidDateRangeException.class, () -> service.calculateRewards(LocalDate.now(), null));
    }

    @Test
    void calculateRewards_validDateRange_shouldReturnCorrectRewards() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 3, 31);
        List<TransactionEntity> transactions = List.of(
            new TransactionEntity("C001", new BigDecimal("120"), LocalDate.of(2026, 1, 10)),
            new TransactionEntity("C001", new BigDecimal("80"), LocalDate.of(2026, 2, 15))
        );
        when(repo.findByDateBetween(start, end)).thenReturn(transactions);
        when(calculator.calculatePoints(new BigDecimal("120"))).thenReturn(90);
        when(calculator.calculatePoints(new BigDecimal("80"))).thenReturn(30);
        List<CustomerReward> result = service.calculateRewards(start, end);
        assertEquals(1, result.size());
        assertEquals("C001", result.get(0).getCustomerId());
        assertEquals(120, result.get(0).getTotalPoints());
    }
}
