package com.projectx.documents.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDto {
    private Long id;
    private String documentName;
    private String documentType;
    private MultipartFile documentFile;
}
