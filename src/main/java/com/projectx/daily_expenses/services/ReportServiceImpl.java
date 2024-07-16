package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.Constants;
import com.projectx.daily_expenses.commons.DateRangeDto;
import com.projectx.daily_expenses.commons.EntityIdDto;
import com.projectx.daily_expenses.commons.ResourceNotFoundException;
import com.projectx.daily_expenses.dtos.ViewExpenseItemsDto;
import com.projectx.daily_expenses.dtos.ViewReportDto;
import com.projectx.daily_expenses.entities.ExpensesDetails;
import com.projectx.daily_expenses.repositories.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ExpensesRepository expensesRepository;

    @Override
    public byte[] generateReport(DateRangeDto dto) throws ParseException {
        byte[] byteData = null;
        Date startDate = Constants.getISOStartDate(dto.getStartDate());
        Date endDate = Constants.getISOEndDate(dto.getEndDate());
        AtomicInteger index = new AtomicInteger(0);
        List<ExpensesDetails> fetchList = expensesRepository.getAllExpensesWithDates(startDate,endDate);
        List<ViewReportDto> reportDataList = fetchList!=null && !fetchList.isEmpty()?fetchList.stream()
                .map(data -> setExpenseData(data,index))
                .toList():new ArrayList<>();
        if (reportDataList!=null && !reportDataList.isEmpty()) {
            return ReportGenerator.generateReport(reportDataList, dto.getStartDate(), dto.getEndDate());
        } else {
            return byteData;
        }
    }

    @Override
    public byte[] generateReportByExpenseId(EntityIdDto dto) throws ResourceNotFoundException, ParseException {
        try {
            byte[] byteData = null;
            ExpensesDetails details = expensesRepository.getExpensesById(dto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(Constants.EXPENSE_NOT_EXISTS);
            }
            AtomicInteger counter = new AtomicInteger(0);
            ViewReportDto reportDto = ViewReportDto.builder()
                    .srNo(1)
                    .expenseId(details.getId())
                    .expenseDate(Constants.toExpenseDate(details.getInsertedTime()))
                    .totalAmount(details.getTotalAmount())
                    .itemsList(details.getExpenseItems().stream()
                            .map(item -> ViewExpenseItemsDto.builder()
                                    .srNo(counter.incrementAndGet())
                                    .itemName(item.getItemName())
                                    .itemPrice(item.getItemPrice())
                                    .paymentWith(item.getPaymentType())
                                    .build())
                            .toList())
                    .build();
            if (reportDto!=null) {
                return ReportGenerator.generateReportByExpenseId(reportDto);
            } else {
                return byteData;
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ViewReportDto> getExpenseReportData(DateRangeDto dto) throws ParseException {
        Date startDate = Constants.getISOStartDate(dto.getStartDate());
        Date endDate = Constants.getISOEndDate(dto.getEndDate());
        AtomicInteger index = new AtomicInteger(0);
        List<ExpensesDetails> fetchList = expensesRepository.getAllExpensesWithDates(startDate,endDate);
        return fetchList!=null && !fetchList.isEmpty()?fetchList.stream()
                .map(data -> setExpenseData(data,index))
                .toList():new ArrayList<>();

    }

    private ViewReportDto setExpenseData(ExpensesDetails details,AtomicInteger index) {
        AtomicInteger counter = new AtomicInteger(0);
        return ViewReportDto.builder()
                .srNo(index.incrementAndGet())
                .expenseId(details.getId())
                .expenseDate(Constants.toExpenseDate(details.getInsertedTime()))
                .totalAmount(details.getTotalAmount())
                .itemsList(details.getExpenseItems().stream()
                        .map(item -> ViewExpenseItemsDto.builder()
                                .srNo(counter.incrementAndGet())
                                .itemName(item.getItemName())
                                .itemPrice(item.getItemPrice())
                                .paymentWith(item.getPaymentType())
                                .build())
                        .toList())
                .build();
    }
}
