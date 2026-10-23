package com.example.BankManagement.controller;

import com.example.BankManagement.dto.AccountRequestDTO;
import com.example.BankManagement.enums.AccountType;
import com.example.BankManagement.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


}
