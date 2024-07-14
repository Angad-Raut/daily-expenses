package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.*;
import com.projectx.daily_expenses.dtos.ExpenseDto;
import com.projectx.daily_expenses.dtos.ExpenseItemDto;
import com.projectx.daily_expenses.dtos.ViewExpenseItemsDto;
import com.projectx.daily_expenses.dtos.ViewExpensesDto;
import com.projectx.daily_expenses.entities.ExpenseItems;
import com.projectx.daily_expenses.entities.ExpensesDetails;
import com.projectx.daily_expenses.repositories.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                    .insertedTime(new Date())
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
        List<ExpensesDetails> fetchList = expensesRepository.getMonthAllExpenses(Constants.firstDayOfMonth(),Constants.lastDayOfMonth());
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
