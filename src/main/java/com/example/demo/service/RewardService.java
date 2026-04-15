package com.example.demo.service;

import com.example.demo.calculator.RewardCalculator;
import com.example.demo.dto.CustomerReward;
import com.example.demo.dto.MonthlyReward;
import com.example.demo.entity.TransactionEntity;
import com.example.demo.exception.InvalidDateRangeException;
import com.example.demo.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardService {
    private static final Logger log = LoggerFactory.getLogger(RewardService.class);

    private final TransactionRepository repo;
    private final RewardCalculator calculator;

    public RewardService(TransactionRepository repo, RewardCalculator calculator) {
        this.repo = repo;
        this.calculator = calculator;
    }



    public List<CustomerReward> calculateRewards(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);

        List<TransactionEntity> transactions = repo.findByDateBetween(startDate, endDate);
        log.info("Found {} transactions between {} and {}", transactions.size(), startDate, endDate);

        return transactions.stream()
            .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) > 0)
            .collect(Collectors.groupingBy(TransactionEntity::getCustomerId))
            .entrySet().stream()
            .map(entry -> buildCustomerReward(entry.getKey(), entry.getValue()))
            .sorted((a, b) -> a.getCustomerId().compareTo(b.getCustomerId()))
            .collect(Collectors.toList());
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidDateRangeException("Both startDate and endDate must be provided");
        }
        if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            throw new InvalidDateRangeException("startDate must be before endDate");
        }
        if (endDate.isAfter(startDate.plusMonths(3))) {
            throw new InvalidDateRangeException("Date range must not exceed 3 months");
        }
    }

    private CustomerReward buildCustomerReward(String customerId, List<TransactionEntity> transactions) {
        Map<YearMonth, Integer> monthlyPoints = transactions.stream()
            .collect(Collectors.groupingBy(
                t -> YearMonth.from(t.getDate()),
                Collectors.summingInt(t -> calculator.calculatePoints(t.getAmount()))
            ));

        List<MonthlyReward> monthlyRewards = monthlyPoints.entrySet().stream()
            .map(e -> new MonthlyReward(e.getKey().getYear(), e.getKey().getMonthValue(), e.getValue()))
            .sorted((a, b) -> {
                int yearComp = Integer.compare(a.getYear(), b.getYear());
                return yearComp != 0 ? yearComp : Integer.compare(a.getMonth(), b.getMonth());
            })
            .collect(Collectors.toList());

        int totalPoints = monthlyRewards.stream().mapToInt(MonthlyReward::getPoints).sum();

        return new CustomerReward(customerId, monthlyRewards, totalPoints);
    }
}
