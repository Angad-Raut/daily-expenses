package com.projectx.loans.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewEMIsDto {
    private Integer srNo;
    private Long emiId;
    private String emiAmount;
    private String emiDate;
    private String paymentMode;
}
