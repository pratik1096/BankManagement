package com.example.BankManagement.service;

import com.example.BankManagement.dto.AccountRequestDTO;
import com.example.BankManagement.dto.AccountResponseDTO;
import com.example.BankManagement.dto.AccountUpdateRequestDTO;
import com.example.BankManagement.dto.DepositDTO;
import com.example.BankManagement.dto.WithdrawDTO;
import com.example.BankManagement.entity.Account;
import com.example.BankManagement.enums.AccountStatus;
import com.example.BankManagement.enums.AccountType;
import com.example.BankManagement.exception.DuplicateResourceException;
import com.example.BankManagement.exception.InsufficientAccountBalanceException;
import com.example.BankManagement.exception.ResourceNotFoundException;
import com.example.BankManagement.repository.AccountRepository;
import com.example.BankManagement.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("Pratik", "123", BigDecimal.valueOf(1000), AccountType.SAVINGS, AccountStatus.ACTIVE, "999", "p@test.com");
        account.setPanCardNumber("PAN1");
        account.setAdharCardNumber("ADHAR1");
    }

    @Test
    void createAccount_success() {
        AccountRequestDTO dto = new AccountRequestDTO("Pratik", AccountType.SAVINGS, "p@test.com", "PAN1", "ADHAR1", "999");
        when(accountRepository.existsByEmailId(any())).thenReturn(false);
        when(accountRepository.existsByPanCardNumber(any())).thenReturn(false);
        when(accountRepository.existsByAdharCardNumber(any())).thenReturn(false);
        when(accountRepository.existsByAccountNumber(any())).thenReturn(false);

        accountService.createAccount(dto);

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_duplicateEmail() {
        AccountRequestDTO dto = new AccountRequestDTO("Pratik", AccountType.SAVINGS, "p@test.com", "PAN1", "ADHAR1", "999");
        when(accountRepository.existsByEmailId("p@test.com")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> accountService.createAccount(dto));
    }

    @Test
    void createAccount_duplicatePan() {
        AccountRequestDTO dto = new AccountRequestDTO("Pratik", AccountType.SAVINGS, "p@test.com", "PAN1", "ADHAR1", "999");
        when(accountRepository.existsByEmailId(any())).thenReturn(false);
        when(accountRepository.existsByPanCardNumber("PAN1")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> accountService.createAccount(dto));
    }

    @Test
    void createAccount_duplicateAdhar() {
        AccountRequestDTO dto = new AccountRequestDTO("Pratik", AccountType.SAVINGS, "p@test.com", "PAN1", "ADHAR1", "999");
        when(accountRepository.existsByEmailId(any())).thenReturn(false);
        when(accountRepository.existsByPanCardNumber(any())).thenReturn(false);
        when(accountRepository.existsByAdharCardNumber("ADHAR1")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> accountService.createAccount(dto));
    }

    @Test
    void getAccount_success() {
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
        AccountResponseDTO res = accountService.getAccount("123");
        assertEquals("123", res.getAccountNumber());
        assertEquals("Pratik", res.getName());
    }

    @Test
    void getAccount_notFound() {
        when(accountRepository.findByAccountNumber("999")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccount("999"));
    }

    @Test
    void getAccounts_success() {
        when(accountRepository.findAll()).thenReturn(List.of(account));
        List<AccountResponseDTO> list = accountService.getAccounts();
        assertEquals(1, list.size());
    }

    @Test
    void updateAccount_success() {
        AccountUpdateRequestDTO dto = new AccountUpdateRequestDTO("NewName", "new@test.com", "888", "123");
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
        accountService.updateAccount(dto);
        verify(accountRepository).save(account);
        assertEquals("NewName", account.getAccountHolderName());
    }

    @Test
    void updateAccount_notFound() {
        AccountUpdateRequestDTO dto = new AccountUpdateRequestDTO("NewName", "new@test.com", "888", "999");
        when(accountRepository.findByAccountNumber("999")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accountService.updateAccount(dto));
    }

    @Test
    void deposit_success() {
        DepositDTO dto = new DepositDTO("123", BigDecimal.valueOf(500));
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
        BigDecimal bal = accountService.deposit(dto);
        assertEquals(BigDecimal.valueOf(1500), bal);
        verify(accountRepository).save(account);
    }

    @Test
    void deposit_notFound() {
        DepositDTO dto = new DepositDTO("999", BigDecimal.valueOf(500));
        when(accountRepository.findByAccountNumber("999")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accountService.deposit(dto));
    }

    @Test
    void withdraw_success() {
        WithdrawDTO dto = new WithdrawDTO("123", BigDecimal.valueOf(500));
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
        BigDecimal bal = accountService.withdraw(dto);
        assertEquals(BigDecimal.valueOf(500), bal);
        verify(accountRepository).save(account);
    }

    @Test
    void withdraw_insufficient() {
        WithdrawDTO dto = new WithdrawDTO("123", BigDecimal.valueOf(5000));
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
        assertThrows(InsufficientAccountBalanceException.class, () -> accountService.withdraw(dto));
    }

    @Test
    void withdraw_notFound() {
        WithdrawDTO dto = new WithdrawDTO("999", BigDecimal.valueOf(500));
        when(accountRepository.findByAccountNumber("999")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accountService.withdraw(dto));
    }

    @Test
    void closeAccount_success() {
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
        accountService.closeAccount("123");
        verify(accountRepository).deleteByAccountNumber("123");
    }

    @Test
    void closeAccount_notFound() {
        when(accountRepository.findByAccountNumber("999")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accountService.closeAccount("999"));
    }
}