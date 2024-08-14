package com.projectx.daily_expenses.repositories;

import com.projectx.daily_expenses.entities.CompanyDocuments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDocumentRepository extends JpaRepository<CompanyDocuments,Long> {
    @Query(value = "select * from company_documents where id=:documentId",nativeQuery = true)
    CompanyDocuments getById(@Param("documentId")Long documentId);

    @Query(value = "select * from company_documents",nativeQuery = true)
    Page<CompanyDocuments> getAllCompanyDocuments(Pageable pageable);

    @Query(value = "select * from company_documents where company_id=:companyId",nativeQuery = true)
    Page<CompanyDocuments> getSingleCompanyDocuments(@Param("companyId")Long companyId,Pageable pageable);
}
