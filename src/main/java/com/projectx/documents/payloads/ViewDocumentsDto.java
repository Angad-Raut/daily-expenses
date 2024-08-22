package com.projectx.documents.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewDocumentsDto {
    private Integer srNo;
    private Long documentId;
    private String documentName;
    private String documentType;
    private String uploadedDate;
    private byte[] documentFile;
}
