package com.projectx.documents.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.utils.Constants;
import com.projectx.documents.payloads.DocumentDropDownDto;
import com.projectx.documents.payloads.DocumentDto;
import com.projectx.documents.payloads.DownloadDocDto;
import com.projectx.documents.payloads.ViewDocumentsDto;
import com.projectx.documents.entities.DocumentDetails;
import com.projectx.documents.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class DocumentServiceImpl implements DocumentService{

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Boolean addUpdateDocument(DocumentDto dto) throws ResourceNotFoundException,
            AlreadyExistsException, IOException {
         DocumentDetails documentDetails = null;
         if (dto.getId()==null) {
                  isDocumentExist(dto.getDocumentName());
                  documentDetails = DocumentDetails.builder()
                          .documentName(dto.getDocumentName())
                          .documentType(dto.getDocumentType())
                          .documentImage(dto.getDocumentFile().getBytes())
                          .documentStatus(true)
                          .insertedTime(new Date())
                          .build();
         } else {
             documentDetails = documentRepository.getById(dto.getId());
             if (documentDetails == null) {
                 throw new ResourceNotFoundException(Constants.DOCUMENT_NOT_FOUND);
             }
             if (!dto.getDocumentName().equals(documentDetails.getDocumentName())) {
                 isDocumentExist(dto.getDocumentName());
                 documentDetails.setDocumentName(dto.getDocumentName());
             }
             if (!dto.getDocumentType().equals(documentDetails.getDocumentType())) {
                 documentDetails.setDocumentType(dto.getDocumentType());
             }
             if (!dto.getDocumentFile().getBytes().equals(documentDetails.getDocumentImage())) {
                 documentDetails.setDocumentImage(dto.getDocumentFile().getBytes());
             }
         }
         try {
            return documentRepository.save(documentDetails)!=null?true:false;
         } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
         } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
         }
    }

    @Override
    public List<ViewDocumentsDto> getAllDocuments() {
        List<DocumentDetails> fetchList = documentRepository.findAll();
        AtomicInteger index = new AtomicInteger(0);
        return fetchList!=null && !fetchList.isEmpty()?fetchList.stream()
                .map(data -> ViewDocumentsDto.builder()
                        .srNo(index.incrementAndGet())
                        .documentId(data.getId())
                        .documentName(data.getDocumentName())
                        .documentType(data.getDocumentType())
                        .documentFile(data.getDocumentImage())
                        .uploadedDate(Constants.toExpenseDate(data.getInsertedTime()))
                        .build())
                .collect(Collectors.toList()):new ArrayList<>();
    }

    @Override
    public List<DocumentDropDownDto> getDocumentDropDown() {
        List<DocumentDropDownDto> response = new ArrayList<>();
        response.add(new DocumentDropDownDto("PERSONAL","Personal"));
        response.add(new DocumentDropDownDto("EDUCATIONAL","Educational"));
        response.add(new DocumentDropDownDto("PROFESSIONAL","Professional"));
        return response;
    }

    @Override
    public byte[] getDocument(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            byte[] data = documentRepository.getDocumentFile(dto.getEntityId());
            if (data==null) {
                throw new ResourceNotFoundException(Constants.DOCUMENT_NOT_FOUND);
            }
            return data;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public DownloadDocDto downloadDocument(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            DocumentDetails data = documentRepository.getById(dto.getEntityId());
            if (data==null) {
                throw new ResourceNotFoundException(Constants.DOCUMENT_NOT_FOUND);
            }
            return DownloadDocDto.builder()
                    .documentName(data.getDocumentName())
                    .documentFile(data.getDocumentImage())
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public Integer documentCount() {
        Integer count = documentRepository.documentCount();
        return count!=null?count:0;
    }

    private void isDocumentExist(String document) {
        if (documentRepository.existsDocumentDetailsByDocumentName(document)) {
            throw new AlreadyExistsException(Constants.DOCUMENT_ALREADY_EXIST);
        }
    }
}
