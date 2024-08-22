package com.projectx.documents.controllers;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.ResponseDto;
import com.projectx.documents.payloads.DocumentDropDownDto;
import com.projectx.documents.payloads.DocumentDto;
import com.projectx.documents.payloads.DownloadDocDto;
import com.projectx.documents.payloads.ViewDocumentsDto;
import com.projectx.documents.services.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/addUpdateDocument")
    public ResponseEntity<ResponseDto<Boolean>> addUpdateDocument(@ModelAttribute @Valid DocumentDto dto) {
        try {
            Boolean data = documentService.addUpdateDocument(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistsException | IOException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @GetMapping("/getDocumentDropDown")
    public ResponseEntity<ResponseDto<List<DocumentDropDownDto>>> getDocumentDropDown() {
        try {
            List<DocumentDropDownDto> data = documentService.getDocumentDropDown();
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null),HttpStatus.OK);
        }
    }

    @GetMapping("/getAllDocuments")
    public ResponseEntity<ResponseDto<List<ViewDocumentsDto>>> getAllDocuments() {
        try {
            List<ViewDocumentsDto> data = documentService.getAllDocuments();
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null),HttpStatus.OK);
        }
    }

    @PostMapping("/getDocument")
    public ResponseEntity<ResponseDto<byte[]>> getDocument(@Valid @RequestBody EntityIdDto dto) {
        try {
            byte[] data = documentService.getDocument(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null),HttpStatus.OK);
        }
    }

    @PostMapping("/downloadDocument")
    public ResponseEntity<ResponseDto<DownloadDocDto>> downloadDocument(@Valid @RequestBody EntityIdDto dto) {
        try {
            DownloadDocDto data = documentService.downloadDocument(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null),HttpStatus.OK);
        }
    }

}
