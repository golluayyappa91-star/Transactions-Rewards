package com.example.demo.dto;

public class MonthlyReward {
    private int year;
    private int month;
    private int points;

    public MonthlyReward(int year, int month, int points) {
        this.year = year;
        this.month = month;
        this.points = points;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
