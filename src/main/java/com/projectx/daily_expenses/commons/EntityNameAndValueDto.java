package com.projectx.daily_expenses.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntityNameAndValueDto {
    private String entityName;
    private String entityValue;
}
