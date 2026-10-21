package com.example.BankManagement.entity;

import com.example.BankManagement.enums.LoanPurpose;
import com.example.BankManagement.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Getter
@Setter
public class Loan {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private String loanAccountNumber;

    private String accountNumber;

    private BigDecimal loanAmount;

    private BigDecimal amountSanctioned;

    private BigDecimal amountDisbursed;

    private Long eligibilityId;

    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;

    private Integer tenureInMonths;

    private BigDecimal interestRate;

    private BigDecimal emi;

    private LocalDate nextEmiDate;

    private Integer remainingTenureMonths;

    private BigDecimal principalOutstanding;

    @Enumerated(EnumType.STRING)
    private LoanPurpose loanPurpose;

    private String remarks;

    private LocalDateTime createdOn;

    private String createdBy;

    private LocalDateTime modifiedOn;

    private String modifiedBy;

    public Loan(String loanAccountNumber, String accountNumber, BigDecimal loanAmount, BigDecimal amountSanctioned,
                BigDecimal amountDisbursed, Long eligibilityId, LoanStatus loanStatus, Integer tenureInMonths,
                BigDecimal interestRate, BigDecimal emi, LocalDate nextEmiDate, Integer remainingTenureMonths,
                BigDecimal principalOutstanding, LoanPurpose loanPurpose, String remarks, LocalDateTime createdOn,
                String createdBy, LocalDateTime modifiedOn, String modifiedBy) {
        this.loanAccountNumber = loanAccountNumber;
        this.accountNumber = accountNumber;
        this.loanAmount = loanAmount;
        this.amountSanctioned = amountSanctioned;
        this.amountDisbursed = amountDisbursed;
        this.eligibilityId = eligibilityId;
        this.loanStatus = loanStatus;
        this.tenureInMonths = tenureInMonths;
        this.interestRate = interestRate;
        this.emi = emi;
        this.nextEmiDate = nextEmiDate;
        this.remainingTenureMonths = remainingTenureMonths;
        this.principalOutstanding = principalOutstanding;
        this.loanPurpose = loanPurpose;
        this.remarks = remarks;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.modifiedOn = modifiedOn;
        this.modifiedBy = modifiedBy;
    }
}
