package com.projectx.companies.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.InvalidDataException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdAndValueDto;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.EntityNameAndValueDto;
import com.projectx.common.payloads.PageRequestDto;
import com.projectx.companies.payloads.CompanyDto;
import com.projectx.companies.payloads.CompanyPageResponseDto;
import com.projectx.companies.payloads.EditCompanyDto;
import com.projectx.companies.entities.CompanyDetails;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface CompanyService {
    Boolean addUpdate(CompanyDto dto) throws ResourceNotFoundException,
            AlreadyExistsException, InvalidDataException, IOException, ParseException;
    EditCompanyDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    List<EntityNameAndValueDto> getCompanyDocumentTypeDropDown();
    CompanyPageResponseDto getAllCompanies(PageRequestDto dto);
    Boolean updateStatus(EntityIdDto dto)throws ResourceNotFoundException;
    List<EntityIdAndValueDto> getCompanyDropDown();
    Integer getCompanyCount();
    CompanyDetails getCompanyDetailsById(Long companyId);
    String getCompanyName(Long companyId);
}
