package com.projectx.daily_expenses.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.InvalidDataException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.DateRangePageRequestDto;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.MonthlyPageRequestDto;
import com.projectx.common.payloads.PageRequestDto;
import com.projectx.daily_expenses.dtos.ExpenseDto;
import com.projectx.daily_expenses.dtos.PageResponseDto;
import com.projectx.daily_expenses.dtos.ViewExpenseItemsDto;
import com.projectx.daily_expenses.dtos.ViewExpensesDto;
import com.projectx.daily_expenses.entities.ExpensesDetails;

import java.text.ParseException;
import java.util.List;

public interface ExpenseService {
    Boolean createExpense(ExpenseDto expenseDto)throws AlreadyExistsException,
            InvalidDataException;
    Boolean updateExpense(ExpenseDto expenseDto)throws AlreadyExistsException,
            ResourceNotFoundException,InvalidDataException;
    ExpensesDetails getById(EntityIdDto entityIdDto)throws ResourceNotFoundException;
    List<ViewExpensesDto> getAllExpensesOfMonths();
    List<ViewExpensesDto> getAllExpenses();
    List<ViewExpenseItemsDto> getExpensesItemsByExpenseId(EntityIdDto entityIdDto)throws ResourceNotFoundException;
    Boolean updateStatus(EntityIdDto entityIdDto)throws ResourceNotFoundException;
    PageResponseDto getAllExpensesPagesWithDateRange(PageRequestDto dto) throws ParseException;
    PageResponseDto getAllExpensesPages(PageRequestDto dto);
    PageResponseDto getMonthlyExpensesPages(MonthlyPageRequestDto dto);
    PageResponseDto getAllExpensesPagesWithDateRangeForReport(DateRangePageRequestDto dto) throws ParseException;
    Integer getMonthlyExpenseCount();
    Integer getAllExpenseCount();
    String getMonthlyExpenseTotal();
    String getYearlyExpenseTotal();
}
