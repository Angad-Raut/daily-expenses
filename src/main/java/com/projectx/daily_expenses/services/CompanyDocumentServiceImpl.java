package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.Constants;
import com.projectx.daily_expenses.commons.EntityIdDto;
import com.projectx.daily_expenses.commons.EntityIdWithPageRequestDto;
import com.projectx.daily_expenses.commons.ResourceNotFoundException;
import com.projectx.daily_expenses.dtos.CompanyDocDto;
import com.projectx.daily_expenses.dtos.CompanyDocumentPageResponseDto;
import com.projectx.daily_expenses.dtos.ViewCompanyDocumentDto;
import com.projectx.daily_expenses.entities.CompanyDetails;
import com.projectx.daily_expenses.entities.CompanyDocuments;
import com.projectx.daily_expenses.repositories.CompanyDocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CompanyDocumentServiceImpl implements CompanyDocumentService {

    @Autowired
    private CompanyDocumentRepository companyDocumentRepository;

    @Autowired
    private CompanyService companyService;

    @Transactional
    @Override
    public Boolean addDocumentByCompanyId(CompanyDocDto dto)
            throws ResourceNotFoundException, IOException {
        try {
            CompanyDetails details = companyService.getCompanyDetailsById(dto.getCompanyId());
            if (details==null) {
                throw new ResourceNotFoundException(Constants.COMPANY_DETAILS_NOT_EXISTS);
            }
            CompanyDocuments companyDocuments = CompanyDocuments.builder()
                    .documentType(dto.getDocumentType())
                    .uploadedDate(new Date())
                    .documentFile(dto.getDocumentFile().getBytes())
                    .contentType(dto.getDocumentFile().getContentType())
                    .companyDetails(details)
                    .build();
            return companyDocumentRepository.save(companyDocuments)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public CompanyDocumentPageResponseDto getCompanyAllDocuments(EntityIdWithPageRequestDto dto) {
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("documentType")) {
            sortParameter = "document_type";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("uploadedDate")) {
            sortParameter = "uploaded_date";
        } else {
            sortParameter = "uploaded_date";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<CompanyDocuments> documents = null;
        if (dto.getEntityId()!=null) {
            documents = companyDocumentRepository.getSingleCompanyDocuments(dto.getEntityId(), pageable);
        } else {
            documents = companyDocumentRepository.getAllCompanyDocuments(pageable);
        }
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<CompanyDocuments> listOfDocuments = documents.getContent();
        List<ViewCompanyDocumentDto> documentList = !listOfDocuments.isEmpty()?listOfDocuments.stream()
                .map(data -> ViewCompanyDocumentDto.builder()
                        .srNo(index.incrementAndGet())
                        .companyName(data.getCompanyDetails()!=null?data.getCompanyDetails().getCompanyName():Constants.DASH)
                        .documentType(data.getDocumentType()!=null?data.getDocumentType():Constants.DASH)
                        .uploadedDate(data.getUploadedDate()!=null?Constants.toExpenseDate(data.getUploadedDate()):Constants.DASH)
                        .documentFile(data.getDocumentFile()!=null?data.getDocumentFile():null)
                        .build()).toList()
                :new ArrayList<>();
        return !documentList.isEmpty()?CompanyDocumentPageResponseDto.builder()
                .pageNo(documents.getNumber())
                .pageSize(documents.getSize())
                .totalPages(documents.getTotalPages())
                .totalElements(documents.getTotalElements())
                .content(documentList)
                .build():new CompanyDocumentPageResponseDto();
    }

    @Override
    public Boolean deleteDocument(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            CompanyDocuments documents = companyDocumentRepository.getById(dto.getEntityId());
            if (documents==null) {
                throw new ResourceNotFoundException(Constants.COMPANY_DOCUMENT_NOT_EXISTS);
            }
            companyDocumentRepository.delete(documents);
            return true;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
