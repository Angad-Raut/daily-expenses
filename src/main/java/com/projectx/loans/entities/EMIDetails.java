package com.projectx.loans.entities;

import javax.persistence.*;
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
@Table(name = "emi_details")
public class EMIDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "emi_amount")
    private Double emiAmount;
    @Column(name = "emi_date")
    private Date emiDate;
    @Column(name = "payment_mode")
    private String paymentMode;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = true)
    private LoanDetails loanDetails;
}
