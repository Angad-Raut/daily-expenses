package com.projectx.resumes.repositories;

import com.projectx.resumes.entities.ProjectDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectDetails,Long> {
    @Query(value = "select * from project_details where id=:projectId",nativeQuery = true)
    ProjectDetails getById(@Param("projectId")Long projectId);

    @Query(value = "select * from project_details",nativeQuery = true)
    Page<ProjectDetails> getAllProjects(Pageable pageable);

    @Query(value = "select * from project_details where employee_id=:employeeId",nativeQuery = true)
    Page<ProjectDetails> getAllProjectsByEmployeeId(@Param("employeeId")Long employeeId,Pageable pageable);

    @Query(value = "select technology_name from technologies_list where project_id=:projectId",nativeQuery = true)
    List<String> getProjectTechnologies(@Param("projectId")Long projectId);
}
