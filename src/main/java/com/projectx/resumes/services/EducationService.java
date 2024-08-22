package com.projectx.resumes.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.EntityIdWithPageRequestDto;
import com.projectx.resumes.payloads.EducationDto;
import com.projectx.resumes.payloads.EducationPageResponseDto;

import java.text.ParseException;

public interface EducationService {
    Boolean insertUpdate(EducationDto dto) throws ResourceNotFoundException, AlreadyExistsException, ParseException;
    EducationDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    EducationPageResponseDto getEmployeeEducations(EntityIdWithPageRequestDto dto)throws ResourceNotFoundException;
}
