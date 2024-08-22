package com.projectx.resumes.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TechnoStackDto {
    private Long id;
    private Long employeeId;
    private String stackName;
    private Set<SkillDto> skillList = new HashSet<>();
}
