package com.projectx.loans.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntityTypeAndValueDto {
    private Integer entityType;
    private String entityValue;
}
