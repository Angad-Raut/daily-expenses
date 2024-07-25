package com.projectx.daily_expenses.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyPageRequestDto {
    private Integer pageNumber;
    private Integer pageSize;
    private String sortParam;
    private String sortDir;
    private String monthName;
}
