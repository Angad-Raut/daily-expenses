package com.projectx.resumes.repositories;

import com.projectx.resumes.entities.TechnoStackDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnoStackRepository extends JpaRepository<TechnoStackDetails,Long> {
    @Query(value = "select * from techno_stack_details where id=:technoId",nativeQuery = true)
    TechnoStackDetails getById(@Param("technoId")Long technoId);

    @Query(value = "select * from techno_stack_details",nativeQuery = true)
    Page<TechnoStackDetails> getAllTechnoStacks(Pageable pageable);

    @Query(value = "select * from techno_stack_details where employee_id=:employeeId",nativeQuery = true)
    Page<TechnoStackDetails> getAllTechnoStacksByEmployeeId(@Param("employeeId")Long employeeId,Pageable pageable);

    @Query(value = "select stack_name,experience from techno_stack_items where techno_stack_id=:stackId",nativeQuery = true)
    List<Object[]> getStackItems(@Param("stackId")Long stackId);
}
