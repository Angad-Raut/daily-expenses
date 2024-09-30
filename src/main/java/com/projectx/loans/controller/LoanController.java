package com.projectx.loans.controller;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.*;
import com.projectx.loans.payloads.*;
import com.projectx.loans.services.LoanService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/loanDetails")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping(value = "/insertOrUpdate")
    public ResponseEntity<ResponseDto<Boolean>> insertOrUpdate(@Valid @RequestBody LoanDto dto) {
        try {
            Boolean data = loanService.insertOrUpdate(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistsException | ParseException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/addLoanEMI")
    public ResponseEntity<ResponseDto<Boolean>> addLoanEMI(@Valid @RequestBody EMIDto dto) {
        try {
            Boolean data = loanService.addLoanEMI(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getLoanById")
    public ResponseEntity<ResponseDto<LoanDto>> getLoanById(@Valid @RequestBody EntityIdDto dto) {
        try {
            LoanDto data = loanService.getById(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/updateLoanStatus")
    public ResponseEntity<ResponseDto<Boolean>> updateLoanStatus(@Valid @RequestBody EntityIdDto dto) {
        try {
            Boolean data = loanService.updateStatus(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getLoanTypes")
    public ResponseEntity<ResponseDto<List<EntityTypeAndValueDto>>> getLoanTypes() {
        try {
            List<EntityTypeAndValueDto> data = loanService.getLoanTypes();
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getPaymentModes")
    public ResponseEntity<ResponseDto<List<EntityNameAndValueDto>>> getPaymentModes() {
        try {
            List<EntityNameAndValueDto> data = loanService.getPaymentModes();
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getAllLoans")
    public ResponseEntity<ResponseDto<LoanPageResponseDto>> getAllLoans(
            @Valid @RequestBody PageRequestDto dto) {
        try {
            LoanPageResponseDto data = loanService.getAllLoans(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getAllLoanEMISByLoanId")
    public ResponseEntity<ResponseDto<EMIPageResponseDto>> getAllLoanEMISByLoanId(
            @Valid @RequestBody EntityIdWithPageRequestDto dto) {
        try {
            EMIPageResponseDto data = loanService.getAllLoanEMISByLoanId(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),
                    null), HttpStatus.OK);
        }
    }
}
