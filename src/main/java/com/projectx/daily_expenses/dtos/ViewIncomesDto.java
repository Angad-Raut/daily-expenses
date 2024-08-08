package com.projectx.daily_expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewIncomesDto {
    private Integer srNo;
    private Long incomeId;
    private String incomeType;
    private String incomeDate;
    private String incomeAmount;
}
