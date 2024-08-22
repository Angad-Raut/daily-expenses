package com.projectx.resumes.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewProjectDto {
    private Integer srNo;
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String companyName;
    private String projectName;
    private Integer teamSize;
    private String jobTitle;
}
