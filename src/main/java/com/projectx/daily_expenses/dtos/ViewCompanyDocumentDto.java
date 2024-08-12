package com.projectx.daily_expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewCompanyDocumentDto {
    private Integer srNo;
    private String companyName;
    private String documentType;
    private String uploadedDate;
    private byte[] documentFile;
}
