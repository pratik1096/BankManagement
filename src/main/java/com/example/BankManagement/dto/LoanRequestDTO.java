package com.example.BankManagement.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.example.BankManagement.enums.LoanPurpose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanRequestDTO {

    @NotBlank
    private String accountNumber;

    @Positive
    private long eligibilityId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @Positive
    private Integer tenureInMonths;

    @NotNull
    private LoanPurpose loanPurpose;

}
