package com.projectx.loans.entities;

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
@Table(name = "loan_details")
public class LoanDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "loan_type")
    private Integer loanType;
    @Column(name = "loan_amount")
    private Double loanAmount;
    @Column(name = "remaining_amount")
    private Double remainingAmount;
    @OneToMany(mappedBy = "loanDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EMIDetails> emiDetails = new ArrayList<>();
    @Column(name = "status")
    private Boolean status;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
}
