package com.example.BankManagement.service;

import com.example.BankManagement.dto.*;
import com.example.BankManagement.exception.DuplicateResourceException;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    void createAccount(AccountRequestDTO accountRequestDTO);

    AccountResponseDTO getAccount(String accountNumber);

    List<AccountResponseDTO> getAccounts();

    void updateAccount(AccountUpdateRequestDTO accountUpdateRequestDTO);

    BigDecimal deposit(DepositDTO depositDTO);

    BigDecimal withdraw(WithdrawDTO withdrawDTO);

    void closeAccount(String accountNumber);

}
