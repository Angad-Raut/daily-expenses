package com.projectx.loans.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewLoansDto {
    private Integer srNo;
    private Long loanId;
    private String bankName;
    private String loanType;
    private String loanAmount;
    private String remainingAmount;
    private String startDate;
    private String endDate;
}
