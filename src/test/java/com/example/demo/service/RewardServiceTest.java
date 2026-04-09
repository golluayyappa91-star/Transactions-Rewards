package com.example.demo.service;

import com.example.demo.dto.RewardResponse;
import com.example.demo.entity.TransactionEntity;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    TransactionRepository repo;

    @InjectMocks
    RewardService service;

    @Test
    void getRewards_whenTransactionsIsNull_shouldThrowException() {
        when(repo.findByCustomerId("C001")).thenReturn(null);
        assertThrows(CustomerNotFoundException.class, () -> service.getRewards("C001"));
    }

    @Test
    void getRewards_whenTransactionsIsEmpty_shouldReturnZeroPoints() {
        when(repo.findByCustomerId("C001")).thenReturn(Collections.emptyList());
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> service.getRewards("C001"));
        assertEquals("No transactions found for customer: C001", exception.getMessage());
    }

    @Test
    void calculatePoints_amountLessThanOrEqual50_shouldReturnZero() {
        assertEquals(0, service.calculatePoints(50));
        assertEquals(0, service.calculatePoints(30));
        assertEquals(0, service.calculatePoints(0));
    }

    @Test
    void calculatePoints_amountBetween50And100_shouldReturnCorrectPoints() {
        assertEquals(25, service.calculatePoints(75));
        assertEquals(1, service.calculatePoints(51));
    }

    @Test
    void calculatePoints_amountGreaterThan100_shouldReturnCorrectPoints() {
        assertEquals(150, service.calculatePoints(150));
        assertEquals(90, service.calculatePoints(120));
    }

    @Test
    void calculatePoints_amountEquals100_shouldReturn50() {
        assertEquals(50, service.calculatePoints(100));
    }
}
