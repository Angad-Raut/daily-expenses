package com.projectx.resumes.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDto {
    private Long id;
    private Long employeeId;
    private Long companyId;
    private String projectName;
    private String projectDescription;
    private String jobTitle;
    private Integer teamSize;
    private String projectDomain;
    private String responsibility;
    private List<String> technologies=new ArrayList<>();
}
