package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.EntityIdDto;
import com.projectx.daily_expenses.commons.PageRequestDto;
import com.projectx.daily_expenses.commons.ResourceNotFoundException;
import com.projectx.daily_expenses.dtos.ExternalExpenseDto;
import com.projectx.daily_expenses.dtos.ExternalExpensePageResponseDto;

import java.text.ParseException;

public interface ExternalExpenseService {
    Boolean addUpdate(ExternalExpenseDto dto) throws ResourceNotFoundException, ParseException;
    ExternalExpenseDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    ExternalExpensePageResponseDto getAllExternalExpensesPages(PageRequestDto dto);
    Boolean updateStatus(EntityIdDto dto)throws ResourceNotFoundException;
    Boolean deleteById(EntityIdDto dto)throws ResourceNotFoundException;
}
