package com.example.BankManagement.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawDTO {

    private String accountNumber;

    private BigDecimal amount;

    public WithdrawDTO(String accountNumber, BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

}
