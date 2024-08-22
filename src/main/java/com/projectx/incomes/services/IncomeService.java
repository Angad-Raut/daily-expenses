package com.projectx.incomes.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.InvalidDataException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.EntityNameAndValueDto;
import com.projectx.common.payloads.PageRequestDto;
import com.projectx.incomes.payloads.PaginitionResponseDto;
import com.projectx.incomes.payloads.IncomeDto;

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
