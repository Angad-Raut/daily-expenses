package com.projectx.resumes.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "techno_stack_details")
public class TechnoStackDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "stack_name")
    private String stackName;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "techno_stack_items", joinColumns = @JoinColumn(name = "techno_stack_id"))
    private Set<StackItems> stackItems=new HashSet<>();
    @Column(name = "inserted_time")
    private Date insertedTime;
    @Column(name = "updated_time")
    private Date updatedTime;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = true)
    private EmployeeDetails employeeDetails;
}
