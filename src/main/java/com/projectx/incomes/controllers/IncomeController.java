package com.projectx.incomes.controllers;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.InvalidDataException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.*;
import com.projectx.incomes.payloads.IncomeDto;
import com.projectx.incomes.payloads.PaginitionResponseDto;
import com.projectx.incomes.services.IncomeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/incomes")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @PostMapping(value = "/addUpdateIncome")
    public ResponseEntity<ResponseDto<Boolean>> addUpdateIncome(@RequestBody IncomeDto incomeDto) {
        try {
            Boolean result = incomeService.addUpdateIncome(incomeDto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.CREATED);
        } catch (AlreadyExistsException | InvalidDataException | ParseException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getById")
    public ResponseEntity<ResponseDto<IncomeDto>> getById(@RequestBody EntityIdDto entityIdDto) {
        try {
            IncomeDto result = incomeService.getById(entityIdDto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getIncomeTypesDropDown")
    public ResponseEntity<ResponseDto<List<EntityNameAndValueDto>>> getIncomeTypesDropDown() {
        try {
            List<EntityNameAndValueDto> result = incomeService.getIncomeTypesDropDown();
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/updateIncomeStatusById")
    public ResponseEntity<ResponseDto<Boolean>> updateIncomeStatusById(@RequestBody EntityIdDto entityIdDto) {
        try {
            Boolean result = incomeService.updateStatus(entityIdDto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/deleteIncomeById")
    public ResponseEntity<ResponseDto<Boolean>> deleteIncomeById(@RequestBody EntityIdDto entityIdDto) {
        try {
            Boolean result = incomeService.deleteById(entityIdDto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getAllIncomesPages")
    public ResponseEntity<ResponseDto<PaginitionResponseDto>> getAllIncomesPages(@Valid @RequestBody PageRequestDto dto) {
        try {
            PaginitionResponseDto result = incomeService.getAllIncomes(dto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }
}
