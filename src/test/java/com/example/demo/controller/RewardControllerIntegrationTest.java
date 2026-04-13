package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RewardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void calculateRewards_validDateRange_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/calculate")
                .param("startDate", "2026-01-01")
                .param("endDate", "2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").exists())
                .andExpect(jsonPath("$[0].totalPoints").exists());
    }

    @Test
    void calculateRewards_noDataInRange_shouldReturn204() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/calculate")
                .param("startDate", "2025-01-01")
                .param("endDate", "2025-03-31"))
                .andExpect(status().isNoContent());
    }

    @Test
    void calculateRewards_missingStartDate_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/calculate")
                .param("endDate", "2026-03-31"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateRewards_missingEndDate_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/calculate")
                .param("startDate", "2026-01-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateRewards_startDateAfterEndDate_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/calculate")
                .param("startDate", "2026-03-31")
                .param("endDate", "2026-01-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateRewards_dateRangeExceeds3Months_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/calculate")
                .param("startDate", "2026-01-01")
                .param("endDate", "2026-05-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateRewards_invalidDateFormat_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/calculate")
                .param("startDate", "01-01-2026")
                .param("endDate", "2026-03-31"))
                .andExpect(status().isBadRequest());
    }
}
