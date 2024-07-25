package com.projectx.daily_expenses.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountSettingDto {
    @NotNull(message = "UserId cannot be null!!")
    private Long userId;
    private Long userMobile;
    private String userEmail;
    private MultipartFile photo;
    private MultipartFile signature;
}
