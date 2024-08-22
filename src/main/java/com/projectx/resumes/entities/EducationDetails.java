package com.projectx.resumes.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "education_details")
public class EducationDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "degree_name")
    private String degreeName;
    @Column(name = "university_name")
    private String universityName;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "percentage")
    private String percentage;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = true)
    private EmployeeDetails employeeDetails;
    @Column(name = "inserted_time")
    private Date insertedTime;
    @Column(name = "updated_time")
    private Date updatedTime;
}
