package com.example.BankManagement.controller;

import com.example.BankManagement.dto.*;
import com.example.BankManagement.enums.AccountType;
import com.example.BankManagement.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreate() throws Exception {
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO("Pratik", AccountType.SAVINGS,
                "pratik@test.com","pancard1","adhar01","mobile123");
        mockMvc.perform(post("/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetAccount() throws Exception {
        String accountNumber = "123";
        mockMvc.perform(get("/accounts/getAccount/{accountNumber}",accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountNumber)))
                .andExpect(status().isOk());
    }


    @Test
    void testGetAccounts() throws Exception {
        mockMvc.perform(get("/accounts/getAccounts")).andExpect(status().isOk());
    }

   @Test
    void testUpdateAccount() throws Exception {
       AccountUpdateRequestDTO accountUpdateRequestDTO = new AccountUpdateRequestDTO("Pratik","email","mobile","123");

       mockMvc.perform(put("/accounts/updateAccount")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(accountUpdateRequestDTO)))
               .andExpect(status().isOk());
   }

   @Test
    void testDeposit() throws Exception {
       DepositDTO depositDTO = new DepositDTO("123", BigDecimal.valueOf(1234));

       mockMvc.perform(post("/accounts/deposit")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(depositDTO)))
               .andExpect(status().isOk());


       }

    @Test
    void testWithdraw() throws Exception {
        WithdrawDTO withdrawDTO = new WithdrawDTO("123", BigDecimal.valueOf(1234));

        mockMvc.perform(post("/accounts/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withdrawDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testClose() throws Exception {
        String accountNumber = "123";

        mockMvc.perform(delete("/accounts/close/{accountNumber}",accountNumber)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(accountNumber)
                ).andExpect(status().isOk());

    }
   }
