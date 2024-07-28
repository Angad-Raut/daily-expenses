package com.projectx.daily_expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardCountDto {
    private Integer monthlyExpenseCount;
    private Integer allExpenseCount;
    private Double monthlyExpenseTotal;
    private Double yearlyExpenseTotal;
    private Integer documentCount;
}
