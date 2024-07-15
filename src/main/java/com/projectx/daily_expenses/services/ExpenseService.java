package com.projectx.daily_expenses.services;


import com.projectx.daily_expenses.commons.*;
import com.projectx.daily_expenses.dtos.ExpenseDto;
import com.projectx.daily_expenses.dtos.ViewExpenseItemsDto;
import com.projectx.daily_expenses.dtos.ViewExpensesDto;
import com.projectx.daily_expenses.entities.ExpensesDetails;

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
}
