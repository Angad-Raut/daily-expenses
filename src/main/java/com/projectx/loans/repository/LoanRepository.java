package com.projectx.loans.repository;

import com.projectx.loans.entities.LoanDetails;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<LoanDetails,Long> {
    @Query(value = "select * from loan_details where id=:loanId",nativeQuery = true)
    LoanDetails getById(@Param("loanId")Long loanId);
    @Query(value = "select * from loan_details where status=:status",nativeQuery = true)
    Page<LoanDetails> getAllLoanDetails(@Param("status")Boolean status,Pageable pageable);
    @Modifying
    @Transactional
    @Query(value = "update loan_details set status=:status where id=:loanId",nativeQuery = true)
    Integer updateStatus(@Param("loanId")Long loanId,@Param("status")Boolean status);
    @Transactional
    @Modifying
    @Query(value = "update loan_details set remaining_amount=:amount where id=:loanId",nativeQuery = true)
    Integer updateRemainingAmount(@Param("loanId")Long loanId,@Param("amount")Double amount);
    @Query(value = "select remaining_amount from loan_details where id=:loanId",nativeQuery = true)
    Double getRemainingAmount(@Param("loanId")Long loanId);
    Boolean existsLoanDetailsByBankNameAndLoanType(@Param("bankName")String bankName,@Param("loanType")Integer loanType);
}
