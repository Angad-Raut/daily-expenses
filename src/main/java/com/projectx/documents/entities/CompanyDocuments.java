package com.projectx.documents.entities;

import com.projectx.companies.entities.CompanyDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "company_documents")
public class CompanyDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "document_type")
    private String documentType;
    @Column(name = "uploaded_date")
    private Date uploadedDate;
    @Lob
    @Column(name = "document_file", columnDefinition = "LONGBLOB", length = 2000)
    private byte[] documentFile;
    @Column(name = "content_type")
    private String contentType;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = true)
    private CompanyDetails companyDetails;
}
