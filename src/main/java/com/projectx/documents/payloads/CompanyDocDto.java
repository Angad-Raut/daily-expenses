package com.projectx.documents.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDocDto {
    @NotNull(message = "Please select company!!")
    private Long companyId;
    @NotNull(message = "Please select document type!!")
    private String documentType;
    @NotNull(message = "Please select document file!!")
    private MultipartFile documentFile;
}
