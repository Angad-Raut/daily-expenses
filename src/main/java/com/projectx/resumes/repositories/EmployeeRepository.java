package com.projectx.resumes.repositories;

import com.projectx.resumes.entities.EmployeeDetails;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeDetails,Long> {
    @Query(value = "select * from employee_details where id=:employeeId",nativeQuery = true)
    EmployeeDetails getById(@Param("employeeId")Long employeeId);
    Boolean existsEmployeeDetailsByEmployeeMobile(Long mobile);
    Boolean existsEmployeeDetailsByEmployeeEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update employee_details set status=:status where id=:employeeId",nativeQuery = true)
    Integer updateStatus(@Param("employeeId")Long employeeId,@Param("status")Boolean status);

    @Query(value = "select * from employee_details",nativeQuery = true)
    Page<EmployeeDetails> getAllEmployees(Pageable pageable);

    @Query(value = "select id,employee_name from employee_details",nativeQuery = true)
    List<Object[]> getEmployeeDropDown();

    @Query(value = "select company_id from employee_company_ids where employee_id=:employeeId",nativeQuery = true)
    List<Long> getEmployeeCompanies(@Param("employeeId")Long employeeId);
}
