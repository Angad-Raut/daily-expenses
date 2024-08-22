package com.projectx.users.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordDto {
    @NotNull(message = "UserId cannot be null!!")
    private Long userId;
    @NotNull(message = "Enter old password!!")
    private String oldPassword;
    @NotNull(message = "Enter new password!!")
    private String newPassword;
}
