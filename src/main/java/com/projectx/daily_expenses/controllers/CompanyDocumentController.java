package com.projectx.daily_expenses.controllers;

import com.projectx.daily_expenses.commons.EntityIdWithPageRequestDto;
import com.projectx.daily_expenses.commons.ResourceNotFoundException;
import com.projectx.daily_expenses.commons.ResponseDto;
import com.projectx.daily_expenses.dtos.CompanyDocDto;
import com.projectx.daily_expenses.dtos.CompanyDocumentPageResponseDto;
import com.projectx.daily_expenses.services.CompanyDocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/companyDocuments")
public class CompanyDocumentController {

    @Autowired
    private CompanyDocumentService companyDocumentService;

    @PostMapping(value = "/getCompanyAllDocuments")
    public ResponseEntity<ResponseDto<CompanyDocumentPageResponseDto>> getCompanyAllDocuments(
            @Valid @RequestBody EntityIdWithPageRequestDto dto) {
        try {
            CompanyDocumentPageResponseDto data = companyDocumentService.getCompanyAllDocuments(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/addDocumentByCompanyId",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseDto<Boolean>> addDocumentByCompanyId(
            @ModelAttribute @Valid CompanyDocDto dto) {
        try {
            Boolean data = companyDocumentService.addDocumentByCompanyId(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (ResourceNotFoundException | IOException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),
                    null), HttpStatus.OK);
        }
    }
}
