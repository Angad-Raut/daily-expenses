package com.projectx.daily_expenses.entities;

import jakarta.persistence.*;
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
@Table(name = "company_details")
public class CompanyDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "company_address")
    private String companyAddress;
    @Column(name = "company_logo")
    private byte[] companyLogo;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "status")
    private Boolean status;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "company_docs", joinColumns = @JoinColumn(name = "comp_id"))
    private List<CompanyDocuments> companyDocuments=new ArrayList<>();
}
