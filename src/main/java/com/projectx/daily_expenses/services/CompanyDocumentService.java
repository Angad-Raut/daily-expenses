package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.EntityIdDto;
import com.projectx.daily_expenses.commons.EntityIdWithPageRequestDto;
import com.projectx.daily_expenses.commons.ResourceNotFoundException;
import com.projectx.daily_expenses.dtos.CompanyDocDto;
import com.projectx.daily_expenses.dtos.CompanyDocumentPageResponseDto;
import com.projectx.daily_expenses.dtos.FileDownloadDto;

import java.io.IOException;

public interface CompanyDocumentService {
    Boolean addDocumentByCompanyId(CompanyDocDto dto)
            throws ResourceNotFoundException, IOException;
    CompanyDocumentPageResponseDto getCompanyAllDocuments(EntityIdWithPageRequestDto dto);
    Boolean deleteDocument(EntityIdDto dto)throws ResourceNotFoundException;
    FileDownloadDto downloadFile(EntityIdDto dto)throws ResourceNotFoundException;
}
