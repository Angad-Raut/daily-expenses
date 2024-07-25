package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.*;
import com.projectx.daily_expenses.dtos.*;
import com.projectx.daily_expenses.entities.ExpenseItems;
import com.projectx.daily_expenses.entities.ExpensesDetails;
import com.projectx.daily_expenses.repositories.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpensesRepository expensesRepository;

    @Override
    public Boolean createExpense(ExpenseDto expenseDto)
            throws AlreadyExistsException, InvalidDataException {
        try {
            isExpenseExist();
            Set<ExpenseItemDto> expenseItemDtos = expenseDto.getExpenseItemDtos();
            ExpensesDetails expensesDetails = ExpensesDetails.builder()
                    .expenseItems(expenseItemDtos.stream()
                            .map(data -> ExpenseItems.builder()
                                    .itemName(data.getItemName())
                                    .itemPrice(data.getItemPrice())
                                    .paymentType(data.getPaymentType())
                                    .build())
                            .collect(Collectors.toSet()))
                    .totalAmount(expenseItemDtos.stream()
                            .map(ExpenseItemDto::getItemPrice)
                            .mapToDouble(Double::doubleValue).sum())
                    .insertedTime(expenseDto.getExpenseDate()!=null?Constants.getISODate(expenseDto.getExpenseDate()):new Date())
                    .status(true)
                    .build();
            return expensesRepository.save(expensesDetails)!=null?true:false;
        } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public Boolean updateExpense(ExpenseDto expenseDto)
            throws AlreadyExistsException, ResourceNotFoundException, InvalidDataException {
        try {
            ExpensesDetails details = expensesRepository.getExpensesById(expenseDto.getId());
            if (details==null) {
                throw new ResourceNotFoundException(Constants.EXPENSE_NOT_EXISTS);
            }
            Set<ExpenseItems> expenseItems = setItems(expenseDto.getExpenseItemDtos(),details.getExpenseItems());
            details.setExpenseItems(expenseItems);
            details.setTotalAmount(expenseItems.stream()
                    .map(ExpenseItems::getItemPrice)
                    .mapToDouble(Double::doubleValue).sum());
            return expensesRepository.save(details)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public ExpensesDetails getById(EntityIdDto entityIdDto)
            throws ResourceNotFoundException {
        try {
            ExpensesDetails details = expensesRepository.getExpensesById(entityIdDto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(Constants.EXPENSE_NOT_EXISTS);
            }
            return details;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ViewExpensesDto> getAllExpensesOfMonths() {
        List<ExpensesDetails> fetchList = expensesRepository.getAllExpensesWithDates(Constants.firstDayOfMonth(),Constants.lastDayOfMonth());
        AtomicInteger index = new AtomicInteger(0);
        return !fetchList.isEmpty()?fetchList.stream()
                .map(data -> ViewExpensesDto.builder()
                        .srNo(index.incrementAndGet())
                        .expenseId(data.getId()!=null?data.getId():null)
                        .totalAmount(data.getTotalAmount()!=null?data.getTotalAmount():0.0)
                        .expenseDate(data.getInsertedTime()!=null?Constants.toExpenseDate(data.getInsertedTime()):Constants.DASH)
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public List<ViewExpensesDto> getAllExpenses() {
        List<ExpensesDetails> fetchList = expensesRepository.getAllExpenses();
        AtomicInteger index = new AtomicInteger(0);
        return !fetchList.isEmpty()?fetchList.stream()
                .map(data -> ViewExpensesDto.builder()
                        .srNo(index.incrementAndGet())
                        .expenseId(data.getId()!=null?data.getId():null)
                        .totalAmount(data.getTotalAmount()!=null?data.getTotalAmount():0.0)
                        .expenseDate(data.getInsertedTime()!=null?Constants.toExpenseDate(data.getInsertedTime()):Constants.DASH)
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public List<ViewExpenseItemsDto> getExpensesItemsByExpenseId(EntityIdDto entityIdDto)
            throws ResourceNotFoundException {
        ExpensesDetails expensesDetails = expensesRepository.getExpensesById(entityIdDto.getEntityId());
        if (expensesDetails!=null) {
            List<Object[]> fetchList = expensesRepository.getExpenseItemsByExpenseId(entityIdDto.getEntityId());
            AtomicInteger index = new AtomicInteger(0);
            return !fetchList.isEmpty() ? fetchList.stream()
                    .map(data -> ViewExpenseItemsDto.builder()
                            .srNo(index.incrementAndGet())
                            .itemName(data[0] != null ? data[0].toString():Constants.DASH)
                            .itemPrice(data[1] != null ? Double.parseDouble(data[1].toString()) : null)
                            .paymentWith(data[2] != null ? data[2].toString() : Constants.DASH)
                            .build())
                    .collect(Collectors.toList()) : new ArrayList<>();
        } else {
            throw new ResourceNotFoundException(Constants.EXPENSE_NOT_EXISTS);
        }
    }

    @Override
    public Boolean updateStatus(EntityIdDto entityIdDto)
            throws ResourceNotFoundException {
        try {
            ExpensesDetails details = expensesRepository.getExpensesById(entityIdDto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(Constants.EXPENSE_NOT_EXISTS);
            }
            Boolean status = details.getStatus()!=null && details.getStatus()?false:true;
            Integer count = expensesRepository.updateStatus(entityIdDto.getEntityId(),status);
            return count==1?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public PageResponseDto getAllExpensesPagesWithDateRange(PageRequestDto dto) throws ParseException {
        Date startDate = Constants.firstDayOfMonth();
        Date endDate = Constants.lastDayOfMonth();
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("totalAmount")) {
            sortParameter = "total_amount";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("expenseDate")) {
            sortParameter = "inserted_time";
        } else {
            sortParameter = "inserted_time";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<ExpensesDetails> expenses = expensesRepository.getAllExpensesPagesWithDateRange(
                startDate,endDate,pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<ExpensesDetails> listOfExpenses = expenses.getContent();
        List<ViewExpensesDto> expensesList = !listOfExpenses.isEmpty()?listOfExpenses.stream()
                .map(data -> ViewExpensesDto.builder()
                        .srNo(index.incrementAndGet())
                        .expenseId(data.getId())
                        .expenseDate(Constants.toExpenseDate(data.getInsertedTime()))
                        .totalAmount(data.getTotalAmount())
                        .build()).toList()
                :new ArrayList<>();
        return !expensesList.isEmpty()?PageResponseDto.builder()
                .pageNo(expenses.getNumber())
                .pageSize(expenses.getSize())
                .totalPages(expenses.getTotalPages())
                .totalElements(expenses.getTotalElements())
                .content(expensesList)
                .build():new PageResponseDto();
    }

    @Override
    public PageResponseDto getAllExpensesPages(PageRequestDto dto) {
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("totalAmount")) {
            sortParameter = "total_amount";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("expenseDate")) {
            sortParameter = "inserted_time";
        } else {
            sortParameter = "inserted_time";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<ExpensesDetails> expenses = expensesRepository.getAllExpensesPages(pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<ExpensesDetails> listOfExpenses = expenses.getContent();
        List<ViewExpensesDto> expensesList = !listOfExpenses.isEmpty()?listOfExpenses.stream()
                .map(data -> ViewExpensesDto.builder()
                        .srNo(index.incrementAndGet())
                        .expenseId(data.getId())
                        .expenseDate(Constants.toExpenseDate(data.getInsertedTime()))
                        .totalAmount(data.getTotalAmount())
                        .build()).toList()
                :new ArrayList<>();
        return !expensesList.isEmpty()?PageResponseDto.builder()
                .pageNo(expenses.getNumber())
                .pageSize(expenses.getSize())
                .totalPages(expenses.getTotalPages())
                .totalElements(expenses.getTotalElements())
                .content(expensesList)
                .build():new PageResponseDto();
    }

    @Override
    public PageResponseDto getMonthlyExpensesPages(MonthlyPageRequestDto dto) {
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("totalAmount")) {
            sortParameter = "total_amount";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("expenseDate")) {
            sortParameter = "inserted_time";
        } else {
            sortParameter = "inserted_time";
        }
        String data[] = dto.getMonthName().split(" ");
        LocalDateTime localDateTime = LocalDateTime.of(Integer.parseInt(data[1].toString()), Month.valueOf(data[0].toString().toUpperCase()),1,0,0,0,0);
        LocalDateTime startDate = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).plusHours(0).plusMinutes(0).plusSeconds(0);
        LocalDateTime endDate = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).plusHours(23).plusMinutes(59).plusSeconds(59);
        Date fromDate = Date.from(startDate.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth()).toInstant());
        Date toDate = Date.from(endDate.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth()).toInstant());
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<ExpensesDetails> expenses = expensesRepository.getAllExpensesPagesWithDateRange(
                fromDate,toDate,pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<ExpensesDetails> listOfExpenses = expenses.getContent();
        List<ViewExpensesDto> expensesList = !listOfExpenses.isEmpty()?listOfExpenses.stream()
                .map(result -> ViewExpensesDto.builder()
                        .srNo(index.incrementAndGet())
                        .expenseId(result.getId())
                        .expenseDate(Constants.toExpenseDate(result.getInsertedTime()))
                        .totalAmount(result.getTotalAmount())
                        .build()).toList()
                :new ArrayList<>();
        return !expensesList.isEmpty()?PageResponseDto.builder()
                .pageNo(expenses.getNumber())
                .pageSize(expenses.getSize())
                .totalPages(expenses.getTotalPages())
                .totalElements(expenses.getTotalElements())
                .content(expensesList)
                .build():new PageResponseDto();
    }

    @Override
    public PageResponseDto getAllExpensesPagesWithDateRangeForReport(DateRangePageRequestDto dto) throws ParseException {
        Date startDate = Constants.getISOStartDate(dto.getStartDate());
        Date endDate = Constants.getISOEndDate(dto.getEndDate());
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("totalAmount")) {
            sortParameter = "total_amount";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("expenseDate")) {
            sortParameter = "inserted_time";
        } else {
            sortParameter = "inserted_time";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<ExpensesDetails> expenses = expensesRepository.getAllExpensesPagesWithDateRange(
                startDate,endDate,pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<ExpensesDetails> listOfExpenses = expenses.getContent();
        List<ViewExpensesDto> expensesList = !listOfExpenses.isEmpty()?listOfExpenses.stream()
                .map(data -> ViewExpensesDto.builder()
                        .srNo(index.incrementAndGet())
                        .expenseId(data.getId())
                        .expenseDate(Constants.toExpenseDate(data.getInsertedTime()))
                        .totalAmount(data.getTotalAmount())
                        .build()).toList()
                :new ArrayList<>();
        return !expensesList.isEmpty()?PageResponseDto.builder()
                .pageNo(expenses.getNumber())
                .pageSize(expenses.getSize())
                .totalPages(expenses.getTotalPages())
                .totalElements(expenses.getTotalElements())
                .content(expensesList)
                .build():new PageResponseDto();
    }

    @Override
    public DashboardCountDto getDashboardCounts() {
        Integer allExpenseCount = expensesRepository.getAllExpenseCount();
        Integer monthlyExpenseCount = expensesRepository.expenseExists(Constants.firstDayOfMonth(),Constants.lastDayOfMonth());
        return DashboardCountDto.builder()
                .allExpenseCount(allExpenseCount)
                .monthlyExpenseCount(monthlyExpenseCount)
                .documentCount(0)
                .photoCount(0)
                .build();
    }

    private void isExpenseExist(){
        Integer count = expensesRepository.expenseExists(Constants.atStartOfDay(),Constants.atEndOfDay());
        if (count>0) {
            throw new AlreadyExistsException(Constants.EXPENSE_EXISTS);
        }
    }
    private void isExpenseItemExist(Long expenseId,String itemName){
        Integer count = expensesRepository.existsByExpenseName(expenseId,itemName);
        if (count>0) {
            throw new AlreadyExistsException(Constants.EXPENSE_ITEM_EXISTS);
        }
    }
    private Set<ExpenseItems> setItems(Set<ExpenseItemDto> dtos,Set<ExpenseItems> expenseItemList) {
        for(ExpenseItemDto dto:dtos) {
            if (expenseItemList.contains(dto.getItemName())) {
                throw new AlreadyExistsException(Constants.EXPENSE_ITEM_EXISTS);
            } else {
                expenseItemList.add(ExpenseItems.builder()
                        .itemName(dto.getItemName())
                        .itemPrice(dto.getItemPrice())
                        .paymentType(dto.getPaymentType())
                        .build());
            }
        }
        return expenseItemList;
    }
}
