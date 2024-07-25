package com.projectx.daily_expenses.controllers;

import com.projectx.daily_expenses.commons.*;
import com.projectx.daily_expenses.dtos.DashboardCountDto;
import com.projectx.daily_expenses.dtos.ExpenseDto;
import com.projectx.daily_expenses.dtos.ViewExpenseItemsDto;
import com.projectx.daily_expenses.dtos.ViewExpensesDto;
import com.projectx.daily_expenses.entities.ExpensesDetails;
import com.projectx.daily_expenses.services.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping(value = "/createExpense")
    public ResponseEntity<ResponseDto<Boolean>> createExpense(@RequestBody ExpenseDto expenseDto) {
        try {
            Boolean result = expenseService.createExpense(expenseDto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.CREATED);
        } catch (AlreadyExistsException | InvalidDataException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/updateExpense")
    public ResponseEntity<ResponseDto<Boolean>> updateExpense(@RequestBody ExpenseDto expenseDto) {
        try {
            Boolean result = expenseService.updateExpense(expenseDto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (AlreadyExistsException | InvalidDataException | ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getExpenseById")
    public ResponseEntity<ResponseDto<ExpensesDetails>> getExpenseById(@RequestBody EntityIdDto entityIdDto) {
        try {
            ExpensesDetails result = expenseService.getById(entityIdDto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/updateExpenseStatusById")
    public ResponseEntity<ResponseDto<Boolean>> updateExpenseStatusById(@RequestBody EntityIdDto entityIdDto) {
        try {
            Boolean result = expenseService.updateStatus(entityIdDto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getAllExpensesOfMonth")
    public ResponseEntity<ResponseDto<List<ViewExpensesDto>>> getAllExpensesOfMonth() {
        try {
            List<ViewExpensesDto> result = expenseService.getAllExpensesOfMonths();
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getAllExpenses")
    public ResponseEntity<ResponseDto<List<ViewExpensesDto>>> getAllExpenses() {
        try {
            List<ViewExpensesDto> result = expenseService.getAllExpenses();
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getDashboardCount")
    public ResponseEntity<ResponseDto<DashboardCountDto>> getDashboardCount() {
        try {
            DashboardCountDto result = expenseService.getDashboardCounts();
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getExpenseItemsByExpenseId")
    public ResponseEntity<ResponseDto<List<ViewExpenseItemsDto>>> getExpenseItemsByExpenseId(@RequestBody EntityIdDto entityIdDto) {
        try {
            List<ViewExpenseItemsDto> result = expenseService.getExpensesItemsByExpenseId(entityIdDto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getAllExpensesPagesWithDateRange")
    public ResponseEntity<ResponseDto<PageResponseDto>> getAllExpensesPagesWithDateRange(@Valid @RequestBody PageRequestDto dto) {
        try {
            PageResponseDto result = expenseService.getAllExpensesPagesWithDateRange(dto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }
    @PostMapping(value = "/getAllExpensesPagesWithDateRangeForReport")
    public ResponseEntity<ResponseDto<PageResponseDto>> getAllExpensesPagesWithDateRangeForReport(@Valid @RequestBody DateRangePageRequestDto dto) {
        try {
            PageResponseDto result = expenseService.getAllExpensesPagesWithDateRangeForReport(dto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getAllExpensesPages")
    public ResponseEntity<ResponseDto<PageResponseDto>> getAllExpensesPages(@Valid @RequestBody PageRequestDto dto) {
        try {
            PageResponseDto result = expenseService.getAllExpensesPages(dto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/getMonthlyExpensesPages")
    public ResponseEntity<ResponseDto<PageResponseDto>> getMonthlyExpensesPages(@Valid @RequestBody MonthlyPageRequestDto dto) {
        try {
            PageResponseDto result = expenseService.getMonthlyExpensesPages(dto);
            return new ResponseEntity<>(new ResponseDto<>(result,null,null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage(),null), HttpStatus.OK);
        }
    }
}
