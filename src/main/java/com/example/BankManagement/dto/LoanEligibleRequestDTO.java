package com.example.BankManagement.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanEligibleRequestDTO {

    private String accountNumber;
    private boolean isIndianNational;
    private int age;
    private double monthlyIncome;
    private BigDecimal loanAmount;

}
