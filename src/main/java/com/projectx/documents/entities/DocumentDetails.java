package com.projectx.documents.entities;

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
@Table(name = "document_details")
public class DocumentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "document_name")
    private String documentName;
    private String documentType;
    @Lob
    @Column(name = "document_image", columnDefinition = "LONGBLOB", length = 1000)
    private byte[] documentImage;
    @Column(name = "document_status")
    private Boolean documentStatus;
    @Column(name = "inserted_time")
    private Date insertedTime;
}
