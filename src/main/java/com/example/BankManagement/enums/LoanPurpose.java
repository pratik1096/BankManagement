package com.example.BankManagement.enums;

import java.math.BigDecimal;

public enum LoanPurpose {

    HOME_LOAN(new BigDecimal("8.50")),
    PERSONAL_LOAN(new BigDecimal("12.75")),
    CAR_LOAN(new BigDecimal("9.25"));

    private final BigDecimal interestRate;

    LoanPurpose(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }
}
