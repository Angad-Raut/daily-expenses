package com.projectx.loans.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EMIDto {
    private Long loanId;
    private Long emiId;
    private Double emiAmount;
    private String paymentMode;
    private String emiDate;
}
