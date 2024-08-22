package com.projectx.resumes.controllers;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdAndValueDto;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.PageRequestDto;
import com.projectx.common.payloads.ResponseDto;
import com.projectx.resumes.payloads.EmployeeCompanyIdDto;
import com.projectx.resumes.payloads.EmployeeDto;
import com.projectx.resumes.payloads.EmployeePageResponseDto;
import com.projectx.resumes.payloads.ViewEmployeeCompanyDto;
import com.projectx.resumes.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/employeeDetails")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "/addUpdate")
    public ResponseEntity<ResponseDto<Boolean>> addUpdate(@RequestBody @Valid EmployeeDto dto) {
        try {
            Boolean data = employeeService.createEmployee(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getById")
    public ResponseEntity<ResponseDto<EmployeeDto>> getById(@RequestBody @Valid EntityIdDto dto) {
        try {
            EmployeeDto data = employeeService.getById(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/updateStatus")
    public ResponseEntity<ResponseDto<Boolean>> updateStatus(@RequestBody @Valid EntityIdDto dto) {
        try {
            Boolean data = employeeService.updateStatus(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getEmployeeDropDown")
    public ResponseEntity<ResponseDto<List<EntityIdAndValueDto>>> getEmployeeDropDown() {
        try {
            List<EntityIdAndValueDto> data = employeeService.getEmployeeDropDown();
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getEmployeeCompanyDropDown")
    public ResponseEntity<ResponseDto<List<EntityIdAndValueDto>>> getEmployeeCompanyDropDown(
            @RequestBody @Valid EntityIdDto dto) {
        try {
            List<EntityIdAndValueDto> data = employeeService.getEmployeeCompanyDropDown(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getAllEmployees")
    public ResponseEntity<ResponseDto<EmployeePageResponseDto>> getAllEmployees(@RequestBody @Valid PageRequestDto dto) {
        try {
            EmployeePageResponseDto data = employeeService.getAllEmployees(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/insertUpdateEmployeeCompany")
    public ResponseEntity<ResponseDto<Boolean>> insertUpdateEmployeeCompany(
            @RequestBody @Valid EmployeeCompanyIdDto dto) {
        try {
            Boolean data = employeeService.insertUpdateEmployeeCompany(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException | AlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getEmployeeCompanies")
    public ResponseEntity<ResponseDto<List<ViewEmployeeCompanyDto>>> getEmployeeCompanies(
            @RequestBody @Valid EntityIdDto dto) {
        try {
            List<ViewEmployeeCompanyDto> data = employeeService.getEmployeeCompanies(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null), HttpStatus.OK);
        }
    }
}
