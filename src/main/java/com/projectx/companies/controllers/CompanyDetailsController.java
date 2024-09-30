package com.projectx.companies.controllers;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.InvalidDataException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.*;
import com.projectx.companies.payloads.CompanyDto;
import com.projectx.companies.payloads.CompanyPageResponseDto;
import com.projectx.companies.payloads.EditCompanyDto;
import com.projectx.companies.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/companyDetails")
public class CompanyDetailsController {

    @Autowired
    private CompanyService companyService;

    @PostMapping(value = "/addUpdate",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseDto<Boolean>> addUpdate(
            @ModelAttribute @Valid CompanyDto dto) {
        try {
            Boolean data = companyService.addUpdate(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistsException
                 | InvalidDataException | ParseException | IOException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getById")
    public ResponseEntity<ResponseDto<EditCompanyDto>> getById(
            @Valid @RequestBody EntityIdDto dto) {
        try {
            EditCompanyDto data = companyService.getById(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/updateStatus")
    public ResponseEntity<ResponseDto<Boolean>> updateStatus(
            @Valid @RequestBody EntityIdDto dto) {
        try {
            Boolean data = companyService.updateStatus(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getCompanyDocumentTypeDropDown")
    public ResponseEntity<ResponseDto<List<EntityNameAndValueDto>>> getCompanyDocumentTypeDropDown() {
        try {
            List<EntityNameAndValueDto> data = companyService.getCompanyDocumentTypeDropDown();
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getCompanyDropDown")
    public ResponseEntity<ResponseDto<List<EntityIdAndValueDto>>> getCompanyDropDown() {
        try {
            List<EntityIdAndValueDto> data = companyService.getCompanyDropDown();
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getAllCompanies")
    public ResponseEntity<ResponseDto<CompanyPageResponseDto>> getAllCompanies(
            @Valid @RequestBody PageRequestDto dto) {
        try {
            CompanyPageResponseDto data = companyService.getAllCompanies(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),
                    null), HttpStatus.OK);
        }
    }
}
