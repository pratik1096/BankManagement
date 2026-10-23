package com.example.BankManagement.service.impl;

import com.example.BankManagement.dto.*;
import com.example.BankManagement.entity.Account;
import com.example.BankManagement.enums.AccountStatus;
import com.example.BankManagement.exception.DuplicateResourceException;
import com.example.BankManagement.exception.ResourceNotFoundException;
import com.example.BankManagement.repository.AccountRepository;
import com.example.BankManagement.service.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public void createAccount(AccountRequestDTO accountRequestDTO) throws DuplicateResourceException {
        String accountNumber;

        if(accountRepository.existsByEmailId(accountRequestDTO.getEmailId())){
            throw new DuplicateResourceException("Account with Email Id already exists, please use sign in option to login!" + accountRequestDTO.getEmailId());
        }

        if(accountRepository.existsByPanCardNumber(accountRequestDTO.getPanCardNumber())){
            throw new DuplicateResourceException("Account with Pan Card Number already exists, please use sign in option to login!" + accountRequestDTO.getEmailId());
        }

        if(accountRepository.existsByAdharCardNumber(accountRequestDTO.getAdharCardNumber())){
            throw new DuplicateResourceException("Account with Adhar Card Number already exists, please use sign in option to login!" + accountRequestDTO.getEmailId());
        }

        do {
            accountNumber = generateAccountNumber();
        }while(accountRepository.existsByAccountNumber(accountNumber));

        Account account = new Account(
                accountRequestDTO.getAccountHolderName(),
                accountNumber,
                BigDecimal.ZERO,
                accountRequestDTO.getAccountType(),
                AccountStatus.ACTIVE,
                accountRequestDTO.getMobileNumber(),
                accountRequestDTO.getEmailId()
                );
        accountRepository.save(account);
    }

    private String generateAccountNumber(){

        long number = ThreadLocalRandom.current().nextLong(100_000_000_000L, 1_000_000_000_000L);
        return String.valueOf(number);

    }

    @Override
    public AccountResponseDTO getAccount(String accountNumber) {
        Account account =  accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountNumber));
        return new AccountResponseDTO(account.getAccountNumber(), account.getAccountHolderName(), account.getBalance());
    }

    @Override
    public List<AccountResponseDTO> getAccounts(){

        List<AccountResponseDTO> list = new ArrayList<>();

        List<Account> accountList = accountRepository.findAll();
        System.out.println(accountList);
        for(Account account : accountList){

            list.add(new AccountResponseDTO(account.getAccountNumber(), account.getAccountHolderName(), account.getBalance()));
        }
        System.out.println(list);
        return list;
    }

    @Override
    public void updateAccount(AccountUpdateRequestDTO accountUpdateRequestDTO) {
        Account account = accountRepository.findByAccountNumber(accountUpdateRequestDTO.getAccountNumber()).orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountUpdateRequestDTO.getAccountNumber()));
        account.setAccountHolderName(accountUpdateRequestDTO.getName());
        account.setMobileNumber(accountUpdateRequestDTO.getMobileNumber());
        account.setEmailId(accountUpdateRequestDTO.getEmailId());
        accountRepository.save(account);

    }

    @Override
    public BigDecimal deposit(DepositDTO depositDTO){

        Optional<Account> account = accountRepository.findByAccountNumber(depositDTO.getAccountNumber());

        account.get().setBalance(account.get().getBalance().add(depositDTO.getAmount()));

        accountRepository.save(account.get());

        return account.get().getBalance();

    }

    @Override
    public BigDecimal withdraw(WithdrawDTO withdrawDTO){

        Optional<Account> account = accountRepository.findByAccountNumber(withdrawDTO.getAccountNumber());
        if(account.get().getBalance().compareTo(withdrawDTO.getAmount())>=0){
            account.get().setBalance(account.get().getBalance().subtract(withdrawDTO.getAmount()));
            accountRepository.save(account.get());
            return account.get().getBalance();
        }

        return new BigDecimal(-1);

    }

    @Transactional
    @Override
    public void closeAccount(String accountNumber){
        accountRepository.deleteByAccountNumber(accountNumber);
    }

}
