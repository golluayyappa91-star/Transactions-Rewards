package com.example.demo.service;

import com.example.demo.dto.RewardResponse;

import com.example.demo.entity.TransactionEntity;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardService {
    private static final Logger log = LoggerFactory.getLogger(RewardService.class);

    @Autowired
    TransactionRepository repo;

    public int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (int)((amount - 100) * 2);
            amount = 100;
        }
        if (amount > 50) {
            points += (int)(amount - 50);
        }
        return points;
    }

    public RewardResponse getRewards(String customerId) {

        List<TransactionEntity> transactions = repo.findByCustomerId(customerId);
        log.info("Transactions by the customer : {}" , transactions);
        if(transactions == null || transactions.isEmpty()){
            throw new CustomerNotFoundException("No transactions found for customer: " + customerId);
        }

        int firstMonthPoints = 0;
        int secondMonthPoints = 0;
        int thirdMonthPoints = 0;
        int total = 0;

        for (TransactionEntity t : transactions) {

            int points = calculatePoints(t.getTotal());
            int month = t.getDate().getMonthValue();

            if (month == 1) {
                firstMonthPoints += points;
            }
            else if (month == 2) {
                secondMonthPoints += points;
            }
            else if (month == 3) {
                thirdMonthPoints += points;
            }

            total += points;
        }

        return new RewardResponse(customerId, firstMonthPoints, secondMonthPoints, thirdMonthPoints, total);
    }
}
