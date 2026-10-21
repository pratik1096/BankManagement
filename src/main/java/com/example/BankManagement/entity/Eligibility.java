package com.example.BankManagement.entity;

import com.example.BankManagement.enums.EligibilityStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="eligibilities")
@Getter
@Setter
public class Eligibility {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private String accountNumber;

    private BigDecimal eligibleAmount;

    @Enumerated(EnumType.STRING)
    private EligibilityStatus status;

    private LocalDateTime validTill;

    private LocalDateTime createdAt;

    private Boolean used;

    public Eligibility(){

    }

    public Eligibility(String accountNumber, BigDecimal eligibleAmount, EligibilityStatus status,
                       LocalDateTime validTill, LocalDateTime createdAt, Boolean used) {
        this.accountNumber = accountNumber;
        this.eligibleAmount = eligibleAmount;
        this.status = status;
        this.validTill = validTill;
        this.createdAt = createdAt;
        this.used = used;
    }
}
