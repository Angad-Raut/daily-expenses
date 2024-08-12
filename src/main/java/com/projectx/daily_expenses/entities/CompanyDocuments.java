package com.projectx.daily_expenses.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class CompanyDocuments {
    @Column(name = "document_type")
    private String documentType;
    @Column(name = "uploaded_date")
    private Date uploadedDate;
    @Column(name = "company_id")
    private Long companyId;
    @Lob
    @Column(name = "document_file", columnDefinition = "LONGBLOB", length = 1500)
    private byte[] documentFile;
}
