package com.projectx.documents.services;

import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.EntityIdWithPageRequestDto;
import com.projectx.documents.payloads.CompanyDocDto;
import com.projectx.documents.payloads.CompanyDocumentPageResponseDto;
import com.projectx.documents.payloads.FileDownloadDto;

import java.io.IOException;

public interface CompanyDocumentService {
    Boolean addDocumentByCompanyId(CompanyDocDto dto)
            throws ResourceNotFoundException, IOException;
    CompanyDocumentPageResponseDto getCompanyAllDocuments(EntityIdWithPageRequestDto dto);
    Boolean deleteDocument(EntityIdDto dto)throws ResourceNotFoundException;
    FileDownloadDto downloadFile(EntityIdDto dto)throws ResourceNotFoundException;
}
