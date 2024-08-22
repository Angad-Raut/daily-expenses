package com.projectx.documents.controllers;

import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.EntityIdWithPageRequestDto;
import com.projectx.common.payloads.ResponseDto;
import com.projectx.documents.payloads.CompanyDocDto;
import com.projectx.documents.payloads.CompanyDocumentPageResponseDto;
import com.projectx.documents.payloads.FileDownloadDto;
import com.projectx.documents.services.CompanyDocumentService;
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

    @PostMapping(value = "/deleteDocumentById")
    public ResponseEntity<ResponseDto<Boolean>> deleteDocumentById(
            @RequestBody @Valid EntityIdDto dto) {
        try {
            Boolean data = companyDocumentService.deleteDocument(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/downloadDocumentFile")
    public ResponseEntity<ResponseDto<FileDownloadDto>> downloadDocumentFile(
            @RequestBody @Valid EntityIdDto dto) {
        try {
            FileDownloadDto data = companyDocumentService.downloadFile(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),
                    null), HttpStatus.OK);
        }
    }
}
