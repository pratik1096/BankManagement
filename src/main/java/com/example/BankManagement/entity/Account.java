package com.example.BankManagement.entity;

import com.example.BankManagement.enums.AccountStatus;
import com.example.BankManagement.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name="accounts")
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String accountHolderName;

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    @Column(nullable = false, length = 100)
    private String mobileNumber;

    @Column(nullable = false, length = 100)
    private String emailId;

    public Account() {}

    public Account(String accountHolderName, String accountNumber,
                   BigDecimal balance, AccountType accountType,
                   AccountStatus accountStatus,
                   String mobileNumber,
                   String emailId) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.accountStatus = accountStatus;
        this.mobileNumber = mobileNumber;
        this.emailId = emailId;
    }
}
