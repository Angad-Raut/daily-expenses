package com.projectx.daily_expenses.controllers;

import com.projectx.common.payloads.ResponseDto;
import com.projectx.daily_expenses.dtos.DashboardCountDto;
import com.projectx.companies.services.CompanyService;
import com.projectx.documents.services.DocumentService;
import com.projectx.daily_expenses.services.ExpenseService;
import com.projectx.incomes.services.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private CompanyService companyService;

    @GetMapping(value = "/getDashboardCount")
    public ResponseEntity<ResponseDto<DashboardCountDto>> getDashboardCounts() {
        try {
            return new ResponseEntity<>(new ResponseDto<>(
                    setDashboardCounts(),null,null),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null), HttpStatus.OK);
        }
    }

    private DashboardCountDto setDashboardCounts(){
        return DashboardCountDto.builder()
                .allExpenseCount(expenseService.getAllExpenseCount())
                .monthlyExpenseCount(expenseService.getMonthlyExpenseCount())
                .monthlyExpenseTotal(expenseService.getMonthlyExpenseTotal())
                .yearlyExpenseTotal(expenseService.getYearlyExpenseTotal())
                .documentCount(documentService.documentCount())
                .incomeSum(incomeService.getIncomeSum())
                .incomeCount(incomeService.getIncomeCount())
                .companyCount(companyService.getCompanyCount())
                .build();
    }
}
