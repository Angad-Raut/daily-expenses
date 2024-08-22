package com.projectx.resumes.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewEmployeeCompanyDto {
    private Integer srNo;
    private Long companyId;
    private String companyName;
    private String companyAddress;
    private String startDate;
    private String endDate;
    private String experience;
}
