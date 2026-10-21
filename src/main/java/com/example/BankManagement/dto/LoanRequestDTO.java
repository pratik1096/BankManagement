package com.example.BankManagement.dto;


import com.example.BankManagement.enums.LoanPurpose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanRequestDTO {

    private String accountNumber;

    private long eligibilityId;

    private BigDecimal amount;

    private Integer tenureInMonths;

    private LoanPurpose loanPurpose;

}
