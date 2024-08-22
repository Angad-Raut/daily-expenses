package com.projectx.resumes.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewEducationDto {
    private Integer srNo;
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String degreeName;
    private String universityName;
    private String startDate;
    private String endDate;
    private String percentage;
}
