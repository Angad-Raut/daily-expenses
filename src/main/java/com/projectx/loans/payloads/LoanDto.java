package com.projectx.loans.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanDto {
    private Long id;
    private String bankName;
    private Integer loanType;
    private Double loanAmount;
    private String startDate;
    private String endDate;
}
