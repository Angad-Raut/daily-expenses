package com.projectx.resumes.controllers;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.EntityIdWithPageRequestDto;
import com.projectx.common.payloads.ResponseDto;
import com.projectx.resumes.payloads.*;
import com.projectx.resumes.services.TechnoStackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "/api/technoStackDetails")
public class TechnoStackController {

    @Autowired
    private TechnoStackService technoStackService;

    @PostMapping(value = "/insertUpdateEmployeeSkills")
    public ResponseEntity<ResponseDto<Boolean>> insertUpdateEmployeeSkills(
            @RequestBody @Valid TechnoStackDto dto) {
        try {
            Boolean data = technoStackService.insertUpdateEmployeeSkills(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null), HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getById")
    public ResponseEntity<ResponseDto<TechnoStackDto>> getById(
            @RequestBody @Valid EntityIdDto dto) {
        try {
            TechnoStackDto data = technoStackService.getById(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getEmployeeSkills")
    public ResponseEntity<ResponseDto<SkillsPageResponseDto>> getEmployeeSkills(
            @RequestBody @Valid EntityIdWithPageRequestDto dto) {
        try {
            SkillsPageResponseDto data = technoStackService.getEmployeeSkills(dto);
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

    @PostMapping(value = "/getStackItems")
    public ResponseEntity<ResponseDto<List<ViewStackItemDto>>> getStackItems(
            @RequestBody @Valid EntityIdDto dto) {
        try {
            List<ViewStackItemDto> data = technoStackService.getStackItems(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }
}
