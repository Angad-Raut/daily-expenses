package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.*;
import com.projectx.daily_expenses.dtos.IncomeDto;

import java.text.ParseException;
import java.util.List;

public interface IncomeService {
    Boolean addUpdateIncome(IncomeDto dto) throws ResourceNotFoundException,
            AlreadyExistsException, InvalidDataException, ParseException;
    IncomeDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    List<EntityNameAndValueDto> getIncomeTypesDropDown();
    Boolean updateStatus(EntityIdDto dto)throws ResourceNotFoundException;
    PaginitionResponseDto getAllIncomes(PageRequestDto dto);
    Boolean deleteById(EntityIdDto dto)throws ResourceNotFoundException;
    Integer getIncomeCount();
    String getIncomeSum();
}
