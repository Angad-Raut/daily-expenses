package com.projectx.daily_expenses.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class ExpenseItems {
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "item_price")
    private Double itemPrice;
    @Column(name = "payment_type")
    private String paymentType;
}
