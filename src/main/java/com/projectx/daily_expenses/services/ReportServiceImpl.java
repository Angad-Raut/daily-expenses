package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.Constants;
import com.projectx.daily_expenses.commons.DateRangeDto;
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
        Date startDate = Constants.getISODate(dto.getStartDate());
        Date endDate = Constants.getISODate(dto.getEndDate());
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
