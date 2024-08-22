package com.projectx.users.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private Long userMobile;
}
