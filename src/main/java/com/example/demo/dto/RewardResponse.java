package com.example.demo.dto;

public class RewardResponse {

    private String customerId;
    private int firstMonthPoints;
    private int secondMonthPoints;
    private int thirdMonthPoints;
    private int total;

    public RewardResponse(String customerId, int firstMonthPoints, int secondMonthPoints, int thirdMonthPoints, int total) {
        this.customerId = customerId;
        this.firstMonthPoints = firstMonthPoints;
        this.secondMonthPoints = secondMonthPoints;
        this.thirdMonthPoints = thirdMonthPoints;
        this.total = total;
    }

    public String getCustomerId() {
        return customerId;
    }
    public int getFirstMonthPoints() {
        return firstMonthPoints;
    }
    public int getSecondMonthPoints() {
        return secondMonthPoints;
    }
    public int getThirdMonthPoints() {
        return thirdMonthPoints;
    }
    public int getTotal() {
        return total;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setFirstMonthPoints(int firstMonthPoints) {
        this.firstMonthPoints = firstMonthPoints;
    }

    public void setSecondMonthPoints(int secondMonthPoints) {
        this.secondMonthPoints = secondMonthPoints;
    }

    public void setThirdMonthPoints(int thirdMonthPoints) {
        this.thirdMonthPoints = thirdMonthPoints;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
