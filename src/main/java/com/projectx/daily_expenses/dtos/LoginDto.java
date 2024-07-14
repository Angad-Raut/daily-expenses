package com.projectx.daily_expenses.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {
    @NotNull(message = "Please enter username!!")
    private String userName;
    @NotNull(message = "Please enter password!!")
    private String password;
    @NotNull(message = "Please select login type!!")
    private Boolean isMobile;
}
