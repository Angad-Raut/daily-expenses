package com.projectx.resumes.services;

import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.EntityIdWithPageRequestDto;
import com.projectx.resumes.payloads.SkillsPageResponseDto;
import com.projectx.resumes.payloads.TechnoStackDto;
import com.projectx.resumes.payloads.ViewStackItemDto;

import java.util.List;

public interface TechnoStackService {
    Boolean insertUpdateEmployeeSkills(TechnoStackDto dto)throws ResourceNotFoundException;
    TechnoStackDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    SkillsPageResponseDto getEmployeeSkills(EntityIdWithPageRequestDto dto)throws ResourceNotFoundException;
    List<ViewStackItemDto> getStackItems(EntityIdDto dto);
}
