package com.projectx.daily_expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditCompanyDto {
    private Long id;
    private String companyName;
    private String companyAddress;
    private byte[] companyLogo;
    private String startDate;
    private String endDate;
}
