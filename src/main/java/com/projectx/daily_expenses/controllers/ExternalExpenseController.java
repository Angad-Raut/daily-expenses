package com.projectx.daily_expenses.controllers;

import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.PageRequestDto;
import com.projectx.common.payloads.ResponseDto;
import com.projectx.daily_expenses.dtos.ExternalExpenseDto;
import com.projectx.daily_expenses.dtos.ExternalExpensePageResponseDto;
import com.projectx.daily_expenses.services.ExternalExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequestMapping(value = "/api/externalExpenses")
public class ExternalExpenseController {

    @Autowired
    private ExternalExpenseService externalExpenseService;

    @PostMapping(value = "/addUpdate")
    public ResponseEntity<ResponseDto<Boolean>> addUpdate(@RequestBody @Valid ExternalExpenseDto dto) {
        try {
            Boolean data = externalExpenseService.addUpdate(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.CREATED);
        } catch (ResourceNotFoundException | ParseException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getById")
    public ResponseEntity<ResponseDto<ExternalExpenseDto>> getById(@RequestBody @Valid EntityIdDto dto) {
        try {
            ExternalExpenseDto data = externalExpenseService.getById(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/deleteById")
    public ResponseEntity<ResponseDto<Boolean>> deleteById(@RequestBody @Valid EntityIdDto dto) {
        try {
            Boolean data = externalExpenseService.deleteById(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/updateStatus")
    public ResponseEntity<ResponseDto<Boolean>> updateStatus(@RequestBody @Valid EntityIdDto dto) {
        try {
            Boolean data = externalExpenseService.updateStatus(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getAllExternalExpensesPages")
    public ResponseEntity<ResponseDto<ExternalExpensePageResponseDto>> getAllExternalExpensesPages(@RequestBody @Valid PageRequestDto dto) {
        try {
            ExternalExpensePageResponseDto data = externalExpenseService.getAllExternalExpensesPages(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }
}
