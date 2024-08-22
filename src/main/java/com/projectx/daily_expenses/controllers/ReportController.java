package com.projectx.daily_expenses.controllers;

import com.projectx.common.exceptions.InvalidDataException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.DateRangeDto;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.ResponseDto;
import com.projectx.daily_expenses.dtos.MonthRequestDto;
import com.projectx.daily_expenses.dtos.SingleReportDto;
import com.projectx.daily_expenses.dtos.ViewReportDto;
import com.projectx.daily_expenses.services.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/downloadReport")
    public ResponseEntity<ResponseDto<byte[]>> downloadReport(@Valid @RequestBody DateRangeDto dto) {
        try {
            byte[] result = reportService.generateReport(dto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException | InvalidDataException | ParseException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping("/downloadReportByExpenseId")
    public ResponseEntity<ResponseDto<SingleReportDto>> downloadReportByExpenseId(@Valid @RequestBody EntityIdDto dto) {
        try {
            SingleReportDto result = reportService.generateReportByExpenseId(dto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException | InvalidDataException | ParseException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping("/getExpenseReportData")
    public ResponseEntity<ResponseDto<List<ViewReportDto>>> getExpenseReportData(@Valid @RequestBody DateRangeDto dto) {
        try {
            List<ViewReportDto> result = reportService.getExpenseReportData(dto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException | InvalidDataException | ParseException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping("/getMonthReportData")
    public ResponseEntity<ResponseDto<List<ViewReportDto>>> getMonthReportData(@Valid @RequestBody MonthRequestDto dto) {
        try {
            List<ViewReportDto> result = reportService.getMonthReportData(dto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException | InvalidDataException | ParseException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping("/generateMonthReport")
    public ResponseEntity<ResponseDto<byte[]>> generateMonthReport(@Valid @RequestBody MonthRequestDto dto) {
        try {
            byte[] result = reportService.generateMonthReport(dto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException | InvalidDataException | ParseException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

}
