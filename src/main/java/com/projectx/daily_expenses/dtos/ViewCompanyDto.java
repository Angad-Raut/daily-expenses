package com.projectx.daily_expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewCompanyDto {
    private Integer srNo;
    private Long companyId;
    private String companyName;
    private String startDate;
    private String endDate;
}
