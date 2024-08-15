package com.projectx.daily_expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewExternalExpenseDto {
    private Integer srNo;
    private Long id;
    private String personName;
    private String description;
    private String amount;
    private String amountGivenDate;
}
