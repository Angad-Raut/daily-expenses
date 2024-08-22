package com.projectx.resumes.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewEmployeesDto {
    private Integer srNo;
    private Long employeeId;
    private String employeeName;
    private Long employeeMobile;
    private String employeeEmail;
    private String totalExperience;
}
