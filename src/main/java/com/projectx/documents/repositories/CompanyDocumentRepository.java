package com.projectx.documents.repositories;

import com.projectx.documents.entities.CompanyDocuments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyDocumentRepository extends JpaRepository<CompanyDocuments,Long> {
    @Query(value = "select * from company_documents where id=:documentId",nativeQuery = true)
    CompanyDocuments getById(@Param("documentId")Long documentId);

    @Query(value = "select * from company_documents",nativeQuery = true)
    Page<CompanyDocuments> getAllCompanyDocuments(Pageable pageable);

    @Query(value = "select * from company_documents where company_id=:companyId",nativeQuery = true)
    Page<CompanyDocuments> getSingleCompanyDocuments(@Param("companyId")Long companyId,Pageable pageable);

    @Query(value = "select d.document_type,d.content_type,d.document_file from company_documents d "
            +"where d.id=:documentId",nativeQuery = true)
    List<Object[]> getFileDetails(@Param("documentId")Long documentId);
}
