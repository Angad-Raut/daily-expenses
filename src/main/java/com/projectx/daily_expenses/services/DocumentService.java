package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.AlreadyExistsException;
import com.projectx.daily_expenses.commons.EntityIdDto;
import com.projectx.daily_expenses.commons.ResourceNotFoundException;
import com.projectx.daily_expenses.dtos.DocumentDropDownDto;
import com.projectx.daily_expenses.dtos.DocumentDto;
import com.projectx.daily_expenses.dtos.DownloadDocDto;
import com.projectx.daily_expenses.dtos.ViewDocumentsDto;

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
