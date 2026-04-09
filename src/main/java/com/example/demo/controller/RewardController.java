package com.example.demo.controller;

import com.example.demo.dto.RewardResponse;
import com.example.demo.service.RewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-v2/rewards")
public class RewardController {

    @Autowired
    RewardService service;

    private static final Logger log = LoggerFactory.getLogger(RewardController.class);

    @GetMapping("/{customerId}")
    public ResponseEntity<RewardResponse> getRewards(@PathVariable String customerId) {
        log.info("Processing rewards for customer: {}", customerId);
        log.info("Invoking the RewardService Method");
        RewardResponse response = service.getRewards(customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
