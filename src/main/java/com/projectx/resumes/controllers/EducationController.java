package com.projectx.resumes.controllers;


import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.EntityIdWithPageRequestDto;
import com.projectx.common.payloads.ResponseDto;
import com.projectx.resumes.payloads.EducationDto;
import com.projectx.resumes.payloads.EducationPageResponseDto;
import com.projectx.resumes.services.EducationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping(value = "/api/educationDetails")
public class EducationController {

    @Autowired
    private EducationService educationService;

    @PostMapping(value = "/insertUpdate")
    public ResponseEntity<ResponseDto<Boolean>> insertUpdate(
            @RequestBody @Valid EducationDto dto) {
        try {
            Boolean data = educationService.insertUpdate(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null), HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistsException | ParseException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getById")
    public ResponseEntity<ResponseDto<EducationDto>> getById(
            @RequestBody @Valid EntityIdDto dto) {
        try {
            EducationDto data = educationService.getById(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getEmployeeEducations")
    public ResponseEntity<ResponseDto<EducationPageResponseDto>> getEmployeeEducations(
            @RequestBody @Valid EntityIdWithPageRequestDto dto) {
        try {
            EducationPageResponseDto data = educationService.getEmployeeEducations(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException  e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }
}
