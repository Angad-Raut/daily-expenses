package com.projectx.resumes.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewEmployeeSkillsDto {
    private Integer srNo;
    private Long stackId;
    private String stackName;
    private String employeeName;
    private String updatedDate;
}
