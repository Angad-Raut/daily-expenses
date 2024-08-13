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
    private String monthlyExpenseTotal;
    private String yearlyExpenseTotal;
    private Integer documentCount;
    private Integer incomeCount;
    private String incomeSum;
    private Integer companyCount;
}
