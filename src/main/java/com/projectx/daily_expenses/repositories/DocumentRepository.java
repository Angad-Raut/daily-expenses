package com.projectx.daily_expenses.repositories;

import com.projectx.daily_expenses.entities.DocumentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentDetails,Long> {
    @Query(value = "select * from document_details where id=:documentId",nativeQuery = true)
    DocumentDetails getById(@Param("documentId")Long documentId);
    Boolean existsDocumentDetailsByDocumentName(String documentName);

    @Query(value = "select document_image from document_details where id=:documentId",nativeQuery = true)
    byte[] getDocumentFile(@Param("documentId")Long documentId);

    @Query(value = "select count(*) from document_details",nativeQuery = true)
    Integer documentCount();
}
