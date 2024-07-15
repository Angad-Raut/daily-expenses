package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.DateRangeDto;

import java.text.ParseException;

public interface ReportService {
    byte[] generateReport(DateRangeDto dto) throws ParseException;
}
