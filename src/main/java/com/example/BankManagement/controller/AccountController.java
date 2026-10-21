package com.example.BankManagement.controller;

import com.example.BankManagement.dto.*;
import com.example.BankManagement.exception.DuplicateResourceException;
import com.example.BankManagement.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody AccountRequestDTO accountRequestDTO) {

            accountService.createAccount(accountRequestDTO);

            return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/getAccount/{accountNumber}")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable @NotNull @Positive String accountNumber){
        AccountResponseDTO response = accountService.getAccount(accountNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAccounts")
    public List<AccountResponseDTO> getAccounts(){
        return accountService.getAccounts();
    }

    @PutMapping("/updateAccount")
    public ResponseEntity<Void> updateAccount(@Valid @RequestBody AccountUpdateRequestDTO accountUpdateRequestDTO){

        accountService.updateAccount(accountUpdateRequestDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deposit")
    public BigDecimal deposit(@RequestBody DepositDTO depositDTO){
        return accountService.deposit(depositDTO);
    }

    @PostMapping("/withdraw")
    public BigDecimal withdraw(@RequestBody WithdrawDTO withdrawDTO){
        return accountService.withdraw(withdrawDTO);
    }

    @DeleteMapping("/close/{accountNumber}")
    public ResponseEntity<HttpStatus> closeAccount(@PathVariable String accountNumber){
        accountService.closeAccount(accountNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
