package com.example.adwords.model;

import java.time.LocalDate;
import java.util.List;

public class DailyCosts {

    private LocalDate date;
    private Double maxBudget;
    private Double totalCosts;
    private List<GeneratedCost> costs;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(Double maxBudget) {
        this.maxBudget = maxBudget;
    }

    public Double getTotalCosts() {
        return totalCosts;
    }

    public void setTotalCosts(Double totalCosts) {
        this.totalCosts = totalCosts;
    }

    public List<GeneratedCost> getCosts() {
        return costs;
    }

    public void setCosts(List<GeneratedCost> costs) {
        this.costs = costs;
    }
}
