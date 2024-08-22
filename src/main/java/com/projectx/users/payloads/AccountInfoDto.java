package com.projectx.users.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfoDto {
    private Long mobile;
    private String email;
    private byte[] photoUrl;
    private byte[] signatureUrl;
}
