package com.example.BankManagement.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanEligibleRequestDTO {

    @NotBlank
    private String accountNumber;
    private boolean isIndianNational;
    @Positive
    private int age;
    @Positive
    private double monthlyIncome;
    @NotNull
    @Positive
    private BigDecimal loanAmount;

}
