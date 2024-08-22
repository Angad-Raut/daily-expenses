package com.projectx.daily_expenses.services;

import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.DateRangeDto;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.daily_expenses.dtos.MonthRequestDto;
import com.projectx.daily_expenses.dtos.SingleReportDto;
import com.projectx.daily_expenses.dtos.ViewReportDto;

import java.text.ParseException;
import java.util.List;

public interface ReportService {
    byte[] generateReport(DateRangeDto dto) throws ParseException;
    SingleReportDto generateReportByExpenseId(EntityIdDto dto)throws ResourceNotFoundException,ParseException;
    List<ViewReportDto> getExpenseReportData(DateRangeDto dto) throws ParseException;
    List<ViewReportDto> getMonthReportData(MonthRequestDto dto)throws ParseException;
    byte[] generateMonthReport(MonthRequestDto dto)throws ParseException;
}
