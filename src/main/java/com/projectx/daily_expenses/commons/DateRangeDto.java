package com.projectx.daily_expenses.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateRangeDto {
    private String startDate;
    private String endDate;
}
