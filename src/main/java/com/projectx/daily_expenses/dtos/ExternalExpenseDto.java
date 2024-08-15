package com.projectx.daily_expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalExpenseDto {
    private Long id;
    private String personName;
    private String description;
    private Double amount;
    private String amountGivenDate;
}
