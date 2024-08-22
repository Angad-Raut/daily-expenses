package com.projectx.resumes.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationDto {
    private Long id;
    private Long employeeId;
    private String degreeName;
    private String universityName;
    private String startDate;
    private String endDate;
    private String percentage;
}
