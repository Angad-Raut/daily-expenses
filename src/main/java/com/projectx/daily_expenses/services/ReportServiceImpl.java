package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.Constants;
import com.projectx.daily_expenses.commons.DateRangeDto;
import com.projectx.daily_expenses.commons.EntityIdDto;
import com.projectx.daily_expenses.commons.ResourceNotFoundException;
import com.projectx.daily_expenses.dtos.MonthRequestDto;
import com.projectx.daily_expenses.dtos.ViewExpenseItemsDto;
import com.projectx.daily_expenses.dtos.ViewReportDto;
import com.projectx.daily_expenses.entities.ExpensesDetails;
import com.projectx.daily_expenses.repositories.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
                    .totalAmount(details.getTotalAmount()!=null?Constants.toINRFormat(details.getTotalAmount()):Constants.DASH)
                    .total(details.getTotalAmount()!=null?details.getTotalAmount():0.0)
                    .itemsList(details.getExpenseItems().stream()
                            .map(item -> ViewExpenseItemsDto.builder()
                                    .srNo(counter.incrementAndGet())
                                    .itemName(item.getItemName())
                                    .itemPrice(item.getItemPrice()!=null?Constants.toINRFormat(item.getItemPrice()):Constants.DASH)
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

    @Override
    public List<ViewReportDto> getMonthReportData(MonthRequestDto dto) throws ParseException {
        byte[] byteData = null;
        String data[] = dto.getMonthName().split(" ");
        LocalDateTime localDateTime = LocalDateTime.of(Integer.parseInt(data[1].toString()), Month.valueOf(data[0].toString().toUpperCase()),1,0,0,0,0);
        LocalDateTime startDate = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).plusHours(0).plusMinutes(0).plusSeconds(0);
        LocalDateTime endDate = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).plusHours(23).plusMinutes(59).plusSeconds(59);
        Date fromDate = Date.from(startDate.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth()).toInstant());
        Date toDate = Date.from(endDate.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth()).toInstant());
        AtomicInteger index = new AtomicInteger(0);
        List<ExpensesDetails> fetchList = expensesRepository.getAllExpensesWithDates(fromDate,toDate);
        List<ViewReportDto> reportDataList = fetchList!=null && !fetchList.isEmpty()?fetchList.stream()
                .map(result -> setExpenseData(result,index))
                .toList():new ArrayList<>();
        return reportDataList;
    }

    @Override
    public byte[] generateMonthReport(MonthRequestDto dto) throws ParseException {
        byte[] byteData = null;
        List<ViewReportDto> reportDataList = getMonthReportData(dto);
        if (reportDataList!=null && !reportDataList.isEmpty()) {
            return ReportGenerator.generateMonthReport(reportDataList, dto.getMonthName());
        } else {
            return byteData;
        }
    }

    private ViewReportDto setExpenseData(ExpensesDetails details,AtomicInteger index) {
        AtomicInteger counter = new AtomicInteger(0);
        return ViewReportDto.builder()
                .srNo(index.incrementAndGet())
                .expenseId(details.getId())
                .itemCount(details.getExpenseItems().size())
                .expenseDate(Constants.toExpenseDate(details.getInsertedTime()))
                .totalAmount(details.getTotalAmount()!=null?Constants.toINRFormat(details.getTotalAmount()):Constants.DASH)
                .total(details.getTotalAmount()!=null?details.getTotalAmount():0.0)
                .itemsList(details.getExpenseItems().stream()
                        .map(item -> ViewExpenseItemsDto.builder()
                                .srNo(counter.incrementAndGet())
                                .itemName(item.getItemName())
                                .itemPrice(item.getItemPrice()!=null?Constants.toINRFormat(item.getItemPrice()):Constants.DASH)
                                .paymentWith(item.getPaymentType())
                                .build())
                        .toList())
                .build();
    }
}
