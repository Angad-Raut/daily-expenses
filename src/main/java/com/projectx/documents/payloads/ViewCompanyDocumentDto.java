package com.projectx.documents.payloads;

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
    private Long documentId;
    private Long customerId;
    private String companyName;
    private String documentType;
    private String uploadedDate;
    private String contentType;
}
