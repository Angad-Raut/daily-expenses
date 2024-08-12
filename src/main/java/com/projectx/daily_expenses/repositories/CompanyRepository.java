package com.projectx.daily_expenses.repositories;

import com.projectx.daily_expenses.entities.CompanyDetails;
import com.projectx.daily_expenses.entities.CompanyDocuments;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyDetails,Long> {
    @Query(value = "select * from company_details where id=:companyId",nativeQuery = true)
    public CompanyDetails getById(@Param("companyId")Long companyId);
    Boolean existsCompanyDetailsByCompanyName(String companyName);

    @Transactional
    @Modifying
    @Query(value = "update company_details set status=:status where id=:companyId",nativeQuery = true)
    Integer updateStatus(@Param("companyId")Long companyId,@Param("status")Boolean status);

    @Query(value = "select * from company_details",nativeQuery = true)
    Page<CompanyDetails> getAllCompaniesPages(Pageable pageable);

    @Query(value = "select * from company_docs where company_id=companyId",nativeQuery = true)
    Page<CompanyDocuments> getCompanyDocumentsPage(@Param("companyId")Long companyId,Pageable pageable);

    @Query(value = "select * from company_docs",nativeQuery = true)
    Page<CompanyDocuments> getAllCompanyDocumentsPage(Pageable pageable);

    @Query(value = "select company_name from company_details where id=:companyId",nativeQuery = true)
    String getCompanyName(@Param("companyId")Long companyId);

    @Query(value = "select c.id,c.company_name from company_details c",nativeQuery = true)
    List<Object[]> getCompanyDropDown();
}
