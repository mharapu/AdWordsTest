package com.example.adwords.service;

import com.example.adwords.model.Budget;
import com.example.adwords.model.DailyCosts;
import com.example.adwords.model.GeneratedCost;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CostsGenerator {

    @PostMapping(path = "/generateCosts")
    public List<DailyCosts> getGeneratedCostsHistory(@RequestBody List<Budget> budgetList, @RequestHeader(required = false) Integer months) {
        System.out.println("Date | Budget | Costs");
        List<DailyCosts> dailyCosts = new ArrayList<>();

        if (budgetList != null && !budgetList.isEmpty()) {
            // get the first and last day of setting budget.
            // if last budget change has a null end date we'll use the end of start date month
            LocalDate day = budgetList.get(0).getStartDate().toLocalDate();
            Budget lastBudget = budgetList.get(budgetList.size() - 1);
            LocalDate endPeriod = months != null ? day.withDayOfMonth(1).plusMonths(months) :
                    lastBudget.getEndDate() != null ? lastBudget.getEndDate().toLocalDate()
                    : lastBudget.getStartDate().toLocalDate().withDayOfMonth(1).plusMonths(1);
            if (lastBudget.getEndDate() == null) {
                lastBudget.setEndDate(endPeriod.atStartOfDay());
            }
            int currMonth = day.getMonthValue();
            double totalMonthBudget = 0;
            double totalMonthCosts = 0;
            // parse the period
            while (day.isBefore(endPeriod)) {
                DailyCosts costs = generateDailyCosts(budgetList, day, totalMonthBudget, totalMonthCosts);
                dailyCosts.add(costs);
                totalMonthBudget = totalMonthBudget + costs.getMaxBudget();
                totalMonthCosts = totalMonthCosts + costs.getCosts().stream().mapToDouble(GeneratedCost::getAmount).sum();
                System.out.println(String.format("%s | %.2f | %.2f", day.format(DateTimeFormatter.ofPattern("MM.dd.yyyy")), costs.getMaxBudget(), costs.getTotalCosts()));
                day = day.plusDays(1);
                if (day.getMonthValue() != currMonth) {
                    // reset month budget and costs when the month ends
                    totalMonthBudget = costs.getMaxBudget();
                    totalMonthCosts = 0;
                    currMonth = day.getMonthValue();
                }
            }
        }
        System.out.println("Total days " + dailyCosts.size());
        return dailyCosts;
    }

    private DailyCosts generateDailyCosts(List<Budget> budgetList, LocalDate day, double totalMonthBudget, double totalMonthCosts) {
        // create a new daily cost for passed day
        DailyCosts dailyCosts = new DailyCosts();
        dailyCosts.setDate(day);
        dailyCosts.setTotalCosts(0d);
        dailyCosts.setCosts(new ArrayList<>());

        // search for budget changes during passed day and get the max value
        List<Budget> budgetChanges = budgetList.stream()
                .filter(budget -> budget.getStartDate().isAfter(day.atStartOfDay())
                        && budget.getStartDate().isBefore(day.plusDays(1).atStartOfDay().minusSeconds(1))).collect(Collectors.toList());
        if (!budgetChanges.isEmpty()) {
            double maxDailyBudget = budgetChanges.stream().mapToDouble(Budget::getAmount).max().getAsDouble();
            totalMonthBudget = totalMonthBudget + maxDailyBudget;
            dailyCosts.setMaxBudget(maxDailyBudget);
        }

        // random generate number of costs generation for this day between 1 and 10
        int generateTimesPerDay = (int) (Math.random() * 10) + 1;

        for (int i = 1; i <= generateTimesPerDay; i++) {
            // generate a random time for passed day based on generations number
            LocalDateTime dateTime = day.atTime(i * ( 24 / generateTimesPerDay ) - i, (int) (Math.random() * 60));
            // find the allocated budget for current time
            Budget currentBudget = budgetList.stream()
                    .filter(budget -> dateTime.isAfter(budget.getStartDate()) && dateTime.isBefore(budget.getEndDate().minusSeconds(1)))
                    .findFirst().orElse(null);
            if (currentBudget == null || currentBudget.getAmount() == 0) {
                continue;
            }

            // if we have a new budget higher than the max budget we increase the daily max budget
            // and the total month budget with the difference
            if (dailyCosts.getMaxBudget() == null) {
                totalMonthBudget = totalMonthBudget + currentBudget.getAmount();
                dailyCosts.setMaxBudget(currentBudget.getAmount());
            }

            // generate a new cost based on current max budget, available month budget and total month cost
            GeneratedCost generatedCost = new GeneratedCost();
            generatedCost.setDate(dateTime);
            double maxCost = Math.min(totalMonthBudget - totalMonthCosts, dailyCosts.getMaxBudget() * 2 - dailyCosts.getTotalCosts());
            double cost = Math.random() * maxCost;
            generatedCost.setAmount(cost);
            dailyCosts.getCosts().add(generatedCost);
            dailyCosts.setTotalCosts(dailyCosts.getTotalCosts() + cost);
            totalMonthCosts = totalMonthCosts + cost;
        }
        if (dailyCosts.getMaxBudget() == null) {
            dailyCosts.setMaxBudget(0d);
        }
        return dailyCosts;
    }
}
