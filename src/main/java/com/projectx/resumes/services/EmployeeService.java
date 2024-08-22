package com.projectx.resumes.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdAndValueDto;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.PageRequestDto;
import com.projectx.resumes.entities.EmployeeDetails;
import com.projectx.resumes.payloads.*;

import java.util.List;

public interface EmployeeService {
    Boolean createEmployee(EmployeeDto dto)throws ResourceNotFoundException, AlreadyExistsException;
    EmployeeDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    EmployeePageResponseDto getAllEmployees(PageRequestDto dto);
    EmployeeDetails getEmployeeDetails(Long resumeId)throws ResourceNotFoundException;
    Boolean updateStatus(EntityIdDto dto)throws ResourceNotFoundException;
    Boolean insertUpdateEmployeeCompany(EmployeeCompanyIdDto dto)throws ResourceNotFoundException;
    List<ViewEmployeeCompanyDto> getEmployeeCompanies(EntityIdDto dto);
    List<EntityIdAndValueDto> getEmployeeDropDown();
    List<EntityIdAndValueDto> getEmployeeCompanyDropDown(EntityIdDto dto);

}
