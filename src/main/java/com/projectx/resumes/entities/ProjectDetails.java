package com.projectx.resumes.entities;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "project_details")
public class ProjectDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "company_id")
    private Long companyId;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "project_desc")
    private String projectDescription;
    @Column(name = "job_title")
    private String jobTitle;
    @Column(name = "team_size")
    private Integer teamSize;
    @Column(name = "project_domain")
    private String projectDomain;
    @Column(name = "responsibility")
    private String responsibility;
    @ElementCollection
    @CollectionTable(name = "technologies_list", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology_name")
    private List<String> technologies=new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = true)
    private EmployeeDetails employeeDetails;
    @Column(name = "inserted_time")
    private Date insertedTime;
    @Column(name = "updated_time")
    private Date updatedTime;
}
