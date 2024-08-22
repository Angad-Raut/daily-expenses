package com.projectx.resumes.controllers;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.EntityIdWithPageRequestDto;
import com.projectx.common.payloads.ResponseDto;
import com.projectx.resumes.payloads.ProjectDto;
import com.projectx.resumes.payloads.ProjectPageResponseDto;
import com.projectx.resumes.payloads.TechDto;
import com.projectx.resumes.payloads.TechnologyListDto;
import com.projectx.resumes.services.ProjectService;
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
@RequestMapping(value = "/api/projectDetails")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "/insertUpdate")
    public ResponseEntity<ResponseDto<Boolean>> insertUpdate(
            @RequestBody @Valid ProjectDto dto) {
        try {
            Boolean data = projectService.insertUpdate(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null), HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getById")
    public ResponseEntity<ResponseDto<ProjectDto>> getById(
            @RequestBody @Valid EntityIdDto dto) {
        try {
            ProjectDto data = projectService.getById(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getEmployeeProjects")
    public ResponseEntity<ResponseDto<ProjectPageResponseDto>> getEmployeeProjects(
            @RequestBody @Valid EntityIdWithPageRequestDto dto) {
        try {
            ProjectPageResponseDto data = projectService.getEmployeeProjects(dto);
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

    @PostMapping(value = "/getProjectTechnologies")
    public ResponseEntity<ResponseDto<List<TechDto>>> getProjectTechnologies(
            @Valid @RequestBody EntityIdDto dto) {
        try {
            List<TechDto> data = projectService.getProjectTechnologies(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/addUpdateProjectTechnologies")
    public ResponseEntity<ResponseDto<Boolean>> addUpdateProjectTechnologies(
            @Valid @RequestBody TechnologyListDto dto) {
        try {
            Boolean data = projectService.addUpdateProjectTechnologies(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }
}
