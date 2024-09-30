package com.projectx.loans.repository;

import com.projectx.loans.entities.EMIDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EMIDetailsRepository extends JpaRepository<EMIDetails,Long> {

    @Query(value = "select * from emi_details where id=:emiId and loan_id=:loanId",nativeQuery = true)
    EMIDetails getByEMIIdAndLoanId(@Param("emiId")Long emiId,@Param("loanId")Long loanId);
    @Query(value = "select * from emi_details where loan_id=:loanId",nativeQuery = true)
    Page<EMIDetails> getAllEMIByLoanId(@Param("loanId")Long loanId, Pageable pageable);
    @Query(value = "select count(*) from emi_details "
            +"where emi_date between :startDate and :endDate and loan_id=:loanId",nativeQuery = true)
    Integer isEMIExists(@Param("loanId")Long loanId,@Param("startDate") Date startDate, @Param("endDate")Date endDate);
}
