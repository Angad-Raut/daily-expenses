package com.projectx.loans.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.*;
import com.projectx.loans.payloads.*;

import java.text.ParseException;
import java.util.List;

public interface LoanService {
    Boolean insertOrUpdate(LoanDto dto) throws ResourceNotFoundException, AlreadyExistsException, ParseException;
    Boolean addLoanEMI(EMIDto dto)throws ResourceNotFoundException, AlreadyExistsException;
    LoanDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    EMIDto getEMIById(EntityIdDto dto)throws ResourceNotFoundException;
    List<EntityTypeAndValueDto> getLoanTypes();
    List<EntityNameAndValueDto> getPaymentModes();
    Boolean updateStatus(EntityIdDto dto)throws ResourceNotFoundException;
    Boolean updateRemainingAmount(Long loanId,Double amount)throws ResourceNotFoundException,AlreadyExistsException;
    LoanPageResponseDto getAllLoans(PageRequestDto dto);
    EMIPageResponseDto getAllLoanEMISByLoanId(EntityIdWithPageRequestDto dto);
}
