package com.projectx.documents.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.documents.payloads.DocumentDropDownDto;
import com.projectx.documents.payloads.DocumentDto;
import com.projectx.documents.payloads.DownloadDocDto;
import com.projectx.documents.payloads.ViewDocumentsDto;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    Boolean addUpdateDocument(DocumentDto dto)throws ResourceNotFoundException,
            AlreadyExistsException, IOException;
    List<ViewDocumentsDto> getAllDocuments();
    List<DocumentDropDownDto> getDocumentDropDown();
    byte[] getDocument(EntityIdDto dto)throws ResourceNotFoundException;
    Integer documentCount();
    DownloadDocDto downloadDocument(EntityIdDto dto)throws ResourceNotFoundException;
}
