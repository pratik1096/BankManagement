package com.example.BankManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountResponseDTO {

    private String accountNumber;

    private String name;

    private BigDecimal balance;

    public AccountResponseDTO(String accountNumber, String accountHolderName, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.name = accountHolderName;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "accountNumber : " + accountNumber  + ", name : " + name + ", balance : " + balance;
    }
}
