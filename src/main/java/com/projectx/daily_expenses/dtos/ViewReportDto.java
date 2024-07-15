package com.projectx.daily_expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewReportDto {
    private Integer srNo;
    private Long expenseId;
    private Double totalAmount;
    private String expenseDate;
    List<ViewExpenseItemsDto> itemsList = new ArrayList<>();
}
