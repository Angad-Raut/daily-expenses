package com.projectx.resumes.repositories;

import com.projectx.resumes.entities.EducationDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<EducationDetails,Long> {
    @Query(value = "select * from education_details where id=:educationId",nativeQuery = true)
    EducationDetails getById(@Param("educationId")Long educationId);

    @Query(value = "select * from education_details",nativeQuery = true)
    Page<EducationDetails> getAllEducations(Pageable pageable);

    @Query(value = "select * from education_details where employee_id=:employeeId",nativeQuery = true)
    Page<EducationDetails> getAllEducationsByEmployeeId(@Param("employeeId")Long employeeId,Pageable pageable);
}
