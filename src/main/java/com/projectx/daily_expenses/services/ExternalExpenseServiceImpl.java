package com.projectx.daily_expenses.services;

import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.PageRequestDto;
import com.projectx.common.utils.Constants;
import com.projectx.daily_expenses.dtos.*;
import com.projectx.daily_expenses.entities.ExternalExpenseDetails;
import com.projectx.daily_expenses.repositories.ExternalExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ExternalExpenseServiceImpl implements ExternalExpenseService {

    @Autowired
    private ExternalExpenseRepository externalExpenseRepository;

    @Override
    public Boolean addUpdate(ExternalExpenseDto dto) throws ResourceNotFoundException, ParseException {
        ExternalExpenseDetails externalExpenseDetails = null;
        if (dto.getId()==null) {
             externalExpenseDetails = ExternalExpenseDetails.builder()
                     .amount(dto.getAmount())
                     .description(dto.getDescription())
                     .personName(dto.getPersonName()!=null?dto.getPersonName():null)
                     .amountGivenDate(dto.getAmountGivenDate()!=null?Constants.getISODate(dto.getAmountGivenDate()):null)
                     .status(true)
                     .insertedDate(new Date())
                     .build();
        } else {
            externalExpenseDetails = externalExpenseRepository.getById(dto.getId());
            if (externalExpenseDetails==null) {
                throw new ResourceNotFoundException(Constants.EXTERNAL_EXPENSE_NOT_EXISTS);
            }
            if (!dto.getDescription().equals(externalExpenseDetails.getDescription())) {
                externalExpenseDetails.setDescription(dto.getDescription());
            }
            if (dto.getPersonName()!=null && externalExpenseDetails.getPersonName()!=null){
                if (!dto.getPersonName().equals(externalExpenseDetails.getPersonName())){
                    externalExpenseDetails.setPersonName(dto.getPersonName());
                }
            } else if (dto.getPersonName()!=null && externalExpenseDetails.getPersonName()==null) {
                externalExpenseDetails.setPersonName(dto.getPersonName());
            } else if (dto.getPersonName()==null && externalExpenseDetails.getPersonName()!=null) {
                externalExpenseDetails.setPersonName(null);
            }
            if (!dto.getAmount().equals(externalExpenseDetails.getAmount())) {
                externalExpenseDetails.setAmount(dto.getAmount());
            }
            if (dto.getAmountGivenDate()!=null && externalExpenseDetails.getAmountGivenDate()!=null){
                if (!dto.getAmountGivenDate().equals(Constants.toExpenseDate(externalExpenseDetails.getAmountGivenDate()))) {
                    externalExpenseDetails.setAmountGivenDate(Constants.getISODate(dto.getAmountGivenDate()));
                }
            } else if (dto.getAmountGivenDate()==null && externalExpenseDetails.getAmountGivenDate()!=null) {
                externalExpenseDetails.setAmountGivenDate(null);
            } else if (dto.getAmountGivenDate()!=null && externalExpenseDetails.getAmountGivenDate()==null) {
                externalExpenseDetails.setAmountGivenDate(Constants.getISODate(dto.getAmountGivenDate()));
            }
        }
        try {
            return externalExpenseRepository.save(externalExpenseDetails)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public ExternalExpenseDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try{
            ExternalExpenseDetails details = externalExpenseRepository.getById(dto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(Constants.EXTERNAL_EXPENSE_NOT_EXISTS);
            }
            return ExternalExpenseDto.builder()
                    .id(details.getId()!=null?details.getId():null)
                    .amount(details.getAmount()!=null?details.getAmount():null)
                    .description(details.getDescription()!=null?details.getDescription():null)
                    .personName(details.getPersonName()!=null?details.getPersonName():null)
                    .amountGivenDate(details.getAmountGivenDate()!=null?Constants.toExpenseDate(details.getAmountGivenDate()):null)
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public ExternalExpensePageResponseDto getAllExternalExpensesPages(PageRequestDto dto) {
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("personName")) {
            sortParameter = "inserted_date";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("description")) {
            sortParameter = "description";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("amount")) {
            sortParameter = "amount";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("amountGivenDate")) {
            sortParameter = "amount_given_date";
        } else {
            sortParameter = "inserted_date";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<ExternalExpenseDetails> externalExpenseDetails = externalExpenseRepository.getAllExternalExpenses(true,pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<ExternalExpenseDetails> listOfExternalExpenses = externalExpenseDetails.getContent();
        List<ViewExternalExpenseDto> documentList = !listOfExternalExpenses.isEmpty()?listOfExternalExpenses.stream()
                .map(data -> ViewExternalExpenseDto.builder()
                        .srNo(index.incrementAndGet())
                        .id(data.getId()!=null?data.getId():null)
                        .personName(data.getPersonName()!=null?data.getPersonName():Constants.DASH)
                        .description(data.getDescription()!=null?data.getDescription():Constants.DASH)
                        .amount(data.getAmount()!=null?Constants.toINRFormat(data.getAmount()):Constants.DASH)
                        .amountGivenDate(data.getAmountGivenDate()!=null?Constants.toExpenseDate(data.getAmountGivenDate()):Constants.DASH)
                        .build()).collect(Collectors.toList())
                :new ArrayList<>();
        return !documentList.isEmpty()? ExternalExpensePageResponseDto.builder()
                .pageNo(externalExpenseDetails.getNumber())
                .pageSize(externalExpenseDetails.getSize())
                .totalPages(externalExpenseDetails.getTotalPages())
                .totalElements(externalExpenseDetails.getTotalElements())
                .content(documentList)
                .build():new ExternalExpensePageResponseDto();
    }

    @Override
    public Boolean updateStatus(EntityIdDto dto) throws ResourceNotFoundException {
        try{
            ExternalExpenseDetails details = externalExpenseRepository.getById(dto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(Constants.EXTERNAL_EXPENSE_NOT_EXISTS);
            }
            Boolean status = details.getStatus()?false:true;
            Integer count = externalExpenseRepository.updateStatus(details.getId(),status);
            return count==1?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public Boolean deleteById(EntityIdDto dto) throws ResourceNotFoundException {
        try{
            ExternalExpenseDetails details = externalExpenseRepository.getById(dto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(Constants.EXTERNAL_EXPENSE_NOT_EXISTS);
            }
            externalExpenseRepository.delete(details);
            return true;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
