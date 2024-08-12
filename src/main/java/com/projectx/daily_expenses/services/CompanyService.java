package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.*;
import com.projectx.daily_expenses.dtos.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface CompanyService {
    Boolean addUpdate(CompanyDto dto) throws ResourceNotFoundException,
            AlreadyExistsException, InvalidDataException, IOException, ParseException;
    EditCompanyDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    List<EntityNameAndValueDto> getCompanyDocumentTypeDropDown();
    CompanyPageResponseDto getAllCompanies(PageRequestDto dto);
    Boolean addDocumentByCompanyId(CompanyDocDto dto)throws ResourceNotFoundException,IOException;
    Boolean updateStatus(EntityIdDto dto)throws ResourceNotFoundException;
    CompanyDocumentPageResponseDto getCompanyAllDocuments(EntityIdWithPageRequestDto dto)throws ResourceNotFoundException;
    List<EntityIdAndValueDto> getCompanyDropDown();
}
