package com.projectx.resumes.entities;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employee_details")
public class EmployeeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "employee_name")
    private String employeeName;
    @Column(name = "employee_mobile")
    private Long employeeMobile;
    @Column(name = "employee_email")
    private String employeeEmail;
    @Column(name = "linked_in_url")
    private String linkedInUrl;
    @Column(name = "git_hub_url")
    private String gitHubUrl;
    @Column(name = "profile_summary")
    private String profileSummary;
    @Column(name = "total_experience")
    private String totalExperience;
    @ElementCollection
    @CollectionTable(name = "employee_company_ids", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "company_id")
    private Set<Long> companyList = new HashSet<>();
    @OneToMany(mappedBy = "employeeDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EducationDetails> educationDetails=new ArrayList<>();
    @OneToMany(mappedBy = "employeeDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProjectDetails> projectDetails = new ArrayList<>();
    @OneToMany(mappedBy = "employeeDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TechnoStackDetails> skillDetails= new ArrayList<>();
    @Column(name = "status")
    private Boolean status;
    @Column(name = "inserted_date")
    private Date insertedDate;
    @Column(name = "updated_date")
    private Date updatedDate;
}
