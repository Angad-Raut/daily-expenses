package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.*;
import com.projectx.daily_expenses.dtos.*;
import com.projectx.daily_expenses.entities.CompanyDetails;
import com.projectx.daily_expenses.entities.CompanyDocuments;
import com.projectx.daily_expenses.entities.ExpensesDetails;
import com.projectx.daily_expenses.repositories.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    @Override
    public Boolean addUpdate(CompanyDto dto) throws ResourceNotFoundException, AlreadyExistsException, InvalidDataException, IOException, ParseException {
        CompanyDetails companyDetails = null;
        if (dto.getId()==null) {
              isCompanyExists(dto.getCompanyName());
            companyDetails = CompanyDetails.builder()
                    .companyName(dto.getCompanyName())
                    .companyAddress(dto.getCompanyAddress())
                    .companyLogo(dto.getCompanyLogo().getBytes())
                    .status(true)
                    .startDate(Constants.getISODate(dto.getStartDate()))
                    .endDate(dto.getEndDate()!=null?Constants.getISODate(dto.getEndDate()):null)
                    .build();
        } else {
            companyDetails = companyRepository.getById(dto.getId());
            if (!dto.getCompanyName().equals(companyDetails.getCompanyName())) {
                isCompanyExists(dto.getCompanyName());
                companyDetails.setCompanyName(dto.getCompanyName());
            }
            if (!dto.getCompanyAddress().equals(companyDetails.getCompanyAddress())) {
                companyDetails.setCompanyAddress(dto.getCompanyAddress());
            }
            if (dto.getCompanyLogo()!=null) {
                if (!dto.getCompanyLogo().getBytes().equals(companyDetails.getCompanyLogo())) {
                    companyDetails.setCompanyLogo(dto.getCompanyLogo().getBytes());
                }
            }
            if (dto.getStartDate()!=null && companyDetails.getStartDate()!=null) {
                if (!dto.getStartDate().equals(Constants.toExpenseDate(companyDetails.getStartDate()))) {
                    companyDetails.setStartDate(Constants.getISODate(dto.getStartDate()));
                }
            }
            if (dto.getEndDate()!=null && companyDetails.getEndDate()!=null) {
                if (!dto.getEndDate().equals(Constants.toExpenseDate(companyDetails.getEndDate()))) {
                    companyDetails.setEndDate(Constants.getISODate(dto.getEndDate()));
                }
            } else if (dto.getEndDate()!=null && companyDetails.getEndDate()==null) {
                companyDetails.setEndDate(Constants.getISODate(dto.getEndDate()));
            } else if (dto.getEndDate()==null && companyDetails.getEndDate()!=null) {
                companyDetails.setEndDate(null);
            }
        }
        try {
            return companyRepository.save(companyDetails)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        } catch (RuntimeException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public EditCompanyDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            CompanyDetails companyDetails = companyRepository.getById(dto.getEntityId());
            if (companyDetails==null) {
                throw new ResourceNotFoundException(Constants.COMPANY_DETAILS_NOT_EXISTS);
            }
            return EditCompanyDto.builder()
                    .id(companyDetails.getId())
                    .companyName(companyDetails.getCompanyName())
                    .companyAddress(companyDetails.getCompanyAddress())
                    .companyLogo(companyDetails.getCompanyLogo())
                    .startDate(Constants.toExpenseDate(companyDetails.getStartDate()))
                    .endDate(companyDetails.getEndDate()!=null?Constants.toExpenseDate(companyDetails.getEndDate()):Constants.DASH)
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<EntityNameAndValueDto> getCompanyDocumentTypeDropDown() {
        List<EntityNameAndValueDto> responseList = new ArrayList<>();
        responseList.add(new EntityNameAndValueDto(Constants.SALARY_TYPE,"Salary Slip"));
        responseList.add(new EntityNameAndValueDto(Constants.FORM_16_TYPE,"Form 16"));
        responseList.add(new EntityNameAndValueDto(Constants.OFFER_LETTER_TYPE,"Offer Letter"));
        responseList.add(new EntityNameAndValueDto(Constants.EXPERIENCE_LETTER_TYPE,"Experience Letter"));
        responseList.add(new EntityNameAndValueDto(Constants.SERVICE_LETTER_TYPE,"Service Letter"));
        responseList.add(new EntityNameAndValueDto(Constants.APPOINTMENT_LETTER_TYPE,"Appointment Letter"));
        return responseList;
    }

    @Override
    public CompanyPageResponseDto getAllCompanies(PageRequestDto dto) {
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("companyName")) {
            sortParameter = "company_name";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("startDate")) {
            sortParameter = "start_date";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("endDate")){
            sortParameter = "end_date";
        } else {
            sortParameter = "id";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<CompanyDetails> companies = companyRepository.getAllCompaniesPages(pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<CompanyDetails> listOfCompanies = companies.getContent();
        List<ViewCompanyDto> companiesList = !listOfCompanies.isEmpty()?listOfCompanies.stream()
                .map(data -> ViewCompanyDto.builder()
                        .srNo(index.incrementAndGet())
                        .companyId(data.getId())
                        .companyName(data.getCompanyName()!=null?data.getCompanyName():Constants.DASH)
                        .startDate(data.getStartDate()!=null?Constants.toExpenseDate(data.getStartDate()):Constants.DASH)
                        .endDate(data.getEndDate()!=null?Constants.toExpenseDate(data.getEndDate()):Constants.DASH)
                        .build()).toList()
                :new ArrayList<>();
        return !companiesList.isEmpty()?CompanyPageResponseDto.builder()
                .pageNo(companies.getNumber())
                .pageSize(companies.getSize())
                .totalPages(companies.getTotalPages())
                .totalElements(companies.getTotalElements())
                .content(companiesList)
                .build():new CompanyPageResponseDto();
    }

    @Transactional
    @Override
    public Boolean addDocumentByCompanyId(CompanyDocDto dto) throws ResourceNotFoundException {
        try {
            CompanyDetails details = companyRepository.getById(dto.getCompanyId());
            if (details==null) {
                throw new ResourceNotFoundException(Constants.COMPANY_DETAILS_NOT_EXISTS);
            }
            if (details.getCompanyDocuments()!=null && !details.getCompanyDocuments().isEmpty()){
                List<CompanyDocuments> documents = details.getCompanyDocuments();
                documents.add(new CompanyDocuments(dto.getDocumentType(),new Date(),details.getId(),dto.getDocumentFile().getBytes()));
                details.setCompanyDocuments(documents);
            } else {
                List<CompanyDocuments> documents = new ArrayList<>();
                documents.add(new CompanyDocuments(dto.getDocumentType(),new Date(),details.getId(),dto.getDocumentFile().getBytes()));
                details.setCompanyDocuments(documents);
            }
            CompanyDetails companyDetails = companyRepository.save(details);
            return companyDetails.getCompanyDocuments()!=null && !companyDetails.getCompanyDocuments().isEmpty()?true:false;
        } catch (ResourceNotFoundException | IOException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public Boolean updateStatus(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            CompanyDetails companyDetails = companyRepository.getById(dto.getEntityId());
            if (companyDetails==null) {
                throw new ResourceNotFoundException(Constants.COMPANY_DETAILS_NOT_EXISTS);
            }
            Boolean status = companyDetails.getStatus()?false:true;
            Integer count = companyRepository.updateStatus(dto.getEntityId(),status);
            return count==1?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public CompanyDocumentPageResponseDto getCompanyAllDocuments(EntityIdWithPageRequestDto dto) throws ResourceNotFoundException {
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "company_id";
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
              documents = companyRepository.getCompanyDocumentsPage(dto.getEntityId(), pageable);
        } else {
              documents = companyRepository.getAllCompanyDocumentsPage(pageable);
        }
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<CompanyDocuments> listOfDocuments = documents.getContent();
        List<ViewCompanyDocumentDto> documentList = !listOfDocuments.isEmpty()?listOfDocuments.stream()
                .map(data -> ViewCompanyDocumentDto.builder()
                        .srNo(index.incrementAndGet())
                        .companyName(data.getCompanyId()!=null?companyRepository.getCompanyName(data.getCompanyId()):Constants.DASH)
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
    public List<EntityIdAndValueDto> getCompanyDropDown() {
        List<Object[]> fetchList = companyRepository.getCompanyDropDown();
        return fetchList!=null && !fetchList.isEmpty()?fetchList.stream()
                .map(data -> EntityIdAndValueDto.builder()
                        .entityId(data[0]!=null?Long.parseLong(data[0].toString()):null)
                        .entityValue(data[1]!=null?data[1].toString():null)
                        .build())
                .toList():new ArrayList<>();
    }

    @Override
    public Integer getCompanyCount() {
        Integer count = companyRepository.getCompanyCount();
        return count!=null?count:0;
    }

    private void isCompanyExists(String companyName) {
        if (companyRepository.existsCompanyDetailsByCompanyName(companyName)) {
            throw new AlreadyExistsException(Constants.COMPANY_DETAILS_ALREADY_EXISTS);
        }
    }
}
