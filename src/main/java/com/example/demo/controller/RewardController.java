package com.example.demo.controller;

import com.example.demo.dto.CustomerReward;
import com.example.demo.service.RewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rewards")
public class RewardController {

    private static final Logger log = LoggerFactory.getLogger(RewardController.class);
    private final RewardService service;

    public RewardController(RewardService service) {
        this.service = service;
    }

    @GetMapping("/calculate")
    public ResponseEntity<List<CustomerReward>> calculateRewards(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        log.info("Calculating rewards for date range: {} to {}", startDate, endDate);
        List<CustomerReward> rewards = service.calculateRewards(startDate, endDate);
        if (rewards.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }
}
