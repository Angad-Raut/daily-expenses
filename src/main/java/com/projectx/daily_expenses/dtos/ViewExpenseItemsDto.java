package com.projectx.daily_expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewExpenseItemsDto {
    private Integer srNo;
    private String itemName;
    private String itemPrice;
    private String paymentWith;
}
