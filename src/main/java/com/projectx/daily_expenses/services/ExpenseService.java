package com.projectx.daily_expenses.services;


import com.projectx.daily_expenses.commons.*;
import com.projectx.daily_expenses.dtos.DashboardCountDto;
import com.projectx.daily_expenses.dtos.ExpenseDto;
import com.projectx.daily_expenses.dtos.ViewExpenseItemsDto;
import com.projectx.daily_expenses.dtos.ViewExpensesDto;
import com.projectx.daily_expenses.entities.ExpensesDetails;
import org.springframework.data.domain.Page;

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
    DashboardCountDto getDashboardCounts();
}
