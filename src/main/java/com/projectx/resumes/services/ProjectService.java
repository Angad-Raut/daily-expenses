package com.projectx.resumes.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.EntityIdWithPageRequestDto;
import com.projectx.resumes.payloads.ProjectDto;
import com.projectx.resumes.payloads.ProjectPageResponseDto;
import com.projectx.resumes.payloads.TechDto;
import com.projectx.resumes.payloads.TechnologyListDto;

import java.util.List;

public interface ProjectService {
    Boolean insertUpdate(ProjectDto dto)throws ResourceNotFoundException, AlreadyExistsException;
    ProjectDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    ProjectPageResponseDto getEmployeeProjects(EntityIdWithPageRequestDto dto);
    List<TechDto> getProjectTechnologies(EntityIdDto dto);
    Boolean addUpdateProjectTechnologies(TechnologyListDto dto);
}
