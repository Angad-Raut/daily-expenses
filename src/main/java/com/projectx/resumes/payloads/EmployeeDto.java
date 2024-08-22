package com.projectx.resumes.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {
    private Long id;
    private String employeeName;
    private Long employeeMobile;
    private String employeeEmail;
    private String linkedInUrl;
    private String gitHubUrl;
    private String profileSummary;
    private String totalExperience;
}
