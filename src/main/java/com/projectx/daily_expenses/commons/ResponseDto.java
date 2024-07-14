package com.projectx.daily_expenses.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDto<T> {
    private T result;
    private String errorMessage;
    private Map<String,String> validationMessages=new HashMap<>();
}
